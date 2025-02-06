package goorm.deepdive.team1.domain.kakao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.common.exception.KakaoApiExceptionCode;
import goorm.deepdive.team1.domain.kakao.Dto.AddressResponseDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiAddressService {

    private static final String REST_API_KEY = "9cd742e01ee31be1eb4ac4d5fb33500f";

    public static void main(String[] args) {
        String address = "경기도 고양시 덕양구 화중로 219"; // 🔹 변환하고 싶은 주소
        AddressResponseDto response = getGeoDataFromAddress(address);

            System.out.println("지번 주소: " + response.getRegionAddress());
            System.out.println("도로명 주소: " + response.getRoadAddress());
            System.out.println("위도: " + response.getY());
            System.out.println("경도: " + response.getX());

    }


    public static AddressResponseDto getGeoDataFromAddress(String address) {
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String requestUrl = apiUrl + "?query=" + encodedAddress;

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + REST_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                return parseJsonResponse(response.toString());
            } else {
                throw getKakaoApiException(responseCode);
            }
        } catch (CustomException e) {
                throw e;

        } catch (Exception e) {
            throw new CustomException(KakaoApiExceptionCode.API_CALL_FAILED);
        }
    }

    private static CustomException getKakaoApiException(int responseCode) {
        return switch (responseCode) {
            case 400 -> new CustomException(KakaoApiExceptionCode.INVALID_REQUEST);
            case 403 -> new CustomException(KakaoApiExceptionCode.UNAUTHORIZED);
            case 429 -> new CustomException(KakaoApiExceptionCode.RATE_LIMIT_EXCEEDED);
            case 503 -> new CustomException(KakaoApiExceptionCode.SERVICE_DISABLED);
            default -> new CustomException(KakaoApiExceptionCode.API_CALL_FAILED);
        };
    }

    private static AddressResponseDto parseJsonResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray documents = jsonObject.getJSONArray("documents");

        if (documents.isEmpty()) {
            throw new CustomException(KakaoApiExceptionCode.NOT_FOUND);
        }

        JSONObject data = documents.getJSONObject(0);

        double latitude = data.getDouble("y");
        double longitude = data.getDouble("x");

        JSONObject address = data.optJSONObject("address");
        JSONObject roadAddress = data.optJSONObject("road_address");

        if (address == null || roadAddress == null) {
            throw new CustomException(KakaoApiExceptionCode.NOT_FOUND);
        }

        String region = address.optString("address_name", null);
        String roadName = roadAddress.optString("address_name", null);

        if (region == null || roadName == null) {
            throw new CustomException(KakaoApiExceptionCode.NOT_FOUND);
        }

        return AddressResponseDto.builder()
                .x(latitude)
                .y(longitude)
                .regionAddress(region)
                .roadAddress(roadName)
                .build();
        }
    }
