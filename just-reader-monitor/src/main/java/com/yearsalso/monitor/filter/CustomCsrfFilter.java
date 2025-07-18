package com.yearsalso.monitor.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

/**
 * 自定义Csrf过滤器
 */
public class CustomCsrfFilter extends OncePerRequestFilter {

    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

    /**
     * 重写过滤器
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
            String token = csrf.getToken();
            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie(CSRF_COOKIE_NAME, token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }

}
