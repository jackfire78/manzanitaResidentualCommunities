package com.manzanita.spring.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database users table
@Document(collection = "CommunityRestaurants")
public class LifeRestaurants {
	@Id
	private String id;

	@NotBlank
	@Size(max = 25)
	private String restaurantName;

	@NotBlank
	@Size(max = 200)
	private String datesOpened;

	public LifeRestaurants() {
	}

	public LifeRestaurants(String restaurantName, String datesOpened) {
		this.restaurantName = restaurantName;
		this.datesOpened = datesOpened;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return the restaurantName
	 */
	public String getRestaurantName() {
		return restaurantName;
	}

	/**
	 * @param restaurantName the restaurantName to set
	 */
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	/**
	 * @return the datesOpened
	 */
	public String getDatesOpened() {
		return datesOpened;
	}

	/**
	 * @param datesOpened the datesOpened to set
	 */
	public void setDatesOpened(String datesOpened) {
		this.datesOpened = datesOpened;
	}

}
