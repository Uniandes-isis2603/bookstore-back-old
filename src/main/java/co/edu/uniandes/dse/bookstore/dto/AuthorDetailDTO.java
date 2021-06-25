package co.edu.uniandes.dse.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuthorDetailDTO extends AuthorDTO {
	private List<BookDTO> books = new ArrayList<>();
	private List<PrizeDTO> prizes = new ArrayList<>();
}
