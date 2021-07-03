package  com.weblearnex.app.controller;

import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.model.LoginRequest;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.Validation;
import com.weblearnex.app.setup.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.weblearnex.app.service.ILoginService;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private ILoginService iLoginService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@CrossOrigin("*")
    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<ResponseBean> login(@RequestBody LoginRequest loginRequest , HttpServletRequest request) {
         ResponseEntity<ResponseBean> responseBean = iLoginService.login(loginRequest.getUsername(),loginRequest.getPassword(),request);
        HttpHeaders headers = new HttpHeaders();
        List<String> headerlist = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerlist.add("Content-Type");
        headerlist.add(" Accept");
        headerlist.add("X-Requested-With");
        headerlist.add("Authorization");
        headers.setAccessControlAllowHeaders(headerlist);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        return responseBean;
        //return new ResponseEntity<ResponseBean>(new ResponseBean("Token created successfully", ResponseStatus.SUCCESS, token),HttpStatus.CREATED);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@CrossOrigin("*")
    @PostMapping("/signout")
    @ResponseBody
    public ResponseEntity<ResponseBean> logout (@RequestHeader(value="Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
      if (iLoginService.logout(token)) {
          headers.remove("Authorization");
          return new ResponseEntity<ResponseBean>(new ResponseBean("signout successfully", com.weblearnex.app.constant.ResponseStatus.SUCCESS, token),HttpStatus.CREATED);
      }
        return new ResponseEntity<ResponseBean>(new ResponseBean("Logout Failed", com.weblearnex.app.constant.ResponseStatus.SUCCESS, token),HttpStatus.CREATED);
    }

   
    @PostMapping("/valid/token")
    @ResponseBody
    public Boolean isValidToken (@RequestHeader(value="Authorization") String token) {
        return true;
    }


    @SuppressWarnings("rawtypes")
	@PostMapping("/signin/token")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseBean> createNewToken (@RequestHeader(value="Authorization") String token) {
        String newToken = iLoginService.createNewToken(token);
        HttpHeaders headers = new HttpHeaders();
        List<String> headerList = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerList.add("Content-Type");
        headerList.add(" Accept");
        headerList.add("X-Requested-With");
        headerList.add("Authorization");
        headers.setAccessControlAllowHeaders(headerList);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", newToken);
        return new ResponseEntity<ResponseBean>(new ResponseBean("Get Token successfully", com.weblearnex.app.constant.ResponseStatus.SUCCESS, token),HttpStatus.CREATED);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody User user) {
    	String validate = 	Validation.validateUser(user);
		User userExists = iLoginService.getUser(user.getEmail());
		if (userExists != null) {
			return new ResponseEntity<ResponseBean>(new ResponseBean("Email id already existed", com.weblearnex.app.constant.ResponseStatus.SUCCESS, user) ,HttpStatus.ACCEPTED);
		}else if(!validate.isEmpty()) {
			return new ResponseEntity<ResponseBean>(new ResponseBean("Validation Fail"+validate, com.weblearnex.app.constant.ResponseStatus.SUCCESS, user),HttpStatus.ACCEPTED);
		}
		iLoginService.saveUser(user);
		 return new ResponseEntity<ResponseBean>(new ResponseBean("User register successfully", com.weblearnex.app.constant.ResponseStatus.SUCCESS, user),HttpStatus.OK);
	}
}
