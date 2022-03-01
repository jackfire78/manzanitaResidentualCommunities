package com.manzanita.spring.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database life clubs table
@Document(collection = "CommunityLifeClubs")
public class LifeClubs {
	@Id
	private String id;

	@NotBlank
	@Size(max = 25)
	private String clubName;

	@NotBlank
	@Size(max = 250)
	private String clubDescription;

	public LifeClubs() {
	}

	public LifeClubs(String clubName, String clubDescription) {
		super();
		this.clubName = clubName;
		this.clubDescription = clubDescription;
	}

	public String getId() {
		return id;
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
