package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.State;

@Repository("stateRepository")
public interface StateRepository extends JpaRepository<State, Long> {

	@Query("SELECT s FROM State s WHERE s.id = ?1")
	public State getStateWithID(Long id);

	@Query("SELECT s FROM State s WHERE s.stateName = ?1")
	public State getStateWithName(String stateName);

}
