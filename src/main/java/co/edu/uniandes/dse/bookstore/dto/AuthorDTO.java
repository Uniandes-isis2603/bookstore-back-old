package co.edu.uniandes.dse.bookstore.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AuthorDTO {
	private Long id;
	private Date birthDate;
	private String name;
	private String description;
	private String image;
}
