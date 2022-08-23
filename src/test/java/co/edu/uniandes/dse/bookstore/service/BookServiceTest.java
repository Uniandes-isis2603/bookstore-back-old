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
import co.edu.uniandes.dse.bookstore.services.BookService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Books
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(BookService.class)
class BookServiceTest {

	@Autowired
	private BookService bookService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<BookEntity> bookList = new ArrayList<>();
	private List<EditorialEntity> editorialList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from BookEntity");
		entityManager.getEntityManager().createQuery("delete from EditorialEntity");
		entityManager.getEntityManager().createQuery("delete from AuthorEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EditorialEntity editorialEntity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(editorialEntity);
			editorialList.add(editorialEntity);
		}

		for (int i = 0; i < 3; i++) {
			BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
			bookEntity.setEditorial(editorialList.get(0));
			entityManager.persist(bookEntity);
			bookList.add(bookEntity);
		}

		AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
		entityManager.persist(authorEntity);
		authorEntity.getBooks().add(bookList.get(0));
		bookList.get(0).getAuthors().add(authorEntity);
	}

	/**
	 * Prueba para crear un Book
	 */
	@Test
	void testCreateBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
		newEntity.setEditorial(editorialList.get(0));
		newEntity.setIsbn("1-4028-9462-7");
		BookEntity result = bookService.createBook(newEntity);
		assertNotNull(result);
		BookEntity entity = entityManager.find(BookEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getDescription(), entity.getDescription());
		assertEquals(newEntity.getImage(), entity.getImage());
		assertEquals(newEntity.getPublishingDate(), entity.getPublishingDate());
		assertEquals(newEntity.getIsbn(), entity.getIsbn());
	}

	/**
	 * Prueba para crear un Book con ISBN inválido
	 */
	@Test
	void testCreateBookWithNoValidISBN() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn("");
			bookService.createBook(newEntity);
		});
	}

	/**
	 * Prueba para crear un Book con ISBN inválido
	 */
	@Test
	void testCreateBookWithNoValidISBN2() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn(null);
			bookService.createBook(newEntity);
		});
	}

	/**
	 * Prueba para crear un Book con ISBN existente.
	 */
	@Test
	void testCreateBookWithStoredISBN() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn(bookList.get(0).getIsbn());
			bookService.createBook(newEntity);
		});
	}

	/**
	 * Prueba para crear un Book con una editorial que no existe
	 */
	@Test
	void testCreateBookWithInvalidEditorial() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setIsbn("1-4028-9462-7");
			EditorialEntity editorialEntity = new EditorialEntity();
			editorialEntity.setId(0L);
			newEntity.setEditorial(editorialEntity);
			bookService.createBook(newEntity);
		});
	}

	/**
	 * Prueba para crear un Book con una editorial en null.
	 */
	@Test
	void testCreateBookWithNullEditorial() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(null);
			bookService.createBook(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Books.
	 */
	@Test
	void testGetBooks() {
		List<BookEntity> list = bookService.getBooks();
		assertEquals(bookList.size(), list.size());
		for (BookEntity entity : list) {
			boolean found = false;
			for (BookEntity storedEntity : bookList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Book.
	 */
	@Test
	void testGetBook() throws EntityNotFoundException {
		BookEntity entity = bookList.get(0);
		BookEntity resultEntity = bookService.getBook(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
		assertEquals(entity.getDescription(), resultEntity.getDescription());
		assertEquals(entity.getIsbn(), resultEntity.getIsbn());
		assertEquals(entity.getImage(), resultEntity.getImage());
	}
	
	/**
	 * Prueba para consultar un Book que no existe.
	 */
	@Test
	void testGetInvalidBook() {
		assertThrows(EntityNotFoundException.class,()->{
			bookService.getBook(0L);
		});
	}


	/**
	 * Prueba para actualizar un Book.
	 */
	@Test
	void testUpdateBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity entity = bookList.get(0);
		BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
		pojoEntity.setId(entity.getId());
		bookService.updateBook(entity.getId(), pojoEntity);

		BookEntity resp = entityManager.find(BookEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
		assertEquals(pojoEntity.getDescription(), resp.getDescription());
		assertEquals(pojoEntity.getIsbn(), resp.getIsbn());
		assertEquals(pojoEntity.getImage(), resp.getImage());
		assertEquals(pojoEntity.getPublishingDate(), resp.getPublishingDate());
	}
	
	/**
	 * Prueba para actualizar un Book inválido.
	 */
	@Test
	void testUpdateBookInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
			pojoEntity.setId(0L);
			bookService.updateBook(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Book con ISBN inválido.
	 */
	@Test
	void testUpdateBookWithNoValidISBN() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity entity = bookList.get(0);
			BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
			pojoEntity.setIsbn("");
			pojoEntity.setId(entity.getId());
			bookService.updateBook(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para actualizar un Book con ISBN inválido.
	 */
	@Test
	void testUpdateBookWithNoValidISBN2() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity entity = bookList.get(0);
			BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
			pojoEntity.setIsbn(null);
			pojoEntity.setId(entity.getId());
			bookService.updateBook(entity.getId(), pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Book.
	 */
	@Test
	void testDeleteBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity entity = bookList.get(1);
		bookService.deleteBook(entity.getId());
		BookEntity deleted = entityManager.find(BookEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar un Book que no existe.
	 */
	@Test
	void testDeleteInvalidBook() {
		assertThrows(EntityNotFoundException.class, ()->{
			bookService.deleteBook(0L);
		});
	}

	/**
	 * Prueba para eliminar un Book con un author asociado.
	 */
	@Test
	void testDeleteBookWithAuthor() {
		assertThrows(IllegalOperationException.class, () -> {
			BookEntity entity = bookList.get(0);
			bookService.deleteBook(entity.getId());
		});
	}

}
