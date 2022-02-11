package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.CompanyAssignedComponents;

@Repository("companyAssignedComponentsRepository")
public interface AdminAssignedComponentsRepository extends JpaRepository<CompanyAssignedComponents, Long> {

	@Query("SELECT a FROM CompanyAssignedComponents a WHERE a.id = ?1")
	public CompanyAssignedComponents getAdminAssignedComponentsWithID(Long id);
	
	@Query("SELECT a FROM CompanyAssignedComponents a WHERE a.companyID = ?1")
	public CompanyAssignedComponents getAdminAssignedComponentsWithCompanyID(Long companyID);
	
}
