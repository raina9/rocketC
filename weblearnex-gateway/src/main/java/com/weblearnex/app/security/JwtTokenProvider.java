package com.weblearnex.app.security;

import com.weblearnex.app.exception.CustomException;
import com.weblearnex.app.model.SecureUserDetails;
import com.weblearnex.app.reposatory.JwtTokenRepository;
import com.weblearnex.app.setup.JwtToken;
import com.weblearnex.app.setup.Page;
import com.weblearnex.app.setup.Role;
import com.weblearnex.app.setup.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Component
public class JwtTokenProvider {
    private static final String AUTH="auth";
    private static final String AUTHORIZATION="Authorization";
    private String secretKey="secret-key";
    private long validityInMilliseconds = 3600000*10; // 1h
    // private long validityInMilliseconds = 60000*2; // 1 min

    @Autowired
    private JwtTokenRepository jwtTokenRepository;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH,roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token =  Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
        jwtTokenRepository.save(new JwtToken(token,validity));
        return token;
    }
    public JwtToken updateToken(JwtToken jwtToken) {
        return jwtTokenRepository.save(jwtToken);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);
        /*if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }*/
        if (bearerToken != null ) {
            return bearerToken;
        }
        return null;
    }

    public boolean validateToken(String token) throws JwtException,IllegalArgumentException{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
    }
    public JwtToken isTokenPresentInDB (String token) {
        return jwtTokenRepository.findByToken(token);
    }
    public Boolean deleteToken(String token){
        return jwtTokenRepository.deleteJwtTokenByToken(token);
    }
    //user details with out database hit
    public UserDetails getUserDetails(String token) {
        String userName =  getUsername(token);
        List<String> roleList = getRoleList(token);
        UserDetails userDetails = new SecureUserDetails(userName,roleList.toArray(new String[roleList.size()]));
        return userDetails;
    }
    public List<String> getRoleList(String token) {
        return (List<String>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(AUTH);
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public Authentication getAuthentication(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        String userName = userDetails.getUsername();
        //HttpSession session = request.getSession();
        //User user = (User) session.getAttribute(userName);
        // TODO url based validation not applied now it will applied in future.
        /*String requestURL = request.getServletPath();
        List<Role> roles =user.getRole();
        boolean isFound =false;
        for(Role r:roles){
            for(Page p :r.getPages()){
                if(p.getPageURL().equals(requestURL)){
                    isFound =true;
                    break;
                }
            }
            if(isFound)
                break;
        }
        if(!isFound){
            throw new CustomException("you are not authorized for this page", HttpStatus.UNAUTHORIZED);
        }*/
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
