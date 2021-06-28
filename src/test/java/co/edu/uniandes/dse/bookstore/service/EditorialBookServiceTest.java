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

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.EditorialBookService;
import co.edu.uniandes.dse.bookstore.services.EditorialService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Editorial - Books
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ EditorialService.class, EditorialBookService.class })
class EditorialBookServiceTest {

	@Autowired
	private EditorialBookService editorialBookService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EditorialEntity> editorialsList = new ArrayList<>();
	private List<BookEntity> booksList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EditorialEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			BookEntity book = factory.manufacturePojo(BookEntity.class);
			entityManager.persist(book);
			booksList.add(book);
		}

		for (int i = 0; i < 3; i++) {
			EditorialEntity entity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(entity);
			editorialsList.add(entity);
			if (i == 0) {
				booksList.get(i).setEditorial(entity);
				entity.getBooks().add(booksList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un Book existente a un Editorial.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddBook() throws EntityNotFoundException {
		EditorialEntity entity = editorialsList.get(0);
		BookEntity bookEntity = booksList.get(1);
		BookEntity response = editorialBookService.addBook(bookEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(bookEntity.getId(), response.getId());
	}

	/**
	 * Prueba para obtener una colección de instancias de Books asociadas a una
	 * instancia Editorial.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetBooks() throws EntityNotFoundException {
		List<BookEntity> list = editorialBookService.getBooks(editorialsList.get(0).getId());
		assertEquals(1, list.size());
	}

	/**
	 * Prueba para obtener una instancia de Book asociada a una instancia Editorial.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	void testGetBook() throws EntityNotFoundException, IllegalOperationException {
		EditorialEntity entity = editorialsList.get(0);
		BookEntity bookEntity = booksList.get(0);
		BookEntity response = editorialBookService.getBook(entity.getId(), bookEntity.getId());

		assertEquals(bookEntity.getId(), response.getId());
		assertEquals(bookEntity.getName(), response.getName());
		assertEquals(bookEntity.getDescription(), response.getDescription());
		assertEquals(bookEntity.getIsbn(), response.getIsbn());
		assertEquals(bookEntity.getImage(), response.getImage());
	}

	/**
	 * Prueba para obtener una instancia de Books asociada a una instancia Editorial
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void getBookNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			EditorialEntity entity = editorialsList.get(0);
			BookEntity bookEntity = booksList.get(1);
			editorialBookService.getBook(entity.getId(), bookEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial.
	 */
	@Test
	void testReplaceBooks() throws EntityNotFoundException {
		EditorialEntity entity = editorialsList.get(0);
		List<BookEntity> list = booksList.subList(1, 3);
		editorialBookService.replaceBooks(entity.getId(), list);

		for (BookEntity book : list) {
			BookEntity b = entityManager.find(BookEntity.class, book.getId());
			assertTrue(b.getEditorial().equals(entity));
		}
	}
}
