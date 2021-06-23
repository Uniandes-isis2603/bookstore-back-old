package co.edu.uniandes.dse.bookstore.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@MappedSuperclass
public abstract class BaseEntity {
	
	@PodamExclude
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
}
