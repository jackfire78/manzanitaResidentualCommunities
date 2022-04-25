package com.manzanita.spring.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database events table
@Document(collection = "CommunityEvents")
public class Events {
	@Id
	private String id;
	private String userId;
	
	@NotBlank
	@Size(max = 25)
	private String eventName;

	@NotBlank
	@Size(max = 300)
	private String eventDescription;

	@NotBlank
	@Size(max = 250)
	private String eventPicture;

	@NotBlank
	@Size(max = 5)
	private Integer eventPrice;

	@NotBlank
	private Date eventDate;

	@NotBlank
	@Size(max = 100)
	private String eventPresenters;

	public Events() {
	}

	public Events(String eventName, String eventDescription, String eventPicture, Integer eventPrice, Date eventDate,
			String eventPresenters) {
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventPicture = eventPicture;
		this.eventPrice = eventPrice;
		this.eventDate = eventDate;
		this.eventPresenters = eventPresenters;
	}
	//custom constructor for joining events
	public Events(String id, String userId, String eventName) {
		this.id = id;
		this.eventName = eventName;
		this.userId = userId;
	}

	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventDescription
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * @param eventDescription the eventDescription to set
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	/**
	 * @return the eventPicture
	 */
	public String getEventPicture() {
		return eventPicture;
	}

	/**
	 * @param eventPicture the eventPicture to set
	 */
	public void setEventPicture(String eventPicture) {
		this.eventPicture = eventPicture;
	}

	/**
	 * @return the eventPrice
	 */
	public Integer getEventPrice() {
		return eventPrice;
	}

	/**
	 * @param eventPrice the eventPrice to set
	 */
	public void setEventPrice(Integer eventPrice) {
		this.eventPrice = eventPrice;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the eventPresenters
	 */
	public String getEventPresenters() {
		return eventPresenters;
	}

	/**
	 * @param eventPresenters the eventPresenters to set
	 */
	public void setEventPresenters(String eventPresenters) {
		this.eventPresenters = eventPresenters;
	}

}
