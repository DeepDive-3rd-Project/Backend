package goorm.deepdive.team1.infra.config.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import goorm.deepdive.team1.infra.config.base.Team1Config;

@EntityScan(basePackages = "sorisoop.soridam.domain")
@Configuration
@EnableJpaRepositories(basePackages = "goorm.deepdive.team1.infra")
public class JpaConfig implements Team1Config {
}
