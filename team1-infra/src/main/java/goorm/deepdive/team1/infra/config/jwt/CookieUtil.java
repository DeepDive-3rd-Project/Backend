//package goorm.deepdive.team1.infra.config.jwt;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class CookieUtil {
//
//    public static final int COOKIE_EXPIRED_AGE = 0;
//    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
//
//    private CookieUtil(){
//    }
//
//    public static Cookie createCookie(String cookieName, String data, int exp) {
//        Cookie cookie = new Cookie(cookieName, data);
//        cookie.setHttpOnly(true);
////        cookie.setSecure(true);
////        cookie.setPath("/"); 쿠키가 적용될 범위?
//        cookie.setMaxAge(exp);
//        return cookie;
//    }
//
//    public static void clearAuthCookie(HttpServletResponse response, String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(COOKIE_EXPIRED_AGE);
//        response.addCookie(cookie);
//    }
//}
