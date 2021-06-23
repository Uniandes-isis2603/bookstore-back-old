package co.edu.uniandes.dse.bookstore.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class EditorialEntity extends BaseEntity {
	
	private String name;

	@PodamExclude
    @OneToMany(mappedBy = "editorial")
	List<BookEntity> books = new ArrayList<>();

}
