package com.groupware.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * [보안팀 마법 상자 - CryptoUtil]
 * 역할: 사원들의 소중한 개인정보(비밀번호 등)를 암호 모양으로 바꾸거나, 원래대로 해독해주는 클래스입니다.
 */
public class CryptoUtil {

    // [보안 열쇠 설정] 그룹웨어 시스템 전체에서 딱 하나만 존재하는 초강력 비밀 마스터키 (32글자)
    private static final String SECRET_KEY = "groupwareKey12345678901234567890"; 
    // [암호화 설정] 암호가 더 복잡하게 꼬이도록 앞의 16글자를 따서 보조 열쇠(IV)로 씁니다.
    private static final String IV = SECRET_KEY.substring(0, 16); 
    // [암호화 알고리즘] 현재 전 세계 관공서 및 금융권에서 표준으로 쓰는 가장 안전한 국가대표급 암호화 방식(AES-256)
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * ■ 1. 글자 암호화 하기 (회원가입 할 때 사용)
     * 목적: 사용자가 입력한 진짜 비밀번호(예: "asdf123")를 아무도 알아볼 수 없는 외계어 문장으로 바꿉니다.
     * * @param plainText 사용자가 입력한 원래 글자 (평문)
     * @return 컴퓨터만 해독할 수 있게 꽁꽁 묶인 외계어 문자열 (암호문)
     */
    public static String encrypt(String plainText) {
        // 만약 입력된 값이 빈 값이거나 아무것도 없다면 가공하지 않고 그대로 돌려보냅니다.
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        
        try {
            // 자바에게 우리가 정한 비밀 마스터키와 보조 열쇠를 넘겨주며 "이걸로 잠글 준비해"라고 명령합니다.
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

            // 기계 장치(Cipher)에 AES-256 도장을 세팅합니다.
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // "지금부터 암호화 모드(ENCRYPT_MODE)로 작동 개시!"
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // [핵심 연산] 진짜 글자를 컴퓨터 바이트 단위로 쪼개서 완전히 짓이겨버립니다(암호화 실행).
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            
            // 암호화된 외계어 바이트를 메모장이나 DB에 텍스트 형태로 예쁘게 저장할 수 있도록 'Base64'라는 문자로 포장해서 리턴합니다.
            return Base64.getEncoder().encodeToString(encryptedBytes);
            
        } catch (Exception e) {
            // 만약 암호화 하다가 기계에 오류가 나면 에러 내용을 모니터에 출력하고 시스템을 안전하게 방어합니다.
            e.printStackTrace();
            throw new RuntimeException("보안 시스템: 암호화 처리 중 알 수 없는 오류가 발생했습니다.", e);
        }
    }

    /**
     * ■ 2. 외계어 해독하기 (★신구 데이터 자동 판별 하이브리드 버전)
     * 목적: DB에서 꺼내온 비밀번호가 암호 형태면 원래 글자로 풀어주고, 암호화 안 된 옛날 글자면 에러 없이 그대로 통과시킵니다.
     * * @param cipherText DB에서 꺼내온 정체불명의 비밀번호 글자
     * @return 해독이 완료된 진짜 원본 글자 (평문)
     */
    public static String decrypt(String cipherText) {
        // 비어있는 데이터면 곧바로 돌려보내서 오류(NullPointer)를 방지합니다.
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }
        
        try {
            // 암호화 때와 똑같이 비밀 마스터키와 보조 열쇠를 준비합니다. (열쇠가 다르면 절대로 안 열립니다.)
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // "이번엔 해독 모드(DECRYPT_MODE)로 작동 개시!"
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            // [1단계 검문] 꽁꽁 묶여있던 포장지(Base64)를 뜯어내어 바이트 데이터로 변환합니다.
            // 옛날 평문 데이터(예: "1234")가 들어오면 포장지 규격이 안 맞아서 여기서 바로 에러(Exception)가 발생합니다!
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            
            // [2단계 검문] 뜯어낸 바이트 데이터를 우리 마스터키로 완벽하게 복호화(해독) 합니다.
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            // 모든 검문을 무사히 통과했다면 사람이 읽을 수 있는 깨끗한 한글/영문 문자열로 조립해서 돌려줍니다.
            return new String(decryptedBytes, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            /* * ★★★ [일반인을 위한 핵심 안전장치] ★★★
             * 만약 위의 1단계나 2단계 검문 과정에서 "어라? 이거 암호문이 아니잖아!" 혹은 "키가 안 맞는데?" 하고 에러가 터지면
             * 웹사이트가 블루스크린처럼 멈추는 대신, 이 곳(catch)으로 안전하게 대피합니다.
             * * "에러가 났다는 것은 이 데이터가 애초에 암호화된 적이 없는 '과거 가입자의 평문 비밀번호'라는 결정적 증거!"
             * 따라서 시스템은 아무 일도 없었다는 듯이 넘겨받았던 옛날 글자(cipherText)를 날것 그대로 반환합니다.
             */
            return cipherText; 
        }
    }
}