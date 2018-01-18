package es.spring.cloud.example.bookservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="BOOK")
public class Book {
	@Id
	@GeneratedValue
	@Column(name="ID")
    private Long id;
	
	@Column(name="AUTHOR")
    private String author;
    
	@Column(name="TITLE")
    private String title;
    
	public Long getId() {
		return id;
	}
	
	public Book() {
		super();
	}

	public Book(String author, String title) {
		super();
		this.author = author;
		this.title = title;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}