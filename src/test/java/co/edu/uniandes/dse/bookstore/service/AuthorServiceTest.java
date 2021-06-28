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

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Authors
 *
 * @author ISIS2603
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import(AuthorService.class)
class AuthorServiceTest {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<AuthorEntity> authorList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from PrizeEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from AuthorEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(authorEntity);
			authorList.add(authorEntity);
		}

		AuthorEntity authorEntity = authorList.get(2);
		BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
		bookEntity.getAuthors().add(authorEntity);
		entityManager.persist(bookEntity);

		authorEntity.getBooks().add(bookEntity);

		PrizeEntity prize = factory.manufacturePojo(PrizeEntity.class);
		prize.setAuthor(authorList.get(1));
		entityManager.persist(prize);
		authorList.get(1).getPrizes().add(prize);
	}

	/**
	 * Prueba para crear un Author.
	 */
	@Test
	void testCreateAuthor() {
		AuthorEntity newEntity = factory.manufacturePojo(AuthorEntity.class);
		AuthorEntity result = authorService.createAuthor(newEntity);
		assertNotNull(result);

		AuthorEntity entity = entityManager.find(AuthorEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getBirthDate(), entity.getBirthDate());
		assertEquals(newEntity.getDescription(), entity.getDescription());
	}

	/**
	 * Prueba para consultar la lista de Authors.
	 */
	@Test
	void testGetAuthors() {
		List<AuthorEntity> authorsList = authorService.getAuthors();
		assertEquals(authorList.size(), authorsList.size());

		for (AuthorEntity authorEntity : authorsList) {
			boolean found = false;
			for (AuthorEntity storedEntity : authorList) {
				if (authorEntity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Author.
	 */
	@Test
	void testGetAuthor() throws EntityNotFoundException {
		AuthorEntity authorEntity = authorList.get(0);

		AuthorEntity resultEntity = authorService.getAuthor(authorEntity.getId());
		assertNotNull(resultEntity);

		assertEquals(authorEntity.getId(), resultEntity.getId());
		assertEquals(authorEntity.getName(), resultEntity.getName());
		assertEquals(authorEntity.getBirthDate(), resultEntity.getBirthDate());
		assertEquals(authorEntity.getDescription(), resultEntity.getDescription());
	}

	/**
	 * Prueba para actualizar un Author.
	 */
	@Test
	void testUpdateAuthor() throws EntityNotFoundException {
		AuthorEntity authorEntity = authorList.get(0);
		AuthorEntity pojoEntity = factory.manufacturePojo(AuthorEntity.class);

		pojoEntity.setId(authorEntity.getId());

		authorService.updateAuthor(authorEntity.getId(), pojoEntity);

		AuthorEntity response = entityManager.find(AuthorEntity.class, authorEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getName(), response.getName());
		assertEquals(pojoEntity.getBirthDate(), response.getBirthDate());
		assertEquals(pojoEntity.getDescription(), response.getDescription());
	}

	/**
	 * Prueba para eliminar un Author
	 *
	 */
	@Test
	void testDeleteAuthor() throws EntityNotFoundException, IllegalOperationException {
		AuthorEntity authorEntity = authorList.get(0);
		authorService.deleteAuthor(authorEntity.getId());
		AuthorEntity deleted = entityManager.find(AuthorEntity.class, authorEntity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Author asociado a un libro
	 *
	 */
	@Test
	void testDeleteAuthorWithBooks() {
		assertThrows(IllegalOperationException.class, () -> {
			authorService.deleteAuthor(authorList.get(2).getId());
		});
	}

	/**
	 * Prueba para eliminar un Author asociado a un premio
	 *
	 */
	@Test
	void testDeleteAuthorWithPrize() {
		assertThrows(IllegalOperationException.class, () -> {
			authorService.deleteAuthor(authorList.get(1).getId());
		});
	}

}
