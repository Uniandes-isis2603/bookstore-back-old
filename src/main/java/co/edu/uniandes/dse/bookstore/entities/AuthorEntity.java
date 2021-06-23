package co.edu.uniandes.dse.bookstore.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.uniandes.dse.bookstore.podam.DateStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class AuthorEntity extends BaseEntity {

	@Temporal(TemporalType.DATE)
	@PodamStrategyValue(DateStrategy.class)
	private Date birthDate;
	
	@PodamExclude
	@ManyToMany (mappedBy = "authors")
	private List<BookEntity> books = new ArrayList<>();
	
	@PodamExclude
	@OneToMany (mappedBy = "author", fetch = FetchType.LAZY)
	private List<PrizeEntity> prizes = new ArrayList<>();
	
	private String name;
	private String description;
	private String image;
}
