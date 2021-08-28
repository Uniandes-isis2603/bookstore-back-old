package co.edu.uniandes.dse.bookstore.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class UsuarioEntity extends BaseEntity {

	private String nombre;
	private String apellido;

	@Temporal( TemporalType.DATE )
	private Date fechaNacimiento;

	private Boolean estaEstudiando;
}
