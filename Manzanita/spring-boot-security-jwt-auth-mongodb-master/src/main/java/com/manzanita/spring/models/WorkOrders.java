package com.manzanita.spring.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database work orders table
@Document(collection = "MaintenanceWorkOrders")
public class WorkOrders {
	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	private String residentName;

	@NotBlank
	@Size(min = 1, max = 5)
	private String residentUnit;

	@NotBlank
	@Size(min = 10, max = 10)
	private String residentPhoneNum;

	@NotBlank
	@Size(max = 250)
	private String issue;

	@NotBlank
	@Size(max = 15)
	private String severity;

	@NotBlank
	@Size(max = 20)
	private String homeEnterance;

	@NotBlank
	@Size(max = 20)
	private String status;

	public WorkOrders() {
	}

	public WorkOrders(String residentName, String residentUnit, String residentPhoneNum, String issue,
			String severity, String homeEnterance, String status) {
		this.residentName = residentName;
		this.residentUnit = residentUnit;
		this.residentPhoneNum = residentPhoneNum;
		this.issue = issue;
		this.severity = severity;
		this.homeEnterance = homeEnterance;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	/**
	 * All getter and setters for each parameter
	 *
	 * @return the residentName
	 */
	public String getResidentName() {
		return residentName;
	}

	/**
	 * @param residentName the residentName to set
	 */
	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	/**
	 * @return the residentUnit
	 */
	public String getResidentUnit() {
		return residentUnit;
	}

	/**
	 * @param residentUnit the residentUnit to set
	 */
	public void setResidentUnit(String residentUnit) {
		this.residentUnit = residentUnit;
	}

	/**
	 * @return the residentPhoneNum
	 */
	public String getResidentPhoneNum() {
		return residentPhoneNum;
	}

	/**
	 * @param residentPhoneNum the residentPhoneNum to set
	 */
	public void setResidentPhoneNum(String residentPhoneNum) {
		this.residentPhoneNum = residentPhoneNum;
	}

	/**
	 * @return the issue
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * @param issue the issue to set
	 */
	public void setIssue(String issue) {
		this.issue = issue;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @return the homeEnterance
	 */
	public String getHomeEnterance() {
		return homeEnterance;
	}

	/**
	 * @param homeEnterance the homeEnterance to set
	 */
	public void setHomeEnterance(String homeEnterance) {
		this.homeEnterance = homeEnterance;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
