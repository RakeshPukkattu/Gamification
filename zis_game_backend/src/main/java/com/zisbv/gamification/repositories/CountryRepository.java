package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Country;

@Repository("countryRepository")
public interface CountryRepository extends JpaRepository<Country, Long> {

	@Query("SELECT c FROM Country c WHERE c.id = ?1")
	public Country getCountryWithID(Long id);

	@Query("SELECT c FROM Country c WHERE c.countryName = ?1")
	public Country getCountryWithName(String countryName);
}
