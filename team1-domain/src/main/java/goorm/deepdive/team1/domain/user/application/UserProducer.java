package goorm.deepdive.team1.domain.user.application;

import goorm.deepdive.team1.domain.user.domain.User;

public interface UserProducer {
	void sendMessageToCreate(User user);

	void sendMessageToUpdate(User user);
}
