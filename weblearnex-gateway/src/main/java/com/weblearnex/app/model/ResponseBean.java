package com.weblearnex.app.model;



import com.weblearnex.app.constant.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean<T>  {
	private String message;
	private ResponseStatus status;
	private T responseBody;
}
	


