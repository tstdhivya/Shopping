package com.app.shoppingzone.service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.shoppingzone.entity.User;
import com.app.shoppingzone.enumeration.Status;
import com.app.shoppingzone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })

public class UserService {

	private @NonNull UserRepository userRepository;

	public void saveOrUpdate(User userObj) {

		final Long expiryInterval = 5L * 60 * 1000;

		String otps = new DecimalFormat("000000").format(new Random().nextInt(999999));
		System.out.println(otps);

		Date expireDate = new Date(System.currentTimeMillis() + expiryInterval);
		System.out.println(expireDate);
		userObj.setExpiryDate(expireDate);
		User user = new User();
		user.setOtp(otps);
		user.setExpiryDate(expireDate);
		user.setUserName(userObj.getUserName());
		user.setFullName(userObj.getFullName());
		user.setPassword(userObj.getPassword());
		user.setEmail(userObj.getEmail());
		user.setUserRole(userObj.getUserRole());
		user.setPhoneNumber1(userObj.getPhoneNumber1());
		user.setWatsapNumber(userObj.getWatsapNumber());
		user.setAddressLine1(userObj.getAddressLine1());
		user.setAddressLine2(userObj.getAddressLine2());
		user.setCityId(userObj.getCityId());
		user.setCountryId(userObj.getCountryId());
		user.setStateId(userObj.getStateId());
		user.setCountryCode(userObj.getCountryCode());
		userRepository.saveAndFlush(user);

	}

	public Optional<User> findByUserEmail(String userEmail) {
		return userRepository.findByUserEmail(userEmail);
	}

	public Optional<User> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public Optional<User> getByPhoneNumber1(String phoneNumber1) {
		return userRepository.getByPhoneNumber1(phoneNumber1);
	}

	public Optional<User> findByUserRoleType(String admin) {
		return userRepository.findByUserRoleType(admin);
	}

	@Transactional
	public void validateSeller(User user, User otp) {

		otp.setStatus(Status.INACTIVE);
		userRepository.saveAndFlush(otp);

		user.setIsOtpVerified(true);
		userRepository.saveAndFlush(user);
		return;
	}

	public Optional<User> findByUserId(UUID sellerId) {
		return userRepository.findById(sellerId);
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		User user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public Optional<User> findByPasswordAndIsDeletedFalseAndIsLockedFalse(String encryptedPassword, String userName) {
		return userRepository.findByPasswordAndIsDeletedFalseAndIsLockedFalse(encryptedPassword, userName);
	}

	void validateCustomer(User user, User otp) {

	}

	public Optional<User> findByPhoneNumber1(String phoneNumber1) {
		// TODO Auto-generated method stub
		return userRepository.findByPhoneNumber1(phoneNumber1);
	}

}