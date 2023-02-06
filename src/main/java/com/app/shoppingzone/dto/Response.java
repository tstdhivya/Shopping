package com.app.shoppingzone.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

	private Object data;
	private Error error;
	private String timeStamp;
	private String message;
	private List<String> errorMessages;

	@Override
	public String toString() {
		return "Response [response=" + data + ", error=" + error + ", timeStamp=" + timeStamp + "]";
	}

}
