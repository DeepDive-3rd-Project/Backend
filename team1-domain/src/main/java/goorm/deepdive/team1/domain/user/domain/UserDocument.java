package goorm.deepdive.team1.domain.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@Document(indexName = "user_address")
@AllArgsConstructor
public class UserDocument {
	@Id
	private String userId;

	private String name;

	private String email;

	private String phoneNumber;

	private String regionAddress;

	private String roadAddress;

	public void update(String name, String regionAddress, String roadAddress) {
		this.name = name;
		this.regionAddress = regionAddress;
		this.roadAddress = roadAddress;
	}

	public UserDocument create(Long id, String name, String email, String phoneNumber, String regionAddress, String roadAddress) {
		return UserDocument.builder()
			.userId(String.valueOf(id))
			.name(name)
			.email(email)
			.phoneNumber(phoneNumber)
			.regionAddress(regionAddress)
			.roadAddress(roadAddress)
			.build();
	}
}
