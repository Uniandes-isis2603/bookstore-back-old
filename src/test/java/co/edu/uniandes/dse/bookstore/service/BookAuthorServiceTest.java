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
import co.edu.uniandes.dse.bookstore.services.BookAuthorService;
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

	/**
	 * Prueba para asociar un autor a un libro.
	 *
	 */
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
	
	/**
	 * Prueba para asociar un autor que no existe a un libro.
	 *
	 */
	@Test
	void testAddInvalidAuthor() {
		assertThrows(EntityNotFoundException.class, ()->{
			BookEntity newBook = factory.manufacturePojo(BookEntity.class);
			newBook.setEditorial(editorial);
			entityManager.persist(newBook);
			bookAuthorService.addAuthor(newBook.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un autor a un libro que no existe.
	 *
	 */
	@Test
	void testAddAuthorInvalidBook() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			AuthorEntity author = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(author);
			bookAuthorService.addAuthor(0L, author.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de autores de un libro.
	 */
	@Test
	void testGetAuthors() throws EntityNotFoundException {
		List<AuthorEntity> authorEntities = bookAuthorService.getAuthors(book.getId());

		assertEquals(authorList.size(), authorEntities.size());

		for (int i = 0; i < authorList.size(); i++) {
			assertTrue(authorEntities.contains(authorList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de autores de un libro que no existe.
	 */
	@Test
	void testGetAuthorsInvalidBook(){
		assertThrows(EntityNotFoundException.class, ()->{
			bookAuthorService.getAuthors(0L);
		});
	}

	/**
	 * Prueba para consultar un autor de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
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
	
	/**
	 * Prueba para consultar un autor que no existe de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidAuthor()  {
		assertThrows(EntityNotFoundException.class, ()->{
			bookAuthorService.getAuthor(book.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un autor de un libro que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetAuthorInvalidBook() {
		assertThrows(EntityNotFoundException.class, ()->{
			AuthorEntity authorEntity = authorList.get(0);
			bookAuthorService.getAuthor(0L, authorEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un autor no asociado a un libro.
	 *
	 */
	@Test
	void testGetNotAssociatedAuthor() {
		assertThrows(IllegalOperationException.class, ()->{
			BookEntity newBook = factory.manufacturePojo(BookEntity.class);
			newBook.setEditorial(editorial);
			entityManager.persist(newBook);
			AuthorEntity author = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(author);
			bookAuthorService.getAuthor(newBook.getId(), author.getId());
		});
	}

	/**
	 * Prueba para actualizar los autores de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceAuthors() throws EntityNotFoundException {
		List<AuthorEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(entity);
			book.getAuthors().add(entity);
			nuevaLista.add(entity);
		}
		bookAuthorService.replaceAuthors(book.getId(), nuevaLista);
		
		List<AuthorEntity> authorEntities = bookAuthorService.getAuthors(book.getId());
		for (AuthorEntity aNuevaLista : nuevaLista) {
			assertTrue(authorEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los autores de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceAuthorsInvalidBook(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<AuthorEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
				entity.getBooks().add(book);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			bookAuthorService.replaceAuthors(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los autores que no existen de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidAuthors() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<AuthorEntity> nuevaLista = new ArrayList<>();
			AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			bookAuthorService.replaceAuthors(book.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un autor de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceAuthorsInvalidAuthor(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<AuthorEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
				entity.getBooks().add(book);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			bookAuthorService.replaceAuthors(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un autor con un libro.
	 *
	 */
	@Test
	void testRemoveAuthor() throws EntityNotFoundException {
		for (AuthorEntity author : authorList) {
			bookAuthorService.removeAuthor(book.getId(), author.getId());
		}
		assertTrue(bookAuthorService.getAuthors(book.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un autor que no existe con un libro.
	 *
	 */
	@Test
	void testRemoveInvalidAuthor(){
		assertThrows(EntityNotFoundException.class, ()->{
			bookAuthorService.removeAuthor(book.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un autor con un libro que no existe.
	 *
	 */
	@Test
	void testRemoveAuthorInvalidBook(){
		assertThrows(EntityNotFoundException.class, ()->{
			bookAuthorService.removeAuthor(0L, authorList.get(0).getId());
		});
	}

}
