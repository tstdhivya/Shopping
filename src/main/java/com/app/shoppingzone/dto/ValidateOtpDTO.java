package com.app.shoppingzone.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateOtpDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID userId;
	private String otp;
	private String phoneNumber1;

}
