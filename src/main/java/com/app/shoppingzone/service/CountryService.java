package com.app.shoppingzone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.shoppingzone.entity.Country;
import com.app.shoppingzone.repository.CountryRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	public List<Country> findAllCountry() {
		return countryRepository.findAll();
	}

}
