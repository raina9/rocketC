package com.weblearnex.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weblearnex.app.constant.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean<T>  implements Serializable {
	private String message;
	private ResponseStatus status;
	//private int statusCode;
	private T responseBody;
}
	


