package goorm.deepdive.team1.api.kakao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.common.exception.KakaoApiException.*;
import goorm.deepdive.team1.api.kakao.response.KakaoApiAddressResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiAddressService {

    @Value("${kakao.api.key}")
    private String restApiKey;

    public KakaoApiAddressResponse getGeoDataFromAddress(String address) {
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String requestUrl = apiUrl + "?query=" + encodedAddress;

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + restApiKey);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            int responseCode = conn.getResponseCode();

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
            throw new ApiCallFailedException();
        }
    }

    private CustomException getKakaoApiException(int responseCode) {
        return switch (responseCode) {
            case 400 -> new InvalidRequestException();
            case 403 -> new UnauthorizedException();
            case 429 -> new RateLimitExceededException();
            case 503 -> new ServiceDisabledException();
            default -> new ApiCallFailedException();
        };
    }

    private KakaoApiAddressResponse parseJsonResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray documents = jsonObject.getJSONArray("documents");

        if (documents.isEmpty()) {
            throw new AddressNotFoundException();
        }

        JSONObject data = documents.getJSONObject(0);

        double longitude = data.getDouble("x");  //경도
        double latitude = data.getDouble("y");  //위도

        JSONObject address = data.optJSONObject("address");
        JSONObject roadAddress = data.optJSONObject("road_address");

        if (address == null || roadAddress == null) {
            throw new AddressNotFoundException();
        }

        String region = address.optString("address_name", null);
        String roadName = roadAddress.optString("address_name", null);

        if (region == null || roadName == null) {
            throw new AddressNotFoundException();
        }

        return KakaoApiAddressResponse.builder()
                .x(longitude)
                .y(latitude)
                .regionAddress(region)
                .roadAddress(roadName)
                .build();
        }
    }
