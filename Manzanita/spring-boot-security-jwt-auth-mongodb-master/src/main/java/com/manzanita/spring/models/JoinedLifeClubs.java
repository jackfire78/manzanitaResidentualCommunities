package com.manzanita.spring.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database life clubs table
@Document(collection = "ClubMembers")
public class JoinedLifeClubs {
	@Id
	private String id;
	private String userId;

	@NotBlank
	@Size(max = 25)
	private String clubName;

	@NotBlank
	@Size(max = 250)
	private String clubDescription;

	public JoinedLifeClubs() {
	}

	public JoinedLifeClubs(String clubName, String clubDescription) {
		super();
		this.clubName = clubName;
		this.clubDescription = clubDescription;
	}
	
	//custom constructor for joining a club
	public JoinedLifeClubs(String id, String userId, String clubName) {
		this.id = id;
		this.clubName = clubName;
		this.userId = userId;
	}

	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the clubName
	 */
	public String getClubName() {
		return clubName;
	}

	/**
	 * @param clubName the clubName to set
	 */
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	/**
	 * @return the clubDescription
	 */
	public String getClubDescription() {
		return clubDescription;
	}

	/**
	 * @param clubDescription the clubDescription to set
	 */
	public void setClubDescription(String clubDescription) {
		this.clubDescription = clubDescription;
	}

}
