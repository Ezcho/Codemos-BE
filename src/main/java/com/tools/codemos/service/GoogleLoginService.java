package com.tools.codemos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoogleLoginService {

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect_uri}")
    private String KAKAO_REDIRECT_URL;

    private static final String FRIENDS_LIST_SERVICE_URL = "https://kapi.kakao.com/v1/api/talk/friends";
    //RestAPI, 리디렉트 uri, secret 코드 각각 private 선언
    //1211 친구목록 가져오는 url 추가

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    //Authorization 코드 받기 위한 도메인
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";
    //Access TOken 받은 후 유저 정보 받기위한 도메인

    //Authorization코드, 즉 인가코드를 받기위한 최초요청
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }
    //Flow는 여기서KakaoController 로 이동

    public Member getKakaoInfo(String code, HttpServletRequest request) throws Exception { // cf) alt + f7: 사용된 곳 보기
        //얘는 KakaoController의 호출로 넘어옴, KakaoDTO class의 함수
        if (code == null) throw new Exception("Failed get authorization code");
        //Authorization 코드를 받지 못할 경우 예외 리턴
        //추후에 받을 AccessToken과 Refresh토큰 선언
        String accessToken = "";
        String refreshToken = "";

        try {
            //Http헤더 객체 선언
            HttpHeaders headers = new HttpHeaders();
            //아래의 양식에 따라 전체 URL만드는 일련의 과정
            //AccessToken, RefreshToken 요청
            //curl -v -X POST "https://kauth.kakao.com/oauth/token" \
            // -H "Content-Type: application/x-www-form-urlencoded" \
            // -d "grant_type=authorization_code" \
            // -d "client_id=${REST_API_KEY}" \
            // --data-urlencode "redirect_uri=${REDIRECT_URI}" \
            // -d "code=${AUTHORIZE_CODE}"

            headers.add("Content-type", "application/x-www-form-urlencoded");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);
            params.add("code", code);

            RestTemplate restTemplate = new RestTemplate();
            //RestTemplate 클래스는, Java에서 RESTful 서비스를 소비하는 데 사용되는 클래스,
            //GET, POST, PUT, DELETE 등의 HTTP 메소드를 사용하여 요청을 보냄
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
            System.out.println("httpEntity: " + httpEntity);
            //위에서 삽입했던 headers와 params를 가지고 Http Entity를 맹글어줌(URL 탄생)

            //ResponseEntity를 통해 restTemplate객체로 요청했을때 응답을 받아옴,
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,    //POST요청
                    httpEntity,         //위에서만든 httpEntity객체
                    String.class        //restTemplate.exchange() 메서드를 호출할 때, 응답 본문을 String 타입의 객체로 변환
            );

            JSONParser jsonParser = new JSONParser();   //Json파서 선언
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            //JsonObj 선언 String객체로 변환한 response를 다시 JsonData로 변환

            accessToken = (String) jsonObj.get("access_token");    //access_token획득
            refreshToken = (String) jsonObj.get("refresh_token");   //refresh_token획득
            System.out.println("access_token: " + accessToken);      //출력함 해보고
            System.out.println("refresh_token: " + refreshToken);

            HttpSession session = request.getSession();
            session.setAttribute("accessToken", accessToken);
            //session.setAttribute("refreshToken", refreshToken);
            //리프토큰은 세션에 저장X

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        return getUserInfoWithToken(accessToken, refreshToken);   //리턴해서 이제 accessToken을 파라미터로 아래 함수 호출
    }

    private Member getUserInfoWithToken(String accessToken, String refreshToken) throws Exception {
        //HttpHeader 생성
        //curl -v -X GET "https://kapi.kakao.com/v2/user/me" \
        //  -H "Authorization: Bearer ${ACCESS_TOKEN}"
        //위 양식에 따라 요청,
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기, 위의 과정과 동일
        RestTemplate restTem = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTem.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 JSON 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");
        System.out.println("json: "+ jsonObj);

        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));
        String profileImgUrl = String.valueOf(profile.get("profile_image_url"));

        KakaoDTO kakaoUser = KakaoDTO.builder()
                .email(email)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
        // 사용자 정보를 Member 엔티티에 저장
        return saveKakaoUser(kakaoUser, refreshToken);
    }
    private Member saveKakaoUser(KakaoDTO kakaoUser, String refreshToken) {
        System.out.println("save User in member repo");
        Optional<Member> existingMember = memberRepository.findByEmail(kakaoUser.getEmail()); //repo메서드로 조회
        if (existingMember.isPresent()) {
            Member member = existingMember.get();   //기존정보 get해서
            member.setNickname(kakaoUser.getNickname());    //닉네임 업뎃
            member.setRefreshToken(refreshToken);   //refreshToken업뎃
            member.setProfileImgUrl(kakaoUser.getProfileImgUrl());  //프로필 아이콘 업데이트
            return memberRepository.save(member);  //저장
        } else {
            Member newMember = Member.builder() //빌더로 설정
                    .mpw("1111") // 비번1111고정
                    .email(kakaoUser.getEmail()) //
                    .nickname(kakaoUser.getNickname()) // 카카오 닉네임
                    .refreshToken(refreshToken) // 리프레시 토큰
                    //.roleSet(Collections.singleton(MemberRole.USER)) // 역할 설정
                    .profileImgUrl(kakaoUser.getProfileImgUrl())
                    .build();
            return memberRepository.save(newMember);
        }

    }

    public KakaoDTO getUserInfoWithTokenPub(String accessToken) throws Exception {
        //HttpHeader 생성
        //curl -v -X GET "https://kapi.kakao.com/v2/user/me" \
        //  -H "Authorization: Bearer ${ACCESS_TOKEN}"
        //위 양식에 따라 요청,
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기, 위의 과정과 동일
        RestTemplate restTem = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTem.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));
        String profileImgUrl = String.valueOf(profile.get("profile_image_url"));

        return KakaoDTO.builder()
                .email(email)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
    }

    public List<KakaoFriendDTO> getFriendListWithToken(String accessToken) throws ParseException {
        try {
            String url = "https://kapi.kakao.com/v1/api/talk/friends";

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            // HttpEntity 생성
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // GET 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 본문(JSON) 파싱
            List<KakaoFriendDTO> list = new ArrayList<>();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray elements = (JSONArray) jsonObj.get("elements");

            for (Object element : elements) {
                KakaoFriendDTO dto = new KakaoFriendDTO();
                JSONObject obj = (JSONObject) element;
                dto.setId(obj.get("id").toString());
                dto.setUuid(obj.get("uuid").toString());
                dto.setFavorite((boolean) obj.get("favorite"));
                dto.setProfile_nickname(obj.get("profile_nickname").toString());
                dto.setProfile_thumbnail_image(obj.get("profile_thumbnail_image").toString());
                list.add(dto);
            }
            return list;
        }catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new InsufficientScopeException("Additional consent required for 'friends' scope.", getAdditionalConsentURL());
            }
            throw ex;
        }
    }


    public String getAdditionalConsentURL() { //친구목록 가져오기 추가동의
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code"
                + "&scope=friends"; //추가 스코프 요청
    }

    public static class InsufficientScopeException extends RuntimeException {
        private final String consentUrl;

        public InsufficientScopeException(String message, String consentUrl) {
            super(message);
            this.consentUrl = consentUrl;
        }
        public String getConsentUrl() {
            return consentUrl;
        }
    }
}