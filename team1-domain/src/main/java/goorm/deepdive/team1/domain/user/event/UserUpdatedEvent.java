package goorm.deepdive.team1.domain.user.event;

import goorm.deepdive.team1.domain.user.domain.User;

public record UserUpdatedEvent(
	User user
) {
}
