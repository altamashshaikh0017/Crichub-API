package com.as.crichub.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

	private boolean success;

	private String message;

	private int statusCode;

	private Object data;

}
