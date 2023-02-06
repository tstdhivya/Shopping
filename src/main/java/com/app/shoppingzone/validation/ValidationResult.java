package com.app.shoppingzone.validation;

import java.util.List;

import lombok.Data;

@Data
public class ValidationResult {

	private boolean isValid = true;
	private List<String> errors;
	private Object object;
}
