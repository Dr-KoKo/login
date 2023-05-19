package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String MY_SESSION_ID_NAME = "mySessionId";
    private static Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {
        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(MY_SESSION_ID_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie mySessionIdCookie = findCookie(request, MY_SESSION_ID_NAME);
        if(mySessionIdCookie == null) {
            return null;
        }
        return sessionStore.get(mySessionIdCookie.getValue());
    }

    public void expireSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request, MY_SESSION_ID_NAME);
        sessionStore.remove(cookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }

        return null;
    }

}
