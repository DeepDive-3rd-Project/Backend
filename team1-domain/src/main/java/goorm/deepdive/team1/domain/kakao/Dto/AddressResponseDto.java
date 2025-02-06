package goorm.deepdive.team1.domain.kakao.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class AddressResponseDto {
        private double y;      // 위도
        private double x;       // 경도
        private String regionAddress;  // 지번 주소
        private String roadAddress;    // 도로명 주소
}
