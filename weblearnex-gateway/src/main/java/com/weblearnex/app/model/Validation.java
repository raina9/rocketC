package com.weblearnex.app.model;

import com.weblearnex.app.setup.User;
import org.apache.commons.lang3.StringUtils;

public class Validation {
	
	public static String validateUser(User user) {
		String message="";
		if(user!=null) {
		if(StringUtils.isEmpty(user.getEmail())) {
			message="please enter email, ";
		}
		if(user.getPassword().isEmpty()) {
			message+="please enter password, ";
		}
		
		if(user.getFisrtName().isEmpty()) {
			message+="please enter first name, ";
		}
		message = message.replaceAll(",$", "");
		}
		return message;
	}
	
	
	
	
	
	

}
