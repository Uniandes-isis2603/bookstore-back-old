package co.edu.uniandes.dse.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BookDetailDTO extends BookDTO{
	private List<ReviewDTO> reviews = new ArrayList<>();
	private List<AuthorDTO> authors = new ArrayList<>();
}
