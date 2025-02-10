package goorm.deepdive.team1.api.kakao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.common.exception.KakaoApiException.*;
import goorm.deepdive.team1.api.kakao.response.KakaoApiAddressResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiAddressService {

    private final ObjectMapper objectMapper;

    public KakaoApiAddressService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode documents = rootNode.path("documents");

            if (documents.isEmpty()) {
                throw new AddressNotFoundException();
            }

            JsonNode data = documents.get(0);

            double longitude = data.path("x").asDouble();
            double latitude = data.path("y").asDouble();

            JsonNode addressNode = data.path("address");
            JsonNode roadAddressNode = data.path("road_address");

            if (addressNode.isMissingNode() || roadAddressNode.isMissingNode()) {
                throw new AddressNotFoundException();
            }

            String region = addressNode.path("address_name").asText(null);
            String roadName = roadAddressNode.path("address_name").asText(null);

            if (region == null || roadName == null) {
                throw new AddressNotFoundException();
            }

            return KakaoApiAddressResponse.of(longitude,latitude,region,roadName);
        } catch (Exception e) {
            throw new ApiCallFailedException();
        }
    }
    }
