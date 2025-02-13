package goorm.deepdive.team1.infra.repository.elastic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.infra.repository.elastic.exception.ElasticQueryExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticUserRepository {
	private final ElasticsearchClient elasticsearchClient;
	private static final String INDEX_NAME = "user_address";

	public Page<UserDocument> searchByRoadAddress(String roadAddress, Pageable pageable){
		return search("roadAddress", roadAddress, pageable);
	}

	public Page<UserDocument> searchByRegionAddress(String regionAddress, Pageable pageable){
		return search("regionAddress", regionAddress, pageable);
	}

	public Page<UserDocument> searchByName(String name, Pageable pageable){
		return search("name", name, pageable);
	}

	private Page<UserDocument> search(String field, String keyword, Pageable pageable){
		Query query = Query.of(q -> q
			.match(m -> m
				.field(field)
				.query(keyword)
			)
		);
		SearchResponse<UserDocument> response;

		try {
			response = elasticsearchClient.search(s -> s
					.index(INDEX_NAME)
					.query(query)
					.from((int) pageable.getOffset())
					.size(pageable.getPageSize()),
				UserDocument.class
			);
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}

		List<UserDocument> userList = response.hits().hits().stream()
			.map(Hit::source)
			.collect(Collectors.toList());

		long totalHits = response.hits().total() != null
			? response.hits().total().value()
			: userList.size();

		return new PageImpl<>(userList, pageable, totalHits);
	}

	public void save(UserDocument userDocument) {
		try {
			elasticsearchClient.index(IndexRequest.of(i -> i
				.index(INDEX_NAME)
				.id(userDocument.getUserId())
				.document(userDocument)
			));
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}
	}

	public Map<String, Object> searchUserStatistics(List<String> genders, List<String> regions, List<String> ageGroups) {
		try {
			SearchResponse<Void> response = elasticsearchClient.search(s -> s
					.index(INDEX_NAME)
					.size(0)
					.query(q -> q
						.bool(b -> b
							.must(getGenderQuery(genders))
							.must(getRegionQuery(regions))
							.must(getAgeRangeQuery(ageGroups))
						)
					)
					.aggregations("genderStats", a -> a.terms(t -> t.field("gender")))
					.aggregations("regionStats", a -> a.terms(t -> t.field("region")))
					.aggregations("ageStats", a -> a.range(r -> r.field("age").ranges(getAgeBuckets(ageGroups))))
				, Void.class);

			long total = response.hits().total() != null ? response.hits().total().value() : 0;

			Map<String, Long> genderStats = fillMissingKeys(extractTermsAggregation(response, "genderStats"), genders);
			Map<String, Long> regionStats = fillMissingKeys(extractTermsAggregation(response, "regionStats"), regions);
			Map<String, Long> ageStats = fillMissingKeys(extractRangeAggregation(response.aggregations().get("ageStats")), ageGroups);

			return Map.of(
				"total", total,
				"genderStats", genderStats,
				"regionStats", regionStats,
				"ageStats", ageStats
			);
		} catch (Exception e) {
			throw new ElasticQueryExecutionException();
		}
	}

	private Query getGenderQuery(List<String> genders) {
		if (genders == null || genders.isEmpty()) return Query.of(q -> q.matchAll(m -> m));
		return Query.of(q -> q.terms(t -> t
			.field("gender")
			.terms(v -> v.value(genders.stream()
				.map(FieldValue::of)
				.toList()
			))
		));
	}

	private Query getRegionQuery(List<String> regions) {
		if (regions == null || regions.isEmpty()) return Query.of(q -> q.matchAll(m -> m));
		return Query.of(q -> q.terms(t -> t
			.field("region")
			.terms(v -> v.value(regions.stream()
				.map(FieldValue::of)
				.toList()
			))
		));
	}


	public Query getAgeRangeQuery(List<String> ageGroups) {
		return Query.of(q -> q.bool(b -> b
			.should(ageGroups.stream()
				.map(this::getAgeRangeQueryForGroup)
				.collect(Collectors.toList()))
			.minimumShouldMatch(String.valueOf(1))
		));
	}

	private Query getAgeRangeQueryForGroup(String ageGroup) {
		NumberRangeQuery.Builder rangeQuery = new NumberRangeQuery.Builder().field("age");
		switch (ageGroup) {
			case "10.0-20.0":
				rangeQuery.gte(10.0).lt(20.0);
				break;
			case "20.0-30.0":
				rangeQuery.gte(20.0).lt(30.0);
				break;
			case "30.0-40.0":
				rangeQuery.gte(30.0).lt(40.0);
				break;
			case "40.0-50.0":
				rangeQuery.gte(40.0).lt(50.0);
				break;
			case "50.0-60.0":
				rangeQuery.gte(50.0).lt(60.0);
				break;
			case "60.0-*":
				rangeQuery.gte(60.0);
				break;
			default:
				throw new IllegalArgumentException(ageGroup);
		}
		return Query.of(q -> q.range(r -> r.number(rangeQuery.build())));
	}

	private Map<String, Long> extractTermsAggregation(SearchResponse<Void> response, String aggName) {
		return response.aggregations().get(aggName).sterms().buckets().array().stream()
			.collect(Collectors.toMap(bucket -> bucket.key()._get().toString(), StringTermsBucket::docCount));
	}

	private Map<String, Long> extractRangeAggregation(Aggregate aggregation) {
		return aggregation.range().buckets().array().stream()
			.collect(Collectors.toMap(bucket -> bucket.key().toString(),
				MultiBucketBase::docCount));
	}

	private List<AggregationRange> getAgeBuckets(List<String> ageGroups) {
		return ageGroups.stream()
			.map(this::getAgeBucket)
			.toList();
	}

	private AggregationRange getAgeBucket(String ageGroup) {
		return switch (ageGroup) {
			case "10.0-20.0" -> AggregationRange.of(r -> r.from(10.0).to(20.0));
			case "20.0-30.0" -> AggregationRange.of(r -> r.from(20.0).to(30.0));
			case "30.0-40.0" -> AggregationRange.of(r -> r.from(30.0).to(40.0));
			case "40.0-50.0" -> AggregationRange.of(r -> r.from(40.0).to(50.0));
			case "50.0-60.0" -> AggregationRange.of(r -> r.from(50.0).to(60.0));
			case "60.0-*" -> AggregationRange.of(r -> r.from(60.0));
			default -> throw new IllegalArgumentException(ageGroup);
		};
	}

	private Map<String, Long> fillMissingKeys(Map<String, Long> originalData, List<String> requestedKeys) {
		if (requestedKeys == null) return originalData;
		return requestedKeys.stream()
			.collect(Collectors.toMap(
				key -> key,
				key -> originalData.getOrDefault(key, 0L)
			));
	}
}
