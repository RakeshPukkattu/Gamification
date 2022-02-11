package com.zisbv.gamification.handlers;

import java.time.LocalDate;
import java.util.Date;

import com.zisbv.gamification.dto.CompanyDto;
import com.zisbv.gamification.entities.CompanyData;
import com.zisbv.gamification.repositories.CompanyRegistrationEndTimes;

public class CompanyManagementHandler {

	public CompanyData updateStatus(CompanyData existingCompany, Boolean status) {

		CompanyData company = new CompanyData();

		company.setId(existingCompany.getId());
		company.setCompanyName(existingCompany.getCompanyName());
		company.setAddress(existingCompany.getAddress());
		company.setPincode(existingCompany.getPincode());
		company.setState(existingCompany.getState());
		company.setCountry(existingCompany.getCountry());
		company.setPrimaryContact(existingCompany.getPrimaryContact());
		company.setSecondaryContact(existingCompany.getSecondaryContact());
		company.setStartDate(existingCompany.getStartDate());
		company.setDuration(existingCompany.getDuration());
		company.setEndDate(existingCompany.getEndDate());
		company.setPrimaryEmail(existingCompany.getPrimaryEmail());
		company.setSecondaryEmail(existingCompany.getSecondaryEmail());
		company.setStatus(status);
		company.setLast_modified(new Date());

		return company;
	}

	public CompanyData modifyCompany(CompanyDto dataFromUser, CompanyData existingCompany) {

		// new changes for response
		CompanyData newCompany = new CompanyData();
		newCompany.setId(existingCompany.getId());

		// Name
		if (dataFromUser.getCompanyName() != null) {
			newCompany.setCompanyName(dataFromUser.getCompanyName());
		} else {
			newCompany.setCompanyName(existingCompany.getCompanyName());
		}

		// Address
		if (dataFromUser.getAddress() != null) {
			newCompany.setAddress(dataFromUser.getAddress());
		} else {
			newCompany.setAddress(existingCompany.getAddress());
		}

		// pincode
		if (dataFromUser.getPincode() != null) {
			newCompany.setPincode(dataFromUser.getPincode());
		} else {
			newCompany.setPincode(existingCompany.getPincode());
		}

		// state
		if (dataFromUser.getState() != null) {
			newCompany.setState(dataFromUser.getState());
		} else {
			newCompany.setState(existingCompany.getState());
		}

		// country
		if (dataFromUser.getCountry() != null) {
			newCompany.setCountry(dataFromUser.getCountry());
		} else {
			newCompany.setCountry(existingCompany.getCountry());
		}

		// primary contact
		if (dataFromUser.getPrimaryContact() != null) {
			newCompany.setPrimaryContact(dataFromUser.getPrimaryContact());
		} else {
			newCompany.setPrimaryContact(existingCompany.getPrimaryContact());
		}

		// secondary contact
		if (dataFromUser.getSecondaryContact() != null) {
			newCompany.setSecondaryContact(dataFromUser.getSecondaryContact());
		} else {
			newCompany.setSecondaryContact(existingCompany.getSecondaryContact());
		}

		// start date
		if (dataFromUser.getStartDate() != null) {
			newCompany.setStartDate(dataFromUser.getStartDate());
		} else {
			newCompany.setStartDate(existingCompany.getStartDate());
		}

		//duration
		if (dataFromUser.getDuration() != null) {
			newCompany.setDuration(dataFromUser.getDuration());
		} else {
			newCompany.setDuration(existingCompany.getDuration());
		}
		
		// end date
		if (dataFromUser.getDuration() != null) {

			LocalDate date = LocalDate.parse(dataFromUser.getStartDate());
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.ONEMONTH.toString())) {
				LocalDate newDate = date.plusMonths(1);
				newCompany.setEndDate(newDate.toString());
			}
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.SIXMONTHS.toString())) {
				LocalDate newDate = date.plusMonths(6);
				newCompany.setEndDate(newDate.toString());
			}
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.ONEYEAR.toString())) {
				LocalDate newDate = date.plusYears(1);
				newCompany.setEndDate(newDate.toString());
			}
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.TWOYEARS.toString())) {
				LocalDate newDate = date.plusYears(2);
				newCompany.setEndDate(newDate.toString());
			}
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.THREEYEARS.toString())) {
				LocalDate newDate = date.plusYears(3);
				newCompany.setEndDate(newDate.toString());
			}
			if (dataFromUser.getDuration().equalsIgnoreCase(CompanyRegistrationEndTimes.FIVEYEARS.toString())) {
				LocalDate newDate = date.plusYears(5);
				newCompany.setEndDate(newDate.toString());
			}

		} else {
			newCompany.setEndDate(existingCompany.getEndDate());
		}

		// primary email
		if (dataFromUser.getPrimaryEmail() != null) {
			newCompany.setPrimaryEmail(dataFromUser.getPrimaryEmail());
		} else {
			newCompany.setPrimaryEmail(existingCompany.getPrimaryEmail());
		}

		// Secondary email
		if (dataFromUser.getSecondaryEmail() != null) {
			newCompany.setSecondaryEmail(dataFromUser.getSecondaryEmail());
		} else {
			newCompany.setSecondaryEmail(existingCompany.getSecondaryEmail());
		}

		newCompany.setStatus(existingCompany.getStatus());
		newCompany.setLast_modified(new Date());
		return newCompany;
	}
}
