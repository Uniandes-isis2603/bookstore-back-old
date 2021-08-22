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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookEditorialService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private EditorialRepository editorialRepository;
	
	/**
	 * Remplazar la editorial de un book.
	 *
	 * @param bookId      id del libro que se quiere actualizar.
	 * @param editorialId El id de la editorial que se será del libro.
	 * @return el nuevo libro.
	 */

	@Transactional
	public BookEntity replaceEditorial(Long bookId, Long editorialId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(editorialId);
		if (editorialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.EDITORIAL_NOT_FOUND);

		bookEntity.get().setEditorial(editorialEntity.get());
		log.info("Termina proceso de actualizar libro con id = {0}", bookId);

		return bookEntity.get();
	}

	/**
	 * Borrar un book de una editorial. Este metodo se utiliza para borrar la
	 * relacion de un libro.
	 *
	 * @param booksId El libro que se desea borrar de la editorial.
	 */
	@Transactional
	public void removeEditorial(Long bookId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar la Editorial del libro con id = {0}", bookId);
		Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
		if (bookEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

		Optional<EditorialEntity> editorialEntity = editorialRepository
				.findById(bookEntity.get().getEditorial().getId());
		editorialEntity.ifPresent(editorial -> editorial.getBooks().remove(bookEntity.get()));

		bookEntity.get().setEditorial(null);
		log.info("Termina proceso de borrar la Editorial del libro con id = {0}", bookId);
	}
}
