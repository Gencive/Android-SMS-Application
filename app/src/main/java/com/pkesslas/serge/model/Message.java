package com.pkesslas.serge.model;

/**
 * Created by Pierre-Elie on 04/03/15.
 */
public class Message {
	String message, date, author;

	public Message(String message, String date, String author) {
		this.message = message;
		this.date = date;
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
