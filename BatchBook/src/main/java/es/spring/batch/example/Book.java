package es.spring.batch.example;

import java.io.Serializable;

public class Book implements Serializable {
	
	private static final long serialVersionUID = 7812008875947582539L;
	
	private String title;
    private String author;

    public Book() {

    }

    public Book(String firstName, String lastName) {
        this.author = firstName;
        this.title = lastName;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
    public String toString() {
        return "Author: " + author + ", Title: " + title;
    }

}
