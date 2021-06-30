/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.dto.OrganizationDTO;
import co.edu.uniandes.dse.bookstore.dto.OrganizationDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.OrganizationService;

/**
 * Clase que implementa el recurso "organizations".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos las organizaciones que existen en la aplicacion.
	 *
	 * @return JSONArray {@link OrganizationDTO} - Las organizaciones encontradas en
	 *         la aplicación. Si no hay ninguna retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<OrganizationDetailDTO> findAll() {
		List<OrganizationEntity> organizations = organizationService.getOrganizations();
		return modelMapper.map(organizations, new TypeToken<List<OrganizationDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca la organization con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param organizationId Identificador de la organization que se esta buscando.
	 *                       Este debe ser una cadena de dígitos.
	 * @return JSON {@link OrganizationDetailDTO} - La organization buscada
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OrganizationDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		OrganizationEntity organizationEntity = organizationService.getOrganization(id);
		return modelMapper.map(organizationEntity, OrganizationDetailDTO.class);
	}

	/**
	 * Crea una nueva organization con la informacion que se recibe en el cuerpo de
	 * la petición y se regresa un objeto identico con un id auto-generado por la
	 * base de datos.
	 *
	 * @param organizationDTO {@link OrganizationDTO} - La organization que se desea
	 *                        guardar.
	 * @return JSON {@link OrganizationDTO} - La organization guardada con el
	 *         atributo id autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public OrganizationDTO create(@RequestBody OrganizationDTO organizationDTO) throws IllegalOperationException {
		OrganizationEntity organizationEntity = organizationService
				.createOrganization(modelMapper.map(organizationDTO, OrganizationEntity.class));
		return modelMapper.map(organizationEntity, OrganizationDTO.class);
	}

	/**
	 * Actualiza la organization con el id recibido en la URL con la información que
	 * se recibe en el cuerpo de la petición.
	 *
	 * @param id           Identificador de la organization que se desea actualizar.
	 *                     Este debe ser una cadena de dígitos.
	 * @param organization {@link OrganizationDTO} La organization que se desea
	 *                     guardar.
	 * @return JSON {@link OrganizationDTO} - La organization guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OrganizationDTO update(@PathVariable("id") Long id, @RequestBody OrganizationDTO organizationDTO)
			throws EntityNotFoundException {
		OrganizationEntity organizationEntity = organizationService.updateOrganization(id,
				modelMapper.map(organizationDTO, OrganizationEntity.class));
		return modelMapper.map(organizationEntity, OrganizationDTO.class);
	}

	/**
	 * Borra la organization con el id asociado recibido en la URL.
	 *
	 * @param id Identificador de la organization que se desea borrar. Este debe ser
	 *           una cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		organizationService.deleteOrganization(id);
	}

}
