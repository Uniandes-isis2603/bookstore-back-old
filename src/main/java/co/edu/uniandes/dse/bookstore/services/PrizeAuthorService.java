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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repositories.PrizeRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad Prize y Author
 *
 * @author ISIS2603
 */

@Slf4j
@Data
@Service

public class PrizeAuthorService {

	@Autowired
	private AuthorRepository authorRepository; // Variable para acceder a la persistencia de la aplicación. Es una
												// inyección de dependencias.

	@Autowired
	private PrizeRepository prizeRepository; // Variable para acceder a la persistencia de la aplicación. Es una
												// inyección de dependencias.

	/**
	 * Agregar un autor a un premio
	 *
	 * @param prizesId  El id premio a guardar
	 * @param authorsId El id del autor al cual se le va a guardar el premio.
	 * @return El premio que fue agregado al autor.
	 */
	public AuthorEntity addAuthor(Long authorId, Long prizeId) {
		log.info("Inicia proceso de asociar el autor con id = {0} al premio con id = " + prizeId, authorId);
		AuthorEntity autorEntity = authorRepository.findById(authorId).get();
		PrizeEntity prizeEntity = prizeRepository.findById(prizeId).get();
		prizeEntity.setAuthor(autorEntity);
		log.info("Termina proceso de asociar el autor con id = {0} al premio con id = " + prizeId, authorId);
		return authorRepository.findById(authorId).get();
	}

	/**
	 *
	 * Obtener un premio por medio de su id y el de su autor.
	 *
	 * @param prizesId id del premio a ser buscado.
	 * @return el autor solicitada por medio de su id.
	 */
	public AuthorEntity getAuthor(Long prizeId) {
		log.info("Inicia proceso de consultar el autor del premio con id = {0}", prizeId);
		AuthorEntity authorEntity = prizeRepository.findById(prizeId).get().getAuthor();
		log.info("Termina proceso de consultar el autor del premio con id = {0}", prizeId);
		return authorEntity;
	}

	/**
	 * Remplazar autor de un premio
	 *
	 * @param prizesId  el id del premio que se quiere actualizar.
	 * @param authorsId El id del nuebo autor asociado al premio.
	 * @return el nuevo autor asociado.
	 */
	public AuthorEntity replaceAuthor(Long prizeId, Long authorId) {
		log.info("Inicia proceso de actualizar el autor del premio premio con id = {0}", prizeId);
		AuthorEntity autorEntity = authorRepository.findById(authorId).get();
		PrizeEntity prizeEntity = prizeRepository.findById(prizeId).get();
		prizeEntity.setAuthor(autorEntity);
		log.info("Termina proceso de asociar el autor con id = {0} al premio con id = " + prizeId, authorId);
		return authorRepository.findById(authorId).get();
	}

	/**
	 * Borrar el autor de un premio
	 *
	 * @param prizesId El premio que se desea borrar del autor.
	 * @throws EntityNotFoundException si el premio no tiene autor
	 */
	public void removeAuthor(Long prizeId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el autor del premio con id = {0}", prizeId);
		PrizeEntity prizeEntity = prizeRepository.findById(prizeId).get();
		if (prizeEntity.getAuthor() == null) {
			throw new EntityNotFoundException("El premio no tiene autor");
		}
		AuthorEntity authorEntity = authorRepository.findById(prizeEntity.getAuthor().getId()).get();
		prizeEntity.setAuthor(null);
		authorEntity.getPrizes().remove(prizeEntity);
		log.info("Termina proceso de borrar el autor con id = {0} del premio con id = " + prizeId,
				authorEntity.getId());
	}
}
