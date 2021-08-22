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
import co.edu.uniandes.dse.bookstore.repositories.PrizeRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Prize.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class PrizeService {

	@Autowired
	PrizeRepository prizeRepository;

	@Autowired
	OrganizationRepository organizationRepository;

	/**
	 * Guardar un nuevo premio
	 *
	 * @param prizeEntity La entidad de tipo premio del nuevo premio a persistir.
	 * @return La entidad luego de persistirla
	 * @throws IllegalOperationException si la organizacion no existe o ya tiene
	 *                                   premio.
	 */

	@Transactional
	public PrizeEntity createPrize(PrizeEntity prizeEntity) throws IllegalOperationException {
		log.info("Inicia proceso de creación de premio");
		if (prizeEntity.getOrganization() == null)
			throw new IllegalOperationException("Organization is not valid");

		Optional<OrganizationEntity> organizationEntity = organizationRepository
				.findById(prizeEntity.getOrganization().getId());
		if (organizationEntity.isEmpty())
			throw new IllegalOperationException("Organization is not valid");

		if (organizationEntity.get().getPrize() != null)
			throw new IllegalOperationException("Organization already holds a prize");

		log.info("Termina proceso de creación de premio");
		return prizeRepository.save(prizeEntity);
	}

	/**
	 * Devuelve todos los premios que hay en la base de datos.
	 *
	 * @return Lista de entidades de tipo premio.
	 */
	@Transactional
	public List<PrizeEntity> getPrizes() {
		log.info("Inicia proceso de consultar todos los premios");
		return prizeRepository.findAll();
	}

	/**
	 * Busca un premio por ID
	 *
	 * @param prizeId El id del premio a buscar
	 * @return El premio encontrado
	 * @throws EntityNotFoundException si no encuentra el premio
	 */
	@Transactional
	public PrizeEntity getPrize(Long prizeId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar premio con id = {0}", prizeId);
		Optional<PrizeEntity> prizeEntity = prizeRepository.findById(prizeId);
		if (prizeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRIZE_NOT_FOUND);

		log.info("Termina proceso de consultar premio con id = {0}", prizeId);
		return prizeEntity.get();
	}

	/**
	 * Actualizar un premio por ID
	 *
	 * @param prizeId El ID del premio a actualizar
	 * @param prize   La entidad del premio con los cambios deseados
	 * @return La entidad del premio luego de actualizarla
	 */
	@Transactional
	public PrizeEntity updatePrize(Long prizeId, PrizeEntity prize) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar premio con id = {0}", prizeId);
		Optional<PrizeEntity> prizeEntity = prizeRepository.findById(prizeId);
		if (prizeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRIZE_NOT_FOUND);

		prize.setId(prizeId);

		log.info("Termina proceso de actualizar premio con id = {0}", prizeId);
		return prizeRepository.save(prize);
	}

	/**
	 * Eliminar un premio por ID
	 *
	 * @param prizeId El ID del premio a eliminar
	 * @throws BusinessLogicException si el premio tiene un autor asociado.
	 */

	@Transactional
	public void deletePrize(Long prizeId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar premio con id = {0}", prizeId);
		Optional<PrizeEntity> prizeEntity = prizeRepository.findById(prizeId);
		if (prizeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRIZE_NOT_FOUND);

		if (prizeEntity.get().getAuthor() != null) {
			throw new IllegalOperationException("Unable to delete prize because it has an associated author");
		}

		prizeRepository.deleteById(prizeId);
		log.info("Termina proceso de borrar premio con id = {0}", prizeId);
	}
}
