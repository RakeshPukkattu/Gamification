package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Region;

@Repository("regionRepository")
public interface RegionRepository extends JpaRepository<Region, Long> {

	@Query("SELECT r FROM Region r WHERE r.id = ?1")
	public Region getRegionWithID(Long id);

	@Query("SELECT r FROM Region r WHERE r.regionName = ?1")
	public Region getRegionWithName(String regionName);
}
