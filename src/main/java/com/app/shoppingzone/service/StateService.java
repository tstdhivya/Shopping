package com.app.shoppingzone.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shoppingzone.entity.State;
import com.app.shoppingzone.repository.StateRepository;

@Service
public class StateService {
	@Autowired
	private StateRepository stateRepository;

	public List<State> findAllState() {

		return stateRepository.findAll();
	}

	public List<State> findByCountryId(UUID countryId) {
		return stateRepository.findByCountryId(countryId);
	}

}
