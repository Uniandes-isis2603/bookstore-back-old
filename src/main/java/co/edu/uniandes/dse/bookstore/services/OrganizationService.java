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

package co.edu.uniandes.dse.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Organizacion.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class OrganizationService {

	@Autowired
	OrganizationRepository organizationRepository;
	
	/**
	 * Crea una organizacion en la persistencia.
	 *
	 * @param organizationEntity La entidad que representa la organizacion a
	 *                           persistir.
	 * @return La entidad de la organizacion luego de persistirla.
	 * @throws BusinessLogicException Si la organizacion a persistir ya existe.
	 */
	@Transactional
	public OrganizationEntity createOrganization(OrganizationEntity organizationEntity)
			throws IllegalOperationException {
		log.info("Inicia proceso de creación de la organizacion");
		if (!organizationRepository.findByName(organizationEntity.getName()).isEmpty()) {
			throw new IllegalOperationException("Organization name already exists");
		}
		log.info("Termina proceso de creación de la organizacion");
		return organizationRepository.save(organizationEntity);
	}

	/**
	 * Obtener todas las organizaciones existentes en la base de datos.
	 *
	 * @return una lista de organizaciones.
	 */
	@Transactional
	public List<OrganizationEntity> getOrganizations() {
		log.info("Inicia proceso de consultar todas las organizaciones");
		return organizationRepository.findAll();
	}

	/**
	 * Obtener una organizacion por medio de su id.
	 *
	 * @param organizationId: id de la organizacion para ser buscada.
	 * @return la organizacion solicitada por medio de su id.
	 */
	@Transactional
	public OrganizationEntity getOrganization(Long organizationId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar organizacion con id = {0}", organizationId);
		Optional<OrganizationEntity> organizationEntity = organizationRepository.findById(organizationId);

		if (organizationEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND);

		log.info("Termina proceso de consultar organizacion con id = {0}", organizationId);
		return organizationEntity.get();
	}

	/**
	 * Actualizar una organizacion.
	 *
	 * @param organizationId: id de la organizacion para buscarla en la base de
	 *                        datos.
	 * @param organization:   organizacion con los cambios para ser actualizada, por
	 *                        ejemplo el nombre.
	 * @return la organizacion con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public OrganizationEntity updateOrganization(Long organizationId, OrganizationEntity organization)
			throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar organizacion con id = {0}", organizationId);
		Optional<OrganizationEntity> organizationEntity = organizationRepository.findById(organizationId);
		if (organizationEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND);

		organization.setId(organizationId);
		log.info("Termina proceso de actualizar organizacion con id={0}", organizationId);
		return organizationRepository.save(organization);
	}

	/**
	 * Borrar un organizacion
	 *
	 * @param organizationId: id de la organizacion a borrar
	 * @throws BusinessLogicException si la organizacion tiene un premio asociado.
	 */
	@Transactional
	public void deleteOrganization(Long organizationId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar organizacion con id = {0}", organizationId);
		Optional<OrganizationEntity> organizationEntity = organizationRepository.findById(organizationId);
		if (organizationEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND);

		PrizeEntity prize = organizationEntity.get().getPrize();
		if (prize != null)
			throw new IllegalOperationException("Unable to delete organization because it has a prize");

		organizationRepository.deleteById(organizationId);
		log.info("Termina proceso de borrar organizacion con id = {0}", organizationId);
	}
}
