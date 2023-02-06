package com.app.shoppingzone.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.shoppingzone.enumeration.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "shop_user")
public class User extends RecordModifier implements Serializable {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "password")
	public String password;

	@Column(name = "email")
	public String email;

	@Column(name = "phone_number_1")
	private String phoneNumber1;

	@Column(name = "watsap_number")
	private String watsapNumber;

	@Column(name = "address_line_1")
	private String addressLine1;

	@Column(name = "address_line_2")
	private String addressLine2;

	@Type(type = "uuid-char")
	@Column(name = "country_id")
	private UUID countryId;

	@Type(type = "uuid-char")
	@Column(name = "state_id")
	private UUID stateId;

	@Type(type = "uuid-char")
	@Column(name = "city_id")
	private UUID cityId;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	private RoleType userRole;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "country_code")
	private String countryCode;

	public void setAndEncryptPassword(String password) {
		setPassword(PasswordUtils.getEncryptedPassword(password));
	}

	@Column(name = "force_password_change")
	private Boolean forcePasswordChange;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "is_locked")
	private Boolean isLocked;

	@Column(name = "otp_verified")
	private Boolean isOtpVerified;

	@Column(name = "otp")
	private String otp;

}
