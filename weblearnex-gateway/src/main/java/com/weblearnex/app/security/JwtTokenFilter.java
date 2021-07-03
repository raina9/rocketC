package com.weblearnex.app.security;

import com.weblearnex.app.exception.CustomException;
import com.weblearnex.app.reposatory.JwtTokenRepository;
import com.weblearnex.app.setup.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenProvider jwtTokenProvider;
     private long validityInMilliseconds = 3600000*10; // 1h
    // private long validityInMilliseconds = 60000*2; // 1m
    @Autowired
    private JwtTokenRepository jwtTokenRepository;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);

        if (token != null) {
            JwtToken tkn = null;
            try{
                tkn = jwtTokenProvider.isTokenPresentInDB(token);
            }catch (IncorrectResultSizeDataAccessException e){
                // If multiple token found then delete existing token.
                if(request.getRequestURI().contains("/signin")){
                    jwtTokenProvider.deleteToken(token);
                }
            }
            if (tkn==null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
                throw new CustomException("Invalid JWT token",HttpStatus.UNAUTHORIZED);
            }
            try {
                Date now = new Date();
                Date validity = new Date(now.getTime() );
                Date tokenDate = tkn.getValidity();
                if(tokenDate != null && validity.compareTo(tokenDate)==-1){
                    tkn.setValidity(new Date(now.getTime() + validityInMilliseconds));
                    // jwtTokenProvider.updateToken(tkn);
                }else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
                    throw new CustomException("Invalid JWT token",HttpStatus.UNAUTHORIZED);
                }

                 //jwtTokenProvider.validateToken(token) ;
            } catch (JwtException | IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
                throw new CustomException("Invalid JWT token",HttpStatus.UNAUTHORIZED);
            }
            // If token expired then catch and send UNAUTHORIZED exception.
            Authentication auth = null;
            try{
                auth = token != null ? jwtTokenProvider.getAuthentication(token,request) : null;
            }catch (ExpiredJwtException expiredJwtException){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWT token expired.");
                throw new CustomException("JWT token expired",HttpStatus.UNAUTHORIZED);
            }
            //setting auth in the context.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, res);

    }
}
