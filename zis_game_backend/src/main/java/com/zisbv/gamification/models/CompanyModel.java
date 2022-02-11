package com.zisbv.gamification.models;

public class CompanyModel {

	private Long id;

	private String companyName;

	private String address;

	private String pincode;

	private String state;

	private String country;

	private String primaryContact;

	private String secondaryContact;

	private String startDate;

	private String endDate;

	private String duration;

	private String primaryEmail;

	private String secondaryEmail;

	private Boolean status;

	private AssignedComponentsModel assignedComponents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public AssignedComponentsModel getAssignedComponents() {
		return assignedComponents;
	}

	public void setAssignedComponents(AssignedComponentsModel assignedComponents) {
		this.assignedComponents = assignedComponents;
	}

	@Override
	public String toString() {
		return "CompanyModel [id=" + id + ", companyName=" + companyName + ", address=" + address + ", pincode="
				+ pincode + ", state=" + state + ", country=" + country + ", primaryContact=" + primaryContact
				+ ", secondaryContact=" + secondaryContact + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", duration=" + duration + ", primaryEmail=" + primaryEmail + ", secondaryEmail=" + secondaryEmail
				+ ", status=" + status + ", assignedComponents=" + assignedComponents + "]";
	}

}
