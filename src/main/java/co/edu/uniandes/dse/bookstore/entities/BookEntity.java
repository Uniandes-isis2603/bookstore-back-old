package co.edu.uniandes.dse.bookstore.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class BookEntity extends BaseEntity {
	
	private String name;
	private String isbn;
	private String image;
	
	@Temporal(TemporalType.DATE)
	private Date publishDate;
	
	private String description;

	@PodamExclude
	@ManyToOne
	private EditorialEntity editorial;

	@PodamExclude
	@OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ReviewEntity> reviews = new ArrayList<>();

	@PodamExclude
	@ManyToMany
	private List<AuthorEntity> authors = new ArrayList<>();
}
