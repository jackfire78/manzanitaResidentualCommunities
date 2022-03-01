package com.manzanita.spring.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database movies table
@Document(collection = "CommunityLifeMovies")
public class LifeMovies {
	@Id
	private String id;

	@NotBlank
	@Size(max = 25)
	private String movieName;

	@NotBlank
	private Date movieDate;

	@NotBlank
	@Size(max = 250)
	private String movieDescription;

	public LifeMovies() {
	}

	/**
	 * @param movieName
	 * @param movieDate
	 * @param movieDescription
	 */
	public LifeMovies(String movieName, Date movieDate, String movieDescription) {
		this.movieName = movieName;
		this.movieDate = movieDate;
		this.movieDescription = movieDescription;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}

	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	/**
	 * @return the movieDate
	 */
	public Date getMovieDate() {
		return movieDate;
	}

	/**
	 * @param movieDate the movieDate to set
	 */
	public void setMovieDate(Date movieDate) {
		this.movieDate = movieDate;
	}

	/**
	 * @return the movieDescription
	 */
	public String getMovieDescription() {
		return movieDescription;
	}

	/**
	 * @param movieDescription the movieDescription to set
	 */
	public void setMovieDescription(String movieDescription) {
		this.movieDescription = movieDescription;
	}

}
