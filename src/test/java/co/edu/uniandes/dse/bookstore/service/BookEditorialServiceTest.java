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
import co.edu.uniandes.dse.bookstore.services.BookEditorialService;
import co.edu.uniandes.dse.bookstore.services.BookService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Book - Editorial
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ BookService.class, BookEditorialService.class })

class BookEditorialServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookEditorialService bookEditorialService;

	@Autowired
	private BookService bookService;

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
			BookEntity books = factory.manufacturePojo(BookEntity.class);
			entityManager.persist(books);
			booksList.add(books);
		}
		for (int i = 0; i < 3; i++) {
			EditorialEntity entity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(entity);
			editorialsList.add(entity);
			if (i == 0) {
				booksList.get(i).setEditorial(entity);
			}
		}
	}

	/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceEditorial() throws EntityNotFoundException {
		BookEntity entity = booksList.get(0);
		bookEditorialService.replaceEditorial(entity.getId(), editorialsList.get(1).getId());
		entity = bookService.getBook(entity.getId());
		assertEquals(entity.getEditorial(), editorialsList.get(1));
	}
	
	/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial con un libro que no existe
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceEditorialInvalidBook() {
		assertThrows(EntityNotFoundException.class, ()->{
			bookEditorialService.replaceEditorial(0L, editorialsList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidEditorial() {
		assertThrows(EntityNotFoundException.class, ()->{
			BookEntity entity = booksList.get(0);
			bookEditorialService.replaceEditorial(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar un Book existente de un Editorial existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEditorial() throws EntityNotFoundException {
		bookEditorialService.removeEditorial(booksList.get(0).getId());
		BookEntity response = bookService.getBook(booksList.get(0).getId());
		assertNull(response.getEditorial());
	}
	
	/**
	 * Prueba para desasociar un Book que no existe de un Editorial
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEditorialInvalidBook() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			bookEditorialService.removeEditorial(0L);
		});
	}
	

}
