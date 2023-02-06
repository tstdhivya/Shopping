package com.app.shoppingzone.dto;

import lombok.Getter;
import lombok.Setter;

	@Getter
	@Setter
	public class PaswordUpdateDto {

		public String userName;

		private String oldPassword;

		private String newPassword;

		public String confirmPassword;

	}


