package com.weblearnex.app.service.serviceimpl;


import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.exception.CustomException;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.JwtTokenRepository;
import com.weblearnex.app.reposatory.UserRepository;
import com.weblearnex.app.service.RedisService;
import com.weblearnex.app.setup.JwtToken;
import com.weblearnex.app.setup.Role;
import com.weblearnex.app.setup.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.weblearnex.app.security.JwtTokenProvider;
import com.weblearnex.app.service.ILoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LoginService implements ILoginService
{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenRepository jwtTokenRepository;
    @Autowired
    private RedisService redisService;

   /* @Override
    public String login(String username, String password, HttpServletRequest request) {
        try {
            authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByEmail(username);
            if (user == null || user.getRole() == null || user.getRole().isEmpty()) {
                throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
            }
            request.getSession().setAttribute("user", user);
            String token =  jwtTokenProvider.createToken(username, user.getRole().stream().map((Role role)-> "ROLE_"+role.getUserRole()).filter(Objects::nonNull).collect(Collectors.toList()));
            return token;
        } catch (AuthenticationException e) {
        	e.printStackTrace();
            throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }*/

    @Override
    public ResponseEntity<ResponseBean> login(String userID, String password,HttpServletRequest request) {
        ResponseBean responseBean =new ResponseBean();
        try {
            // authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(userID,password));
            // User user = userRepository.findByEmail(userID);
            User user = userRepository.findByLoginId(userID);
            if (user == null) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Invalid user id.");
                return new ResponseEntity(responseBean,HttpStatus.OK);
            }
            if ((user.getRole() == null || user.getRole().isEmpty()) && (user.getAdmin() == null || user.getAdmin() == false)) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("No role assigned to user.");
                return new ResponseEntity(responseBean,HttpStatus.OK);
            }
            if (!user.getPassword().equals(password)) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Invalid password.");
                return new ResponseEntity(responseBean,HttpStatus.OK);
            }
            if (user.getActive().equals(0)) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Inactive user id.");
                return new ResponseEntity(responseBean,HttpStatus.OK);
            }
            //NOTE: normally we dont need to add "ROLE_" prefix. Spring does automatically for us.
            //Since we are using custom token using JWT we should add ROLE_ prefix
            //request.getSession().setAttribute(userID, user);
            String token =  jwtTokenProvider.createToken(userID, user.getRole().stream()
                    .map((Role role)-> "ROLE_"+role.getName()).filter(Objects::nonNull).collect(Collectors.toList()));
            Map<String ,Object> responseMap = new HashMap<>();
            responseMap.put("token",token);
            responseMap.put("user",user);
            responseBean.setMessage("Login successfully");
            responseBean.setResponseBody(responseMap);
            responseBean.setStatus(ResponseStatus.SUCCESS);

            // save user in redis
            user.setToken(token);
            redisService.save(user.getLoginId(), user);
            return new ResponseEntity(responseBean,HttpStatus.OK);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            //throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(responseBean,HttpStatus.OK);
    }

    @Override
    public User saveUser(User user) {
        //user.setPassword(passwordEncoder.encode(user.getPassword()) );
        return userRepository.save(user);
    }

    @Override
    public boolean logout(String token) {
         jwtTokenRepository.delete(new JwtToken(token));
         return true;
    }

    @Override
    public Boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String createNewToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        List<String>roleList = jwtTokenProvider.getRoleList(token);
        String newToken =  jwtTokenProvider.createToken(username,roleList);
        return newToken;
    }

	@Override
	public User getUser(String username) {
		 	return userRepository.findByEmail(username);
	}
}
