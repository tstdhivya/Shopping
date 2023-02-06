package com.app.shoppingzone.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.shoppingzone.config.WriteableRepository;
import com.app.shoppingzone.entity.User;
import com.app.shoppingzone.enumeration.Status;

public interface UserRepository extends WriteableRepository<User, UUID> {

	public Optional<User> findByPhoneNumber1AndStatus(String phoneNumber1, Status status);

	@Query(value = "SELECT * FROM shop_user u where u.email=:userEmail and u.user_type='USER'", nativeQuery = true)
	Optional<User> findByUserEmail(String userEmail);

	@Query(value = "SELECT * FROM shop_user u where u.user_name =:userName", nativeQuery = true)
	Optional<User> findByUserName(String userName);

	@Query(value = "SELECT * FROM shop_user u where u.phone_number_1=:phoneNumber1 and u.user_type='USER'", nativeQuery = true)
	Optional<User> getByPhoneNumber1(String phoneNumber1);

	@Query(value = "SELECT * FROM shop_user u where u.user_type=:admin", nativeQuery = true)
	Optional<User> findByUserRoleType(String admin);

	@Query(value = "SELECT * FROM  shop_user u where  u.password =:encryptedPassword and u.user_name =:userName", nativeQuery = true)
	Optional<User> findByPasswordAndIsDeletedFalseAndIsLockedFalse(String encryptedPassword, String userName);

	@Query(value = "SELECT * FROM shop_user u where u.user_name ==:userName and u.phone_no_1=::phoneNumber1 and u.user_type='USER'", nativeQuery = true)
	Optional<User> findByUserName1(String userName, String phoneNumber1);

	public Optional<User> findByOtp(String otp);

	public Optional<User> findByPhoneNumber1(String phoneNumber1);

}