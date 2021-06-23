package co.edu.uniandes.dse.bookstore.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class OrganizationEntity extends BaseEntity{
	
	enum TIPO_ORGANIZACION {
        PRIVADA,
        PUBLICA,
        FUNDACION
    }

    private String name;
    private TIPO_ORGANIZACION tipo;

    @PodamExclude
    @OneToOne(mappedBy = "organization", fetch = FetchType.LAZY)
    private PrizeEntity prize;
}
