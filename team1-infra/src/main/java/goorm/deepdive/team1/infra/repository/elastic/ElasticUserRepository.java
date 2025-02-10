package goorm.deepdive.team1.infra.repository.elastic;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.infra.repository.elastic.exception.ElasticQueryExecutionException;
import lombok.RequiredArgsConstructor;

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
}
