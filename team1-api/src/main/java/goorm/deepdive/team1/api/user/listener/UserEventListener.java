package goorm.deepdive.team1.api.user.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {
	private final AddressHistoryCommandService addressHistoryCommandService;

	@EventListener
	public void handleUserCreatedEvent(UserCreatedEvent event) {
		User user = event.user();
		addressHistoryCommandService.create(user);
	}
}
