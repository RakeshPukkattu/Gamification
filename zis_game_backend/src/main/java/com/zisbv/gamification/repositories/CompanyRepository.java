package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.CompanyData;

@Repository("companyRepository")
public interface CompanyRepository extends JpaRepository<CompanyData, Long> {

	@Query("SELECT c FROM CompanyData c WHERE c.id = ?1")
	public CompanyData getCompanyWithID(Long id);

	@Query("SELECT c FROM CompanyData c WHERE c.companyName = ?1")
	public CompanyData getCompanyWithName(String companyName);

}
