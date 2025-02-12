package goorm.deepdive.team1.domain.user.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import goorm.deepdive.team1.domain.common.BaseTimeEntity;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "\"user\"")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = false)
	private Integer age;

	public static User create(String name, String email, String phoneNumber, Gender gender, Integer age) {
		return User.builder()
				.name(name)
				.email(email)
				.phoneNumber(phoneNumber)
				.gender(gender)
				.age(age)
				.build();
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void updateGender(Gender gender) { this.gender = gender; }

	public void updateAge(Integer age) {this.age = age;}
}
