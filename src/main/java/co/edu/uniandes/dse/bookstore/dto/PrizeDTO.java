package co.edu.uniandes.dse.bookstore.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PrizeDTO {
	private Long id;
	private Date fechaEntrega;
	private AuthorDTO author;
	private String name;
	private String description;
	private OrganizationDTO organization;
}
