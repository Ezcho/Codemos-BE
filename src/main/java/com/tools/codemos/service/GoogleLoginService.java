package com.tools.codemos.service;

import com.tools.codemos.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleLoginService {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;


    private final GoogleUserService googleUserService;
    private UserRepository userRespository;
    private AuthService authService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect_uri}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String SCOPE;

    private final static String GOOGLE_AUTH_URL = "https://accounts.google.com";
    //Authorization 코드 받기 위한 도메인
    private final static String GOOGLE_API_URI = "https://oauth2.googleapis.com";
    private final static String GOOGLE_USERINFO_URL = "https://www.googleapis.com";

    public GoogleLoginService(GoogleUserService googleUserService) {
        this.googleUserService = googleUserService;
    }

    public String getGoogleAuthorizationCode() {
        return GOOGLE_AUTH_URL + "/o/oauth2/auth"
                + "?client_id=" + GOOGLE_CLIENT_ID
                + "&redirect_uri=" + GOOGLE_REDIRECT_URL
                + "&response_type=code" + "scope="+SCOPE;
    }
    //Flow는 여기서KakaoController 로 이동

    public void getGoogleToken(String authCode) throws Exception { // cf) alt + f7: 사용된 곳 보기
        if (authCode == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-type", "application/x-www-form-urlencoded");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", GOOGLE_CLIENT_ID);
            params.add("client_secret", GOOGLE_CLIENT_SECRET);
            params.add("redirect_uri", GOOGLE_REDIRECT_URL);
            params.add("code", authCode);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    GOOGLE_API_URI + "/token",
                    HttpMethod.POST,    //POST요청
                    httpEntity,         //위에서만든 httpEntity객체
                    String.class        //restTemplate.exchange() 메서드를 호출할 때, 응답 본문을 String 타입의 객체로 변환
            );
            System.out.println("HTTP ENTITY: "+ response);
            JSONObject jsonObj = new JSONObject(response.getBody());
            accessToken = jsonObj.getString("access_token");
            System.out.println("accessToken: "+accessToken);

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        getUserInfoWithToken(accessToken);   //리턴해서 이제 accessToken을 파라미터로 아래 함수 호출
    }

    private void getUserInfoWithToken(String accessToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //HttpHeader 담기, 위의 과정과 동일
        RestTemplate restTem = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTem.exchange(
                GOOGLE_USERINFO_URL + "/oauth2/v2/userinfo",
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        JSONObject jsonObj = new JSONObject(response.getBody());
        String email = jsonObj.getString("email");
        String picture = jsonObj.getString("picture");
        String name = jsonObj.getString("name");

    }
}