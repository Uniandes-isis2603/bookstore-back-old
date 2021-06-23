package co.edu.uniandes.dse.bookstore.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class ReviewEntity extends BaseEntity {
	
	private String name;
    private String source;
    private String description;

    @PodamExclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    private BookEntity book;
}
