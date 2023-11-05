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

package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorBookService;
import co.edu.uniandes.dse.bookstore.services.BookService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Author - Books
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ AuthorBookService.class, BookService.class })
class AuthorBookServiceTest {

	@Autowired
	private AuthorBookService authorBookService;

	@Autowired
	private BookService bookService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private AuthorEntity author = new AuthorEntity();
	private EditorialEntity editorial = new EditorialEntity();
	private List<BookEntity> bookList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from AuthorEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		editorial = factory.manufacturePojo(EditorialEntity.class);
		entityManager.persist(editorial);

		author = factory.manufacturePojo(AuthorEntity.class);
		entityManager.persist(author);

		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorial);
			entity.getAuthors().add(author);
			entityManager.persist(entity);
			bookList.add(entity);
			author.getBooks().add(entity);
		}
	}

	/**
	 * Prueba para asociar un libro a un author.
	 *
	 */
	@Test
	void testAddBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity newBook = factory.manufacturePojo(BookEntity.class);
		newBook.setEditorial(editorial);
		bookService.createBook(newBook);

		BookEntity bookEntity = authorBookService.addBook(author.getId(), newBook.getId());
		assertNotNull(bookEntity);

		assertEquals(bookEntity.getId(), newBook.getId());
		assertEquals(bookEntity.getName(), newBook.getName());
		assertEquals(bookEntity.getDescription(), newBook.getDescription());
		assertEquals(bookEntity.getIsbn(), newBook.getIsbn());
		assertEquals(bookEntity.getImage(), newBook.getImage());

		BookEntity lastBook = authorBookService.getBook(author.getId(), newBook.getId());

		assertEquals(lastBook.getId(), newBook.getId());
		assertEquals(lastBook.getName(), newBook.getName());
		assertEquals(lastBook.getDescription(), newBook.getDescription());
		assertEquals(lastBook.getIsbn(), newBook.getIsbn());
		assertEquals(lastBook.getImage(), newBook.getImage());

	}
	

	/**
	 * Prueba para asociar un libro a un author que no existe.
	 *
	 */

	@Test
	void testAddBookInvalidAuthor() throws EntityNotFoundException, IllegalOperationException {
		BookEntity newBook = factory.manufacturePojo(BookEntity.class);
		newBook.setEditorial(editorial);
		bookService.createBook(newBook);
		assertThrows(EntityNotFoundException.class, () -> {
			authorBookService.addBook(0L, newBook.getId());
		});
	}

	/**
	 * Prueba para asociar un libro que no existe a un author.
	 *
	 */
	@Test
	void testAddInvalidBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			authorBookService.addBook(author.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar la lista de libros de un autor.
	 */
	@Test
	void testGetBooks() throws EntityNotFoundException {
		List<BookEntity> bookEntities = authorBookService.getBooks(author.getId());

		assertEquals(bookList.size(), bookEntities.size());

		for (int i = 0; i < bookList.size(); i++) {
			assertTrue(bookEntities.contains(bookList.get(0)));
		}
	}

	/**
	 * Prueba para consultar la lista de libros de un autor que no existe.
	 */
	@Test
	void testGetBooksInvalidAuthor() {
		assertThrows(EntityNotFoundException.class, () -> {
			authorBookService.getBooks(0L);
		});
	}

	/**
	 * Prueba para consultar un libro de un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = bookList.get(0);
		BookEntity book = authorBookService.getBook(author.getId(), bookEntity.getId());
		assertNotNull(book);

		assertEquals(bookEntity.getId(), book.getId());
		assertEquals(bookEntity.getName(), book.getName());
		assertEquals(bookEntity.getDescription(), book.getDescription());
		assertEquals(bookEntity.getIsbn(), book.getIsbn());
		assertEquals(bookEntity.getImage(), book.getImage());
	}

	/**
	 * Prueba para consultar un libro de un autor que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetBookInvalidAuthor() {
		assertThrows(EntityNotFoundException.class, () -> {
			BookEntity bookEntity = bookList.get(0);
			authorBookService.getBook(0L, bookEntity.getId());
		});
	}

	/**
	 * Prueba para consultar un libro que no existe de un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			authorBookService.getBook(author.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un libro que no está asociado a un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetBookNotAssociatedAuthor() {
		assertThrows(IllegalOperationException.class, () -> {
			AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(authorEntity);

			BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
			bookEntity.setEditorial(editorial);
			entityManager.persist(bookEntity);

			authorBookService.getBook(authorEntity.getId(), bookEntity.getId());
		});
	}

	/**
	 * Prueba para actualizar los libros de un autor.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceBooks() throws EntityNotFoundException, IllegalOperationException {
		List<BookEntity> nuevaLista = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorial);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		
		authorBookService.addBooks(author.getId(), nuevaLista);
		
		List<BookEntity> bookEntities = entityManager.find(AuthorEntity.class, author.getId()).getBooks();
		for (BookEntity item : nuevaLista) {
			assertTrue(bookEntities.contains(item));
		}
	}
	
	/**
	 * Prueba para actualizar los libros de un autor que no existe.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceBooksInvalidAuthor() {
		List<BookEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorial);
			bookService.createBook(entity);
			nuevaLista.add(entity);
		}
		assertThrows(EntityNotFoundException.class, () -> {			
			authorBookService.addBooks(0L, nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar los libros que no existen de un autor.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceInvalidBooks() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<BookEntity> nuevaLista = new ArrayList<>();
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorial);
			entity.setId(0L);
			nuevaLista.add(entity);
			authorBookService.addBooks(author.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un libro con un autor.
	 *
	 */
	@Test
	void testRemoveBook() throws EntityNotFoundException {
		for (BookEntity book : bookList) {
			authorBookService.removeBook(author.getId(), book.getId());
		}
		assertTrue(authorBookService.getBooks(author.getId()).isEmpty());
	}

	/**
	 * Prueba desasociar un libro con un autor que no existe.
	 *
	 */
	@Test
	void testRemoveBookInvalidAuthor() {
		assertThrows(EntityNotFoundException.class, () -> {
			for (BookEntity book : bookList) {
				authorBookService.removeBook(0L, book.getId());
			}
		});
	}

	/**
	 * Prueba desasociar un libro que no existe con un autor.
	 *
	 */
	@Test
	void testRemoveInvalidBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			authorBookService.removeBook(author.getId(), 0L);
		});
	}
}
