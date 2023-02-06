package com.app.shoppingzone.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.shoppingzone.config.WriteableRepository;
import com.app.shoppingzone.entity.City;

public interface CityRepository extends WriteableRepository<City, UUID> {

	@Query(value = "SELECT C From City C WHERE C.stateId =:stateId")
	List<City> findByStateId(UUID stateId);

}
