package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.Language;

@Repository("languageRepository")
public interface LanguageRepository extends JpaRepository<Language, Long> {

	@Query("SELECT l FROM Language l WHERE l.id = ?1")
	public Language getLanguageWithID(Long id);

	@Query("SELECT l FROM Language l WHERE l.languageName = ?1")
	public Language getLanguageWithName(String languageName);
}
