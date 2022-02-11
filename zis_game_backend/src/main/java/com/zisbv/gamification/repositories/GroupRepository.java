package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.GroupData;

@Repository("groupRepository")
public interface GroupRepository extends JpaRepository<GroupData, Integer> {

	@Query("SELECT g FROM GroupData g WHERE g.id = ?1")
	public GroupData getGroupWithID(Long id);

	@Query("SELECT g FROM GroupData g WHERE g.groupName = ?1")
	public GroupData getGroupWithName(String groupName);

}
