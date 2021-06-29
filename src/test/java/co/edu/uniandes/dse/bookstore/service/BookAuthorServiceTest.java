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
import co.edu.uniandes.dse.bookstore.services.BookAuthorService;
import co.edu.uniandes.dse.bookstore.services.BookService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Book - Author
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(BookAuthorService.class)
class BookAuthorServiceTest {
	
	@Autowired
	private BookAuthorService bookAuthorService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private BookEntity book = new BookEntity();
	private EditorialEntity editorial = new EditorialEntity();
	private List<AuthorEntity> authorList = new ArrayList<>();

	
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

		book = factory.manufacturePojo(BookEntity.class);
		book.setEditorial(editorial);
		entityManager.persist(book);

		for (int i = 0; i < 3; i++) {
			AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(entity);
			entity.getBooks().add(book);
			authorList.add(entity);
			book.getAuthors().add(entity);	
		}
	}

	@Test
	void testAddAuthor() throws EntityNotFoundException, IllegalOperationException {
		BookEntity newBook = factory.manufacturePojo(BookEntity.class);
		newBook.setEditorial(editorial);
		entityManager.persist(newBook);
		
		AuthorEntity author = factory.manufacturePojo(AuthorEntity.class);
		entityManager.persist(author);
		
		bookAuthorService.addAuthor(newBook.getId(), author.getId());
		
		AuthorEntity lastAuthor = bookAuthorService.getAuthor(newBook.getId(), author.getId());
		assertEquals(author.getId(), lastAuthor.getId());
		assertEquals(author.getBirthDate(), lastAuthor.getBirthDate());
		assertEquals(author.getDescription(), lastAuthor.getDescription());
		assertEquals(author.getImage(), lastAuthor.getImage());
		assertEquals(author.getName(), lastAuthor.getName());
	}

	@Test
	void testGetAuthors() throws EntityNotFoundException {
		List<AuthorEntity> authorEntities = bookAuthorService.getAuthors(book.getId());

		assertEquals(authorList.size(), authorEntities.size());

		for (int i = 0; i < authorList.size(); i++) {
			assertTrue(authorEntities.contains(authorList.get(0)));
		}
	}

	@Test
	void testGetAuthor() throws EntityNotFoundException, IllegalOperationException {
		AuthorEntity authorEntity = authorList.get(0);
		AuthorEntity author = bookAuthorService.getAuthor(book.getId(), authorEntity.getId());
		assertNotNull(author);

		assertEquals(authorEntity.getId(), author.getId());
		assertEquals(authorEntity.getName(), author.getName());
		assertEquals(authorEntity.getDescription(), author.getDescription());
		assertEquals(authorEntity.getImage(), author.getImage());
		assertEquals(authorEntity.getBirthDate(), author.getBirthDate());
	}

	@Test
	void testReplaceAuthors() throws EntityNotFoundException {
		List<AuthorEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
			entity.getBooks().add(book);		
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		bookAuthorService.replaceAuthors(book.getId(), nuevaLista);
		
		List<AuthorEntity> authorEntities = bookAuthorService.getAuthors(book.getId());
		for (AuthorEntity aNuevaLista : nuevaLista) {
			assertTrue(authorEntities.contains(aNuevaLista));
		}
	}

	@Test
	void testRemoveAuthor() throws EntityNotFoundException {
		for (AuthorEntity author : authorList) {
			bookAuthorService.removeAuthor(book.getId(), author.getId());
		}
		assertTrue(bookAuthorService.getAuthors(book.getId()).isEmpty());
	}

}
