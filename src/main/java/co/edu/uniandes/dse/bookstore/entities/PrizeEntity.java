package co.edu.uniandes.dse.bookstore.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class PrizeEntity extends BaseEntity {

	@Temporal(TemporalType.DATE)
	private Date premiationDate;

	@PodamExclude
	@ManyToOne
	private AuthorEntity author;

	private String name;
	private String description;

	@PodamExclude
	@OneToOne
	private OrganizationEntity organization;
}
