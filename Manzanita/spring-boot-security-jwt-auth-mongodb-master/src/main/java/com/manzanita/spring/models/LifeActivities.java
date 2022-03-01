package com.manzanita.spring.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database activities table
@Document(collection = "CommunityLifeActivites")
public class LifeActivities {
	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	private String activityName;

	@NotBlank
	private Date activityDate;

	@NotBlank
	@Size(max = 250)
	private String activityDescription;

	@NotBlank
	@Size(max = 20)
	private Integer activityPrice;

	public LifeActivities() {
	}

	public LifeActivities(String activityName, Date activityDate, String activityDescription, Integer activityPrice) {
		this.activityName = activityName;
		this.activityDate = activityDate;
		this.activityDescription = activityDescription;
		this.activityPrice = activityPrice;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return the activityDate
	 */
	public Date getActivityDate() {
		return activityDate;
	}

	/**
	 * @param activityDate the activityDate to set
	 */
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	/**
	 * @return the activityDescription
	 */
	public String getActivityDescription() {
		return activityDescription;
	}

	/**
	 * @param activityDescription the activityDescription to set
	 */
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	/**
	 * @return the activityPrice
	 */
	public Integer getActivityPrice() {
		return activityPrice;
	}

	/**
	 * @param activityPrice the activityPrice to set
	 */
	public void setActivityPrice(Integer activityPrice) {
		this.activityPrice = activityPrice;
	}

}
