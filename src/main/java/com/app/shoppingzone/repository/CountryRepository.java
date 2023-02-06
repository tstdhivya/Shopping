package com.app.shoppingzone.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.app.shoppingzone.config.WriteableRepository;
import com.app.shoppingzone.entity.Country;

@Repository
public interface CountryRepository extends WriteableRepository<Country, UUID> {

}
