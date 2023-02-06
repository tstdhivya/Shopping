package com.app.shoppingzone.validation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shoppingzone.advice.ObjectInvalidException;
import com.app.shoppingzone.dto.UserDto;
import com.app.shoppingzone.entity.PasswordUtils;
import com.app.shoppingzone.entity.User;
import com.app.shoppingzone.enumeration.RequestType;
import com.app.shoppingzone.repository.UserRepository;
import com.app.shoppingzone.service.MessagePropertyService;
import com.app.shoppingzone.service.UserService;
import com.app.shoppingzone.util.ValidationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UserValidation {

	@Autowired
	private MessagePropertyService messageSource;

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	public ValidationResult validate(RequestType requestType, UserDto request) {

		final Long expiryInterval = 5L * 60 * 1000;
		String otps = new DecimalFormat("000000").format(new Random().nextInt(999999));

		Date expireDate = new Date(System.currentTimeMillis() + expiryInterval);
		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		User user = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException("invalid.request.payload");
			}
			if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
				errors.add("full.name.required");
			} else {
				request.setFullName(ValidationUtil.getFormattedString(request.getFullName()));
				if (!ValidationUtil.isValidName(request.getFullName())) {
					errors.add("full.name.invalid");
				}
			}
			Optional<User> userOptional = userService.getByPhoneNumber1(request.getPhoneNumber1());
			if (userOptional.isPresent()) {
				String[] params = new String[] { request.getPhoneNumber1() };
				errors.add("user.phone1.duplicate");
			}
			Optional<User> userDuplicateObj = userService.findByUserName(request.getUserName());
			if (userDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getUserName() };
				errors.add("user.name.duplicate");
			}
			Optional<User> userDuplicateMailObj = userService.findByUserEmail(request.getEmail());
			if (userDuplicateMailObj.isPresent()) {
				errors.add("user.email.duplicate");
			}
		} else {
			if (ValidationUtil.isNull(request.getId()))
				throw new ObjectInvalidException("invalid.request.payload");

			Optional<User> userOptional = userService.findByUserId(request.getId());
			if (!userOptional.isPresent()) {
				throw new ObjectInvalidException("user.not.found");
			}
			user = userOptional.get();
		}
		if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
			errors.add("full.name.required");
		} else {
			request.setFullName(ValidationUtil.getFormattedString(request.getFullName()));
			if (!ValidationUtil.isValidName(request.getFullName())) {
				errors.add("full.name.invalid");
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
			errors.add("user.email.required");
		} else {
			request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
			if (!ValidationUtil.isValidEmailId(request.getEmail())) {
				errors.add("user.email.invalid");
			}
		}
		if (ValidationUtil.isNullObject(request.getUserRole())) {
			errors.add("user.role.type.required");
		}
		if (ValidationUtil.isValidPhoneNo(request.getPhoneNumber1())) {
			if (ValidationUtil.isNullOrEmpty(request.getPhoneNumber1())) {
				errors.add("user.phone.no1.required");
			}
			errors.add("user.phone.number1.invalid");
		} else {
			request.setPhoneNumber1(ValidationUtil.getFormattedString(request.getPhoneNumber1()));
			if (!ValidationUtil.isValidMobileNumber(request.getPhoneNumber1())) {
				errors.add("user.phone1.invalid");
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getPassword())) {
			errors.add("password.required");
		}
		if (ValidationUtil.isNullOrEmpty(request.getWatsapNumber())) {
			errors.add("country.id.required");
		}

		if (ValidationUtil.isNull(request.getCountryId())) {
			errors.add("country.id.required");
		}
		if (ValidationUtil.isNull(request.getCityId())) {
			errors.add("city.id.required");
		}
		if (ValidationUtil.isNull(request.getStateId())) {
			errors.add("state.id.required");
		}
		if (ValidationUtil.isNullOrEmpty(request.getAddressLine1())) {
			errors.add("address.1.required");
		}
		if (ValidationUtil.isNullOrEmpty(request.getAddressLine2())) {
			errors.add("address.2.required");

		}
		if (ValidationUtil.isNullOrEmpty(request.getCountryCode())) {
			errors.add("address.3.required");

		}
		String encrptPassword = PasswordUtils.getEncryptedPassword(request.getPassword());
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == user) {
			String admin = "ADMIN";
			Optional<User> userOption = userService.findByUserRoleType(admin);

//			Optional<Role> role = null;
//
//			role = roleRepository.findByRoleName(request.getUserRole().toString());
			user = User.builder().fullName(request.getFullName()).userName(request.getUserName())
					.phoneNumber1(request.getPhoneNumber1()).email(request.getEmail())
					.watsapNumber(request.getWatsapNumber()).password(encrptPassword).userRole(request.getUserRole())
					.addressLine1(request.getAddressLine1()).addressLine2(request.getAddressLine2())
					.cityId(request.getCityId()).stateId(request.getStateId()).countryId(request.getCountryId())
					.countryCode(request.getCountryCode()).expiryDate(expireDate).otp(otps).build();
		} else {
			user.setFullName(request.getFullName());
			user.setUserName(request.getUserName());
			user.setPhoneNumber1(request.getPhoneNumber1());
			user.setEmail(request.getEmail());
			user.setPassword(encrptPassword);
			user.setWatsapNumber(request.getWatsapNumber());
			user.setUserRole(request.getUserRole());
			user.setAddressLine1(request.getAddressLine1());
			user.setAddressLine2(request.getAddressLine2());
			user.setCityId(request.getCityId());
			user.setStateId(request.getStateId());
			user.setCountryId(request.getCountryId());
			user.setCountryCode(request.getCountryCode());
			user.setStatus(request.getStatus());
			user.setExpiryDate(expireDate);
			user.setOtp(otps);

		}
		result.setObject(user);

		return result;

	}

	public ValidationResult validateOtp(RequestType requestType, com.app.shoppingzone.dto.ValidateOtpDTO request,
			User user) {
		errors = new ArrayList<>();

		if (ValidationUtil.isNullOrEmpty(request.getPhoneNumber1())) {
			errors.add(messageSource.getMessage("signup.mobile.required"));
		} else {
			request.setPhoneNumber1(ValidationUtil.getFormattedString(request.getPhoneNumber1()));
			if (!ValidationUtil.isValidMobileNumber(request.getPhoneNumber1())) {
				errors.add(messageSource.getMessage("signup.mobile.invalid"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getOtp())) {
			errors.add(messageSource.getMessage("validation.otp.required"));
		} else {
			request.setOtp(ValidationUtil.getFormattedString(request.getOtp()));
		}

		Optional<User> otpObj = userRepository.findByPhoneNumber1AndStatus(request.getPhoneNumber1(),
				com.app.shoppingzone.enumeration.Status.ACTIVE);

		if (!otpObj.isPresent()) {
			errors.add(messageSource.getMessage("validation.otp.invalid"));
		}

		if (!otpObj.get().getOtp().equals(request.getOtp())) {
			errors.add(messageSource.getMessage("validation.otp.wrong"));
		}
		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		result.setObject(otpObj.get());
		return result;
	}

}
