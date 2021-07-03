package com.weblearnex.app.api;

import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ApiCallServiceImpl implements ApiCallService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseBean GetApiCall(ApiConfig apiConfig) {
        ResponseBean responseBean = new ResponseBean();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            if(apiConfig.getHeaderParems() != null && !"".equals(apiConfig.getHeaderParems().trim())){
                for(String hdr : apiConfig.getHeaderParems().split(",")){
                    String str[] = hdr.split("=");
                    headers.set(str[0], str[1]);
                }
            }
            String url = apiConfig.getApiUrl();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity responseEntity = null;
            try{
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            }catch(HttpClientErrorException.BadRequest badRequest){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(badRequest.getResponseBodyAsString());
                return responseBean;
            }catch (HttpClientErrorException.Unauthorized unauthorized){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(unauthorized.getResponseBodyAsString());
                return responseBean;
            }catch (HttpClientErrorException.Forbidden forbidden){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(forbidden.getResponseBodyAsString());
                return responseBean;
            }catch (Exception e){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(e.getMessage());
                return responseBean;
            }

            if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
                String response = (String) responseEntity.getBody();
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(response);
                return responseBean;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        responseBean.setStatus(ResponseStatus.FAIL);
        responseBean.setMessage("Error at get api call");
        return responseBean;
    }

    @Override
    public ResponseBean PostApiCall(ApiConfig apiConfig, Object requestObject) {
        ResponseBean responseBean = new ResponseBean();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            if(apiConfig.getHeaderParems() != null && !"".equals(apiConfig.getHeaderParems().trim())){
                for(String hdr : apiConfig.getHeaderParems().split(",")){
                    String str[] = hdr.split("=");
                    headers.set(str[0], str[1]);
                }
            }
            HttpEntity<Object> entity = new HttpEntity<Object>(requestObject,headers);
            ResponseEntity responseEntity = null;
            try{
                responseEntity = restTemplate.exchange(apiConfig.getApiUrl(), HttpMethod.POST, entity, String.class);
            }catch(HttpClientErrorException.BadRequest badRequest){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(badRequest.getResponseBodyAsString());
                return responseBean;
            }catch (HttpClientErrorException.Unauthorized unauthorized){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(unauthorized.getResponseBodyAsString());
                return responseBean;
            }catch (HttpClientErrorException.Forbidden forbidden){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(forbidden.getResponseBodyAsString());
                return responseBean;
            }catch (Exception e){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(e.getMessage());
                return responseBean;
            }
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
                String response = (String) responseEntity.getBody();
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(response);
                return responseBean;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        responseBean.setStatus(ResponseStatus.FAIL);
        responseBean.setMessage("Error at post api call");
        return responseBean;
    }
}
