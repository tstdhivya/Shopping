package com.app.shoppingzone.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.shoppingzone.dto.ErrorDto;
import com.app.shoppingzone.dto.LoginRequest;
import com.app.shoppingzone.dto.PaswordUpdateDto;
//import com.app.pets.dto.ErrorDto;
//import com.app.pets.dto.LoginRequest;
//import com.app.pets.dto.SellerPasswodUpdateDto;
//import com.app.pets.dto.ValidateOtpDTO;
//import com.app.pets.entity.Seller;
//import com.app.pets.enumeration.Status;
//import com.app.pets.util.PasswordUtil;
import com.app.shoppingzone.dto.UserDto;
import com.app.shoppingzone.dto.ValidateOtpDTO;
import com.app.shoppingzone.entity.PasswordUtils;
import com.app.shoppingzone.entity.User;
import com.app.shoppingzone.enumeration.RequestType;
import com.app.shoppingzone.enumeration.Status;
import com.app.shoppingzone.repository.UserRepository;
import com.app.shoppingzone.response.Response;
import com.app.shoppingzone.response.ResponseGenerator;
import com.app.shoppingzone.response.TransactionContext;
import com.app.shoppingzone.security.JwtTokenUtil;
import com.app.shoppingzone.service.MessagePropertyService;
import com.app.shoppingzone.service.UserService;
import com.app.shoppingzone.validation.UserValidation;
import com.app.shoppingzone.validation.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @AllArgsConstructor(onConstructor_ = { @Autowired })
	@RestController	
	@RequestMapping("/api/user")
	@Api(value = "user Rest API's", produces = "application/json", consumes = "application/json")
	public class UserController {
		private static final Logger logger = Logger.getLogger(UserController.class);
		// DHIVYA
		private @NonNull ResponseGenerator responseGenerator;

		private @NonNull MessagePropertyService messagePropertySource;

		private @NonNull UserValidation userValidation;

		private @NonNull UserRepository userRepository;

		private @NonNull MessageSource messageSource;

		private @NonNull JwtTokenUtil jwtTokenUtil;

		private @NonNull UserService userService;

		final Long expiryInterval = 5L * 60 * 1000;

		@ApiOperation(value = "Allows to create new user.", response = Response.class)
		@PostMapping(value = "/create", produces = "application/json")
		public ResponseEntity<?> create(@ApiParam(value = "The user request payload") @RequestBody UserDto request,
				@RequestHeader HttpHeaders httpHeader) throws Exception {
			TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
			try {
				ValidationResult validationResult = userValidation.validate(RequestType.POST, request);
				userService.saveOrUpdate((User) validationResult.getObject());

				return responseGenerator.successResponse(context, "user.create", HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}

		}

		@ApiOperation(value = "Logs the seller in to the system and return the auth tokens")
		@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
		public ResponseEntity<?> login(@ApiParam(value = "The LoginRequest payload") @RequestBody LoginRequest request,
				@RequestHeader HttpHeaders httpHeader) throws Exception {
			ErrorDto errorDto = null;
			Map<String, Object> response = new HashMap<String, Object>();
			if (null == request) {
				errorDto = new ErrorDto();
				errorDto.setCode("400");
				errorDto.setMessage("Invalid request payload.!");
				response.put("status", 0);
				response.put("error", errorDto);
				return ResponseEntity.badRequest().body(response);
			}

			Optional<User> userOptional =userRepository.findByUserName(request.getUserName());
			if (!userOptional.isPresent()) {
				errorDto = new ErrorDto();
				errorDto.setCode("400");
				errorDto.setMessage("Invalid username1.!");
				response.put("status", 0);
				response.put("error", errorDto);
				return ResponseEntity.badRequest().body(response);
			}
			User seller = userOptional.get();
			String enPassword = PasswordUtils.getEncryptedPassword(request.getPassword());

			if (!seller.getUserName().equals(request.getUserName())) {
				errorDto = new ErrorDto();
				errorDto.setCode("400");
				errorDto.setMessage("Invalid username2.!");
				response.put("status", 0);
				response.put("error", errorDto);
				return ResponseEntity.badRequest().body(response);
			}
			if (!seller.getPassword().equals(enPassword)) {
				errorDto = new ErrorDto();
				errorDto.setCode("400");
				errorDto.setMessage("Password is wrong.!");
				response.put("status", 0);
				response.put("error", errorDto);
				return ResponseEntity.badRequest().body(response);
			}

			final String token = jwtTokenUtil.generateToken(seller);
			response.put("status", 1);
			response.put("message", "Logged in successfully.!");
			response.put("jwt", token);
			TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
			try {
				return responseGenerator.successResponse(context, response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}

		}
		@ApiOperation(value = "Allows to change current password seller", response = Response.class)
		@PutMapping(value = "/change/password", produces = "application/json")
		public ResponseEntity<?> update(
				@ApiParam(value = "Payload for change current password seller") @RequestBody PaswordUpdateDto request,
				@RequestHeader HttpHeaders httpHeaders) throws Exception {
			TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
			Optional<User> obj = userService.findByUserName(request.userName);
			String userName = obj.get().getUserName();

			Optional<User> sOptional = userService.findByPasswordAndIsDeletedFalseAndIsLockedFalse(
					PasswordUtils.getEncryptedPassword(request.getOldPassword()), userName);

			if (!sOptional.isPresent()) {
				return responseGenerator.errorResponse(context, "invalid.seller.password", HttpStatus.BAD_REQUEST);
			}
			User seller = sOptional.get();

			if (!request.getNewPassword().equals(request.getConfirmPassword())) {
				return responseGenerator.errorResponse(context, "invalid.new.and.confirm.password", HttpStatus.BAD_REQUEST);
			}

			if (request.getConfirmPassword().equals(request.getOldPassword())) {
				return responseGenerator.errorResponse(context, "invalid.old.and.new.password", HttpStatus.BAD_REQUEST);
			}

			seller.setPassword(PasswordUtils.getEncryptedPassword(request.getNewPassword()));
			seller.setForcePasswordChange(false);
			seller.setModifiedOn(new Date());
			seller.setPassword(PasswordUtils.getEncryptedPassword(request.getNewPassword()));
			userService.saveOrUpdate(seller);
			try {
				return responseGenerator.successResponse(context, "password.update", HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}

		@ApiOperation(value = "Allows to validate customer using one time passsword")
		@RequestMapping(value = "/validate/otp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
		public ResponseEntity<?> validateOTP(
				@ApiParam(value = "The Signup request payload") @RequestBody ValidateOtpDTO request,
				@RequestHeader HttpHeaders httpHeader) throws Exception {

			Optional<User> userOptional = userRepository.findByOtp(request.getOtp());
			System.out.print(request.getOtp());

			// Optional<Seller> userOptional = sellerRepository.findLoggedInUser();
			User user = userOptional.get();
			ValidationResult validationResult = userValidation.validateOtp(RequestType.POST, request, user);
			User otp = (User) validationResult.getObject();
			userService.validateSeller(user, otp);

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("status", 1);
			response.put("message", "validation.otp.verified");
			TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
			try {
				return responseGenerator.successResponse(context, "OTP verified successfully.", HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}

		@ApiOperation(value = "Allows to resend one time password")
		@RequestMapping(value = "/resend/otp", method = RequestMethod.GET)
		public ResponseEntity<?> resendOtp(@ApiParam(value = "userId") @RequestParam("userId") UUID userId,
				@ApiParam @RequestHeader HttpHeaders httpHeader) throws Exception {

			Optional<User> obj = userService.findByUserId(userId);

			String userName = obj.get().getUserName();

			Optional<User> userOptional = userRepository.findByUserName(userName);
			User user = userOptional.get();

			Optional<User> otpObj = userRepository.findByPhoneNumber1AndStatus(user.getPhoneNumber1(), Status.ACTIVE);

			String otps = new DecimalFormat("000000").format(new Random().nextInt(999999));

			Date date = new Date(System.currentTimeMillis() + expiryInterval);
			System.out.println(otps);
			user.setOtp(otps);
			user.setExpiryDate(date);
			user.setIsOtpVerified(true);
			userRepository.saveAndFlush(user);

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("message", "OTP resend successfully.");
			response.put("otp", otps);
			TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
			try {
				return responseGenerator.successResponse(context, response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}

		}
}