package co.edu.uniandes.dse.bookstore.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookDTO {
	private Long id;
	private String name;
	private String isbn;
	private String image;
	private Date publishingDate;
	private String description;
	private EditorialDTO editorial;
}
