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

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Editorial.
 *
 * @author ISIS2603
 */

@Slf4j
@Service
public class EditorialService {

	@Autowired
	EditorialRepository editorialRepository;

	/**
	 * Crea una editorial en la persistencia.
	 *
	 * @param editorialEntity La entidad que representa la editorial a persistir.
	 * @return La entidad de la editorial luego de persistirla.
	 * @throws IllegalOperationException Si la editorial a persistir ya existe.
	 */
	@Transactional
	public EditorialEntity createEditorial(EditorialEntity editorialEntity) throws IllegalOperationException {
		log.info("Inicia proceso de creación de la editorial");
		if (!editorialRepository.findByName(editorialEntity.getName()).isEmpty()) {
			throw new IllegalOperationException("Editorial name already exists");
		}
		log.info("Termina proceso de creación de la editorial");
		return editorialRepository.save(editorialEntity);
	}

	/**
	 *
	 * Obtener todas las editoriales existentes en la base de datos.
	 *
	 * @return una lista de editoriales.
	 */
	@Transactional
	public List<EditorialEntity> getEditorials() {
		log.info("Inicia proceso de consultar todas las editoriales");
		return editorialRepository.findAll();
	}

	/**
	 *
	 * Obtener una editorial por medio de su id.
	 *
	 * @param editorialId: id de la editorial para ser buscada.
	 * @return la editorial solicitada por medio de su id.
	 */
	@Transactional
	public EditorialEntity getEditorial(Long editorialId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la editorial con id = {0}", editorialId);
		Optional<EditorialEntity> editorial = editorialRepository.findById(editorialId);
		if (editorial.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.EDITORIAL_NOT_FOUND);
		log.info("Termina proceso de consultar la editorial con id = {0}", editorialId);
		return editorial.get();
	}

	/**
	 *
	 * Actualizar una editorial.
	 *
	 * @param editorialId:    id de la editorial para buscarla en la base de datos.
	 * @param editorial: editorial con los cambios para ser actualizada.
	 * @return la editorial con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public EditorialEntity updateEditorial(Long editorialId, EditorialEntity editorial) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar la editorial con id = {0}", editorialId);
		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(editorialId);
		if (editorialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.EDITORIAL_NOT_FOUND);

		editorial.setId(editorialId);
		log.info("Termina proceso de actualizar la editorial con id = {0}", editorialId);
		return editorialRepository.save(editorial);
	}

	/**
	 * Borrar un editorial
	 *
	 * @param editorialId: id de la editorial a borrar
	 * @throws BusinessLogicException Si la editorial a eliminar tiene libros.
	 */
	@Transactional
	public void deleteEditorial(Long editorialId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la editorial con id = {0}", editorialId);
		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(editorialId);
		if (editorialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.EDITORIAL_NOT_FOUND);

		List<BookEntity> books = editorialEntity.get().getBooks();

		if (!books.isEmpty()) {
			throw new IllegalOperationException(
					"Unable to delete editorial because it has associated books");
		}

		editorialRepository.deleteById(editorialId);
		log.info("Termina proceso de borrar la editorial con id = {0}", editorialId);
	}
}
