package eum.backed.server.service.community;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eum.backed.server.exception.TokenException;
import eum.backed.server.service.community.DTO.KakaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client-id}")
    private String CLIENT_ID;
    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;
    @Value("${kakao.admin-key}")
    private String ADMIN_KEY;

    public String getKakaoAccessT(String code){
        String reqURL = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code",code);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                reqURL,
                HttpMethod.POST,
                kakaoTokenReq,
                String.class
        );
        String tokenJson = response.getBody();
        JSONObject jsonObject = new JSONObject(tokenJson);
        String accessToken = jsonObject.getString("access_token");
        return accessToken;
    }
    public KakaoDTO.KaKaoInfo createKakaoUser(String accessToken)  {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";
        String uid ="";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if(hasEmail){
                uid = element.getAsJsonObject().get("id").getAsString();
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

            }
            br.close();
            KakaoDTO.KaKaoInfo kaKaoInfo = KakaoDTO.KaKaoInfo.builder().uid(uid).email(email).build();
        return kaKaoInfo;

        } catch (IOException e) {
            e.printStackTrace();
            throw new TokenException("카카오 엑세스 토큰 오류");
        }
    }
    public void WithdralKakao(String uid) {
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        Long longUid = Long.valueOf(uid);
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "KakaoAK " + ADMIN_KEY); //전송할 header 작성, access_token전송
            String data = "target_id_type=user_id&target_id=" + longUid;
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }

