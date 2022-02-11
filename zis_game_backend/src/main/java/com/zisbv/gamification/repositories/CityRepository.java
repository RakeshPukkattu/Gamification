package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.City;

@Repository("cityRepository")
public interface CityRepository extends JpaRepository<City, Long> {

	@Query("SELECT c FROM City c WHERE c.id = ?1")
	public City getCityWithID(Long id);

	@Query("SELECT c FROM City c WHERE c.cityName = ?1")
	public City getCityWithName(String cityName);
}
