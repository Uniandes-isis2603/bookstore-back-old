package co.edu.uniandes.dse.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EditorialDetailDTO extends EditorialDTO {
	 private List<BookDTO> books = new ArrayList<>();
}
