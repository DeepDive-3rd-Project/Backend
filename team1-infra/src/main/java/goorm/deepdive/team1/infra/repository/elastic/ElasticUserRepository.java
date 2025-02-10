package goorm.deepdive.team1.infra.repository.elastic;

import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ElasticUserRepository {
	private final ElasticsearchClient elasticsearchClient;


}
