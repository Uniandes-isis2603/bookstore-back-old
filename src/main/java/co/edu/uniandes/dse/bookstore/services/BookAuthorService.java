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

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class BookAuthorService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	/**
	 * Asocia un Author existente a un Book
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 * @return Instancia de AuthorEntity que fue asociada a Book
	 */
	@Transactional
	public AuthorEntity addAuthor(Long bookId, Long authorId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un autor al libro con id = {0}", bookId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		if (authorEntity.isEmpty())
			throw new EntityNotFoundException("The author with the given id was not found");

		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException("The book with the given id was not found");

		bookEntity.get().getAuthors().add(authorEntity.get());
		log.info("Termina proceso de asociarle un autor al libro con id = {0}", bookId);
		return authorEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de AuthorEntity asociadas a una instancia
	 * de Book
	 *
	 * @param bookId Identificador de la instancia de Book
	 * @return Colección de instancias de AuthorEntity asociadas a la instancia de
	 *         Book
	 */
	@Transactional
	public List<AuthorEntity> getAuthors(Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los autores del libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException("The book with the given id was not found");
		log.info("Finaliza proceso de consultar todos los autores del libro con id = {0}", bookId);
		return bookEntity.get().getAuthors();
	}

	/**
	 * Obtiene una instancia de AuthorEntity asociada a una instancia de Book
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 * @return La entidad del Autor asociada al libro
	 */
	@Transactional
	public AuthorEntity getAuthor(Long bookId, Long authorId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un autor del libro con id = {0}", bookId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);

		if (authorEntity.isEmpty())
			throw new EntityNotFoundException("The author with the given id was not found");

		if (bookEntity.isEmpty())
			throw new EntityNotFoundException("The book with the given id was not found");
		log.info("Termina proceso de consultar un autor del libro con id = {0}", bookId);
		if (bookEntity.get().getAuthors().contains(authorEntity.get()))
			return authorEntity.get();

		throw new IllegalOperationException("The author is not associated to the book");
	}

	@Transactional
	/**
	 * Remplaza las instancias de Author asociadas a una instancia de Book
	 *
	 * @param bookId Identificador de la instancia de Book
	 * @param list    Colección de instancias de AuthorEntity a asociar a instancia
	 *                de Book
	 * @return Nueva colección de AuthorEntity asociada a la instancia de Book
	 */
	public List<AuthorEntity> replaceAuthors(Long bookId, List<AuthorEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los autores del libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException("The book with the given id was not found");

		for (AuthorEntity author : list) {
			Optional<AuthorEntity> authorEntity = authorRepository.findById(author.getId());
			if (authorEntity.isEmpty())
				throw new EntityNotFoundException("The author with the given id was not found");

			if (!bookEntity.get().getAuthors().contains(authorEntity.get()))
				bookEntity.get().getAuthors().add(authorEntity.get());
		}
		log.info("Termina proceso de reemplazar los autores del libro con id = {0}", bookId);
		return getAuthors(bookId);
	}

	@Transactional
	/**
	 * Desasocia un Author existente de un Book existente
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 */
	public void removeAuthor(Long bookId, Long authorId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un autor del libro con id = {0}", bookId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);

		if (authorEntity.isEmpty())
			throw new EntityNotFoundException("The author with the given id was not found");

		if (bookEntity.isEmpty())
			throw new EntityNotFoundException("The book with the given id was not found");

		bookEntity.get().getAuthors().remove(authorEntity.get());

		log.info("Termina proceso de borrar un autor del libro con id = {0}", bookId);
	}
}
