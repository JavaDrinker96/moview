package com.example.moview.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class CookieUtils {

    public Optional<Cookie> getCookie(final HttpServletRequest request, final String name) {

        final Cookie[] cookies = request.getCookies();

        if (Objects.nonNull(cookies) && cookies.length > 0) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public void addCookie(final HttpServletResponse response, final String name, final String value, final int maxAge) {

        final Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void deleteCookie(final HttpServletRequest request, final HttpServletResponse response, final String name){

        final Cookie[] cookies = request.getCookies();

        if(Objects.nonNull(cookies) && cookies.length>0){
            for (Cookie cookie: cookies){
                if(cookie.getName().equals(name)){
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public String serialize(final Object object){
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public <T> T deserialize(final Cookie cookie, final Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
