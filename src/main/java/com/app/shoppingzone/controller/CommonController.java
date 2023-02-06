package com.app.shoppingzone.controller;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shoppingzone.entity.City;
import com.app.shoppingzone.entity.Country;
import com.app.shoppingzone.entity.State;
import com.app.shoppingzone.response.Response;
import com.app.shoppingzone.response.ResponseGenerator;
import com.app.shoppingzone.response.TransactionContext;
import com.app.shoppingzone.service.CityService;
import com.app.shoppingzone.service.CountryService;
import com.app.shoppingzone.service.MessagePropertyService;
import com.app.shoppingzone.service.StateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/commen")
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@Api(value = "Address Rest API", produces = "application/json", consumes = "application/json")
public class CommonController {
	// dhivya
	private static final Logger logger = Logger.getLogger(CommonController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull MessagePropertyService messagePropertyService;

	private @NonNull StateService stateService;

	private @NonNull CityService cityService;

	private @NonNull CountryService countryService;

	@ApiOperation(value = "Allows to fetch address.", response = Response.class)
	@GetMapping(value = "/getAllCity", produces = "application/json")
	public ResponseEntity<?> getAllCity(@RequestHeader HttpHeaders httpHeader) throws Exception {

		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			List<City> management = cityService.findAllCity();

			return responseGenerator.successGetResponse(context, "city.get", management, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@ApiOperation(value = "Allows to fetch address.", response = Response.class)
	@GetMapping(value = "/getAllState", produces = "application/json")
	public ResponseEntity<?> getAllState(@RequestHeader HttpHeaders httpHeader) throws Exception {

		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			List<State> management = stateService.findAllState();

			return responseGenerator.successGetResponse(context, "state.get", management, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch address.", response = Response.class)
	@GetMapping(value = "/getAllCountry", produces = "application/json")
	public ResponseEntity<?> getAllCountry(@RequestHeader HttpHeaders httpHeader) throws Exception {

		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			List<Country> management = countryService.findAllCountry();

			return responseGenerator.successGetResponse(context, "country.get", management, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch particular state.", response = Response.class)
	@GetMapping(value = "getCity/{stateId}", produces = "application/json")
	public ResponseEntity<?> getByCityid(@PathVariable("stateId") UUID stateId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<City> mOptional = cityService.findByStateId(stateId);
			return responseGenerator.successGetResponse(context, "get.by.state.fetched.Successfully", mOptional,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to fetch particular country.", response = Response.class)
	@GetMapping(value = "getState/{countryId}", produces = "application/json")
	public ResponseEntity<?> getByCountryid(@PathVariable("countryId") UUID countryId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<State> mOptional = stateService.findByCountryId(countryId);
			return responseGenerator.successGetResponse(context, "City.fetched.Successfully", mOptional, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}
}
