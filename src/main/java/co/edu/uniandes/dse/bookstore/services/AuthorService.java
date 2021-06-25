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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Author.
 *
 * @author ISIS2603
 */

@Slf4j
@Data
@Service
public class AuthorService {

	@Autowired
	AuthorRepository authorRepository;

	/**
     * Se encarga de crear un Author en la base de datos.
     *
     * @param authorEntity Objeto de AuthorEntity con los datos nuevos
     * @return Objeto de AuthorEntity con los datos nuevos y su ID.
     */
	@Transactional
	public AuthorEntity createAuthor(AuthorEntity authorEntity) {
		log.info("Inicia proceso de creación del autor");
		return authorRepository.save(authorEntity);
	}
	
	 /**
     * Obtiene la lista de los registros de Author.
     *
     * @return Colección de objetos de AuthorEntity.
     */
	@Transactional
	public List<AuthorEntity> getAuthors() {
		return authorRepository.findAll();
	}

	/**
     * Obtiene los datos de una instancia de Author a partir de su ID.
     *
     * @param authorId Identificador de la instancia a consultar
     * @return Instancia de AuthorEntity con los datos del Author consultado.
     */
	@Transactional
	public AuthorEntity getAuthor(Long authorId) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		if (authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");

		return authorEntity;
	}

	/**
     * Actualiza la información de una instancia de Author.
     *
     * @param authorId Identificador de la instancia a actualizar
     * @param authorEntity Instancia de AuthorEntity con los nuevos datos.
     * @return Instancia de AuthorEntity con los datos actualizados.
     */
	@Transactional
	public AuthorEntity updateAuthor(Long authorId, AuthorEntity authorEntity) throws EntityNotFoundException {
		AuthorEntity author = authorRepository.findById(authorId).orElse(null);
		if (author == null)
			throw new EntityNotFoundException("The author with the given id was not found");

		authorEntity.setId(authorId);
		return authorRepository.save(authorEntity);
	}

	 /**
     * Elimina una instancia de Author de la base de datos.
     *
     * @param authorId Identificador de la instancia a eliminar.
     * @throws BusinessLogicException si el autor tiene libros asociados.
     */
	@Transactional
	public void deleteAuthor(Long authorId) throws IllegalOperationException, EntityNotFoundException {

		AuthorEntity author = authorRepository.findById(authorId).orElse(null);
		if (author == null)
			throw new EntityNotFoundException("The author with the given id was not found");

		List<BookEntity> books = getAuthor(authorId).getBooks();
		if (books != null && !books.isEmpty())
			throw new IllegalOperationException("Unable to delete the author because he/she has associated books");

		List<PrizeEntity> prizes = getAuthor(authorId).getPrizes();
		if (prizes != null && !prizes.isEmpty())
			throw new IllegalOperationException("Unable to delete the author because he/she has associated prizes");

		authorRepository.deleteById(authorId);
	}
}
