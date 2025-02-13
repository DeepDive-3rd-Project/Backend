package goorm.deepdive.team1.api.user.presentation.resonse;

import java.util.Map;

import lombok.Builder;

@Builder
public record UserStatsResponse(
	Long total,
	Map<String, Integer> genderStats,
	Map<String, Integer> regionStats,
	Map<String, Integer> ageStats
) {
	public static UserStatsResponse from(Map<String, Object> stats) {
		return UserStatsResponse.builder()
			.total((Long)stats.getOrDefault("total", 0))
			.genderStats((Map<String, Integer>) stats.getOrDefault("genderStats", Map.of()))
			.regionStats((Map<String, Integer>) stats.getOrDefault("regionStats", Map.of()))
			.ageStats((Map<String, Integer>) stats.getOrDefault("ageStats", Map.of()))
			.build();
	}
}
