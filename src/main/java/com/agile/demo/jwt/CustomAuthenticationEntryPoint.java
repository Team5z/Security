package com.agile.demo.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
// import org.springframework.security.core.jwt.JwtExceptionCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String exception = (String) request.getAttribute("exception");
        log.error("Commence Get Exception : {}", exception);

//        JwtException JwtExceptionCode;
//        JwtExceptionCode = null;
        if(exception == null) {
            log.error("entry point >> exception is null");
            setResponse(response, JwtException.NOT_FOUND_TOKEN);
        }
        //잘못된 토큰인 경우
        else if(exception.equals(JwtException.INVALID_TOKEN.getCode())) {
            log.error("entry point >> invalid token");
            setResponse(response, JwtException.INVALID_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(JwtException.EXPIRED_TOKEN.getCode())) {
            log.error("entry point >> expired token");
            setResponse(response, JwtException.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(JwtException.UNSUPPORTED_TOKEN.getCode())) {
            log.error("entry point >> unsupported token");
            setResponse(response, JwtException.UNSUPPORTED_TOKEN);
        }
        else if (exception.equals(JwtException.NOT_FOUND_TOKEN.getCode())) {
            log.error("entry point >> not found token");
            setResponse(response, JwtException.NOT_FOUND_TOKEN);
        }
        else {
            setResponse(response, JwtException.UNKNOWN_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, JwtException exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", exceptionCode.getMessage());
        errorInfo.put("code", exceptionCode.getCode());
//        Gson gson = new Gson();
//        String responseJson = gson.toJson(errorInfo);
//        response.getWriter().print(responseJson);
    }
}

