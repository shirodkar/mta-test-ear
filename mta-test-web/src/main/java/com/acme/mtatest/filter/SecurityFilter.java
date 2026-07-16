package com.acme.mtatest.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acme.mtatest.util.JwtTokenUtil;
import com.acme.web.framework.WebHelper;
import com.acme.web.security.WebSecurityFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebFilter(urlPatterns = "/api/*")
public class SecurityFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(SecurityFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    private JwtTokenUtil jwtTokenUtil;

    private WebSecurityFilter webSecurityFilter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        webSecurityFilter = new WebSecurityFilter();
        webSecurityFilter.setCsrfEnabled(true);
        webSecurityFilter.setXssProtectionEnabled(true);
        logger.info("Security filter initialized with context path: {}", WebHelper.getContextPath());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = webSecurityFilter.sanitizeInput(httpRequest.getRequestURI());

        boolean isAjax = WebHelper.isAjaxRequest(httpRequest.getHeader("X-Requested-With"));
        if (isAjax) {
            logger.debug("AJAX request detected for path: {}", path);
        }

        if (isPublicEndpoint(path) || "OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("Missing or invalid authorization header for path: {}", path);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtTokenUtil.validateToken(token)) {
            logger.warn("Invalid JWT token for path: {}", path);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("Security filter destroyed");
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/accounts/validate")
                || path.contains("/transit-time/estimate");
    }
}
