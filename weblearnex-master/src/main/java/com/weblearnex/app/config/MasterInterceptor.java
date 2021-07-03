package com.weblearnex.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.exception.CustomException;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.RedisService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MasterInterceptor implements HandlerInterceptor {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    Gson gson = new Gson();

    @Autowired
    private RedisService redisService;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
            throws Exception {
        log.info("Request is complete");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
            throws Exception {
        log.info("Handler execution is complete");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // log.info("Before Handler execution");
        String token = request.getHeader("Authorization");
        String userID = request.getHeader("userID");
        if(request.getRequestURI().contains("swagger") || request.getRequestURI().contains("/vendor")
                || request.getRequestURI().contains("/getAllAwbSeries") || request.getRequestURI().contains("/reloadStatusFlow")
                || request.getRequestURI().contains("/web/tracking") || request.getRequestURI().contains("/deletesSaleOrderTransactionLog")){
            return true;
        }
        if(token == null || userID == null){
            new CustomException("Unauthorized request", HttpStatus.UNAUTHORIZED);
            return false;
        }

        Object userObject = redisService.get(userID);
        if(userObject == null){
            new CustomException("Unauthorized user id", HttpStatus.UNAUTHORIZED);
            return false;
        }
        User user = objectMapper.readValue((String) userObject, User.class);
        /*if(!user.getToken().equals(token)){
            new CustomException("Invalid authorization token", HttpStatus.UNAUTHORIZED);
            return false;
        }*/

        if(UserType.CLIENT.equals(user.getType())){
            sessionUserBean.setClientId(clientRepository.findByClientCode(user.getClientCode()).getId());
        }
        sessionUserBean.setUser(user);
        return true;
    }
}
