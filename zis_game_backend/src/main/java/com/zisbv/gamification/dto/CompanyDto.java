package com.zisbv.gamification.dto;

import java.time.LocalDate;
import java.util.Date;

import com.zisbv.gamification.entities.CompanyData;
import com.zisbv.gamification.repositories.CompanyRegistrationEndTimes;

public class CompanyDto {

	private String companyName;

	private String address;

	private String pincode;

	private String state;

	private String country;

	private String primaryContact;

	private String secondaryContact;

	private String startDate;

	private String duration;

	private String primaryEmail;

	private String secondaryEmail;

	

	public String getCompanyName() {
		return companyName;
	}



	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getPincode() {
		return pincode;
	}



	public void setPincode(String pincode) {
		this.pincode = pincode;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getPrimaryContact() {
		return primaryContact;
	}



	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}



	public String getSecondaryContact() {
		return secondaryContact;
	}



	public void setSecondaryContact(String secondaryContact) {
		this.secondaryContact = secondaryContact;
	}



	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getDuration() {
		return duration;
	}



	public void setDuration(String duration) {
		this.duration = duration;
	}



	public String getPrimaryEmail() {
		return primaryEmail;
	}



	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}



	public String getSecondaryEmail() {
		return secondaryEmail;
	}



	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}



	public CompanyData dataFromPojo() {
		
		String endDate = "";
		LocalDate date = LocalDate.parse(startDate);
		
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.ONEMONTH.toString())) {
			LocalDate newDate = date.plusMonths(1);
			endDate = newDate.toString();
		}
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.SIXMONTHS.toString())) {
			LocalDate newDate = date.plusMonths(6);
			endDate = newDate.toString();
		}
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.ONEYEAR.toString())) {
			LocalDate newDate = date.plusYears(1);
			endDate = newDate.toString();
		}
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.TWOYEARS.toString())) {
			LocalDate newDate = date.plusYears(2);
			endDate = newDate.toString();
		}
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.THREEYEARS.toString())) {
			LocalDate newDate = date.plusYears(3);
			endDate = newDate.toString();
		}
		if (duration.equalsIgnoreCase(CompanyRegistrationEndTimes.FIVEYEARS.toString())) {
			LocalDate newDate = date.plusYears(5);
			endDate = newDate.toString();
		}
		

		CompanyData company = new CompanyData();
		company.setCompanyName(companyName);
		company.setAddress(address);
		company.setPincode(pincode);
		company.setState(state);
		company.setCountry(country);
		company.setPrimaryContact(primaryContact);
		company.setSecondaryContact(secondaryContact);
		company.setStartDate(startDate);
		company.setEndDate(endDate);
		company.setDuration(duration);
		company.setPrimaryEmail(primaryEmail);
		company.setSecondaryEmail(secondaryEmail);
		company.setStatus(true);
		company.setLast_modified(new Date());

		return company;
	}

}
