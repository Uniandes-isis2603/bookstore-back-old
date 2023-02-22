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
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad de Author y Book.
 *
 * @author ISIS2603
 */

@Slf4j
@Service
public class AuthorBookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	/**
	 * Asocia un Book existente a un Author
	 *
	 * @param authorId Identificador de la instancia de Author
	 * @param bookId   Identificador de la instancia de Book
	 * @return Instancia de BookEntity que fue asociada a Author
	 */

	@Transactional
	public BookEntity addBook(Long authorId, Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un libro al autor con id = {0}", authorId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);

		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.AUTHOR_NOT_FOUND);

		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		bookEntity.get().getAuthors().add(authorEntity.get());
		log.info("Termina proceso de asociarle un libro al autor con id = {0}", authorId);
		return bookEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de BookEntity asociadas a una instancia
	 * de Author
	 *
	 * @param authorsId Identificador de la instancia de Author
	 * @return Colección de instancias de BookEntity asociadas a la instancia de
	 *         Author
	 */
	@Transactional
	public List<BookEntity> getBooks(Long authorId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los libros del autor con id = {0}", authorId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.AUTHOR_NOT_FOUND);

		log.info("Termina proceso de consultar todos los libros del autor con id = {0}", authorId);
		return authorEntity.get().getBooks();
	}

	/**
	 * Obtiene una instancia de BookEntity asociada a una instancia de Author
	 *
	 * @param authorsId Identificador de la instancia de Author
	 * @param booksId   Identificador de la instancia de Book
	 * @return La entidadd de Libro del autor
	 */
	@Transactional
	public BookEntity getBook(Long authorId, Long bookId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el libro con id = {0} del autor con id = " + authorId, bookId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);

		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.AUTHOR_NOT_FOUND);

		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		log.info("Termina proceso de consultar el libro con id = {0} del autor con id = " + authorId, bookId);
		if (!bookEntity.get().getAuthors().contains(authorEntity.get()))
			throw new IllegalOperationException("The book is not associated to the author");
		
		return bookEntity.get();
	}

	/**
	 * Remplaza las instancias de Book asociadas a una instancia de Author
	 *
	 * @param authorId Identificador de la instancia de Author
	 * @param books    Colección de instancias de BookEntity a asociar a instancia
	 *                 de Author
	 * @return Nueva colección de BookEntity asociada a la instancia de Author
	 */
	@Transactional
	public List<BookEntity> addBooks(Long authorId, List<BookEntity> books) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los libros asociados al author con id = {0}", authorId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.AUTHOR_NOT_FOUND);

		for (BookEntity book : books) {
			Optional<BookEntity> bookEntity = bookRepository.findById(book.getId());
			if (bookEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

			if (!bookEntity.get().getAuthors().contains(authorEntity.get()))
				bookEntity.get().getAuthors().add(authorEntity.get());
		}
		log.info("Finaliza proceso de reemplazar los libros asociados al author con id = {0}", authorId);
		authorEntity.get().setBooks(books);
		return authorEntity.get().getBooks();
		//return books;
	}

	/**
	 * Desasocia un Book existente de un Author existente
	 *
	 * @param authorsId Identificador de la instancia de Author
	 * @param booksId   Identificador de la instancia de Book
	 */
	@Transactional
	public void removeBook(Long authorId, Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un libro del author con id = {0}", authorId);
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.AUTHOR_NOT_FOUND);

		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		bookEntity.get().getAuthors().remove(authorEntity.get());
		authorEntity.get().getBooks().remove(bookEntity.get());
		log.info("Finaliza proceso de borrar un libro del author con id = {0}", authorId);
	}
}
