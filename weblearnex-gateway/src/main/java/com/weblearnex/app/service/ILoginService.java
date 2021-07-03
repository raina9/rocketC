package com.weblearnex.app.service;



import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.setup.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {

    ResponseEntity<ResponseBean> login(String username, String password, HttpServletRequest request);

    User saveUser(User user);

    boolean logout(String token);

    Boolean isValidToken(String token);

    String createNewToken(String token);
    
    User getUser(String username);
}
