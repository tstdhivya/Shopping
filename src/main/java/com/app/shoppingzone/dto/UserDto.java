package com.app.shoppingzone.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.app.shoppingzone.enumeration.RoleType;
import com.app.shoppingzone.enumeration.Status;

import lombok.Data;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;

	private String fullName;

	private String userName;

	private String email;

	private String phoneNumber1;

	private String watsapNumber;

	private String password;

	private RoleType userRole;

	private Status status;

	private String addressLine1;

	private String addressLine2;

	private UUID countryId;

	private UUID stateId;

	private UUID cityId;

	private String countryCode;

	private Date expiryDate;

	private String otp;

}
