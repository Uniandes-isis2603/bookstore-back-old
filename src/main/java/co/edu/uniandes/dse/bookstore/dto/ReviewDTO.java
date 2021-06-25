package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;

@Data
public class ReviewDTO {
	private Long id;
	private String name;
    private String source;
    private String description;
    private BookDTO book;
}
