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
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	EditorialRepository editorialRepository;
	
	/**
	 * Guardar un nuevo libro
	 *
	 * @param bookEntity La entidad de tipo libro del nuevo libro a persistir.
	 * @return La entidad luego de persistirla
	 * @throws IllegalOperationException Si el ISBN es inválido o ya existe en la
	 *                                   persistencia o si la editorial es inválida
	 */
	@Transactional
	public BookEntity createBook(BookEntity bookEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación del libro");
		
		if (bookEntity.getEditorial() == null)
			throw new IllegalOperationException("Editorial is not valid");
		
		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(bookEntity.getEditorial().getId());
		if (editorialEntity.isEmpty())
			throw new IllegalOperationException("Editorial is not valid");

		if (!validateISBN(bookEntity.getIsbn()))
			throw new IllegalOperationException("ISBN is not valid");

		if (!bookRepository.findByIsbn(bookEntity.getIsbn()).isEmpty())
			throw new IllegalOperationException("ISBN already exists");

		bookEntity.setEditorial(editorialEntity.get());
		log.info("Termina proceso de creación del libro");
		return bookRepository.save(bookEntity);
	}

	/**
	 * Devuelve todos los libros que hay en la base de datos.
	 *
	 * @return Lista de entidades de tipo libro.
	 */
	@Transactional
	public List<BookEntity> getBooks() {
		log.info("Inicia proceso de consultar todos los libros");
		return bookRepository.findAll();
	}

	/**
	 * Busca un libro por ID
	 *
	 * @param bookId El id del libro a buscar
	 * @return El libro encontrado
	 * @throws EntityNotFoundException Si el libro no se encuentra
	 */
	@Transactional
	public BookEntity getBook(Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);
		log.info("Termina proceso de consultar el libro con id = {0}", bookId);
		return bookEntity.get();
	}

	/**
	 * Actualizar un libro por ID
	 *
	 * @param bookId    El ID del libro a actualizar
	 * @param book La entidad del libro con los cambios deseados
	 * @return La entidad del libro luego de actualizarla
	 * @throws IllegalOperationException Si el ISBN de la actualización es inválido
	 * @throws EntityNotFoundException Si libro no es encontrado
	 */
	@Transactional
	public BookEntity updateBook(Long bookId, BookEntity book)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar el libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		if (!validateISBN(book.getIsbn()))
			throw new IllegalOperationException("ISBN is not valid");

		book.setId(bookId);
		log.info("Termina proceso de actualizar el libro con id = {0}", bookId);
		return bookRepository.save(book);
	}

	/**
	 * Eliminar un libro por ID
	 *
	 * @param bookId El ID del libro a eliminar
	 * @throws IllegalOperationException si el libro tiene autores asociados
	 * @throws EntityNotFoundException si el libro no existe
	 */
	@Transactional
	public void deleteBook(Long bookId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		List<AuthorEntity> authors = bookEntity.get().getAuthors();

		if (!authors.isEmpty())
			throw new IllegalOperationException("Unable to delete book because it has associated authors");

		bookRepository.deleteById(bookId);
		log.info("Termina proceso de borrar el libro con id = {0}", bookId);
	}

	/**
	 * Verifica que el ISBN no sea invalido.
	 *
	 * @param isbn a verificar
	 * @return true si el ISBN es valido.
	 */
	private boolean validateISBN(String isbn) {
		return !(isbn == null || isbn.isEmpty());
	}
}
