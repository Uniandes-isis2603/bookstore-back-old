package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;

@Data
public class OrganizationDTO {
	private Long id;
	
	public enum TIPO_ORGANIZACION {
		PRIVADA, PUBLICA, FUNDACION
	}

	private String name;
	private TIPO_ORGANIZACION tipo;
	//private PrizeDTO prize;
}
