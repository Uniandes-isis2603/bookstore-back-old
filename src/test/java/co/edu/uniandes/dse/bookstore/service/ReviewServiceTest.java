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
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.ReviewService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Reviews
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(ReviewService.class)
class ReviewServiceTest {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<ReviewEntity> reviewList = new ArrayList<>();
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
		entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EditorialEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EditorialEntity editorial = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(editorial);
			editorialList.add(editorial);
		}

		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorialList.get(i));
			entityManager.persist(entity);
			bookList.add(entity);
		}

		for (int i = 0; i < 3; i++) {
			ReviewEntity entity = factory.manufacturePojo(ReviewEntity.class);
			entity.setBook(bookList.get(0));
			bookList.get(0).getReviews().add(entity);
			entityManager.persist(entity);
			reviewList.add(entity);
		}
	}

	/**
	 * Prueba para crear un Review.
	 */
	@Test
	void testCreateReview() throws EntityNotFoundException {
		ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
		newEntity.setBook(bookList.get(1));
		ReviewEntity result = reviewService.createReview(reviewList.get(1).getId(), newEntity);
		assertNotNull(result);
		ReviewEntity entity = entityManager.find(ReviewEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getSource(), entity.getSource());
		assertEquals(newEntity.getDescription(), entity.getDescription());
	}

	/**
	 * Prueba para crear un Review con un libro que no existe.
	 */
	@Test
	void testCreateReviewInvalidBook() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
			reviewService.createReview(0L, newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Reviews.
	 */
	@Test
	void testGetReviews() throws EntityNotFoundException {
		List<ReviewEntity> list = reviewService.getReviews(bookList.get(0).getId());
		assertEquals(reviewList.size(), list.size());
		for (ReviewEntity entity : list) {
			boolean found = false;
			for (ReviewEntity storedEntity : reviewList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar la lista de Reviews de un libro que no existe.
	 */
	@Test
	void testGetReviewsInvalidBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			reviewService.getReviews(0L);
		});
	}

	/**
	 * Prueba para consultar un Review.
	 */
	@Test
	void testGetReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		ReviewEntity resultEntity = reviewService.getReview(bookList.get(0).getId(), entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
		assertEquals(entity.getSource(), resultEntity.getSource());
		assertEquals(entity.getDescription(), resultEntity.getDescription());
	}

	/**
	 * Prueba para consultar un Review de un libro que no existe.
	 */
	@Test
	void testGetReviewInvalidBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity entity = reviewList.get(0);
			reviewService.getReview(0L, entity.getId());
		});
	}

	/**
	 * Prueba para consultar un Review que no existe de un libro.
	 */
	@Test
	void testGetInvalidReview() {
		assertThrows(EntityNotFoundException.class, () -> {
			reviewService.getReview(bookList.get(0).getId(), 0L);
		});
	}

	/**
	 * Prueba para actualizar un Review.
	 */
	@Test
	void testUpdateReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);

		pojoEntity.setId(entity.getId());

		reviewService.updateReview(bookList.get(1).getId(), entity.getId(), pojoEntity);

		ReviewEntity resp = entityManager.find(ReviewEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
		assertEquals(pojoEntity.getSource(), resp.getSource());
		assertEquals(pojoEntity.getDescription(), resp.getDescription());
	}

	/**
	 * Prueba para actualizar un Review de un libro que no existe.
	 */
	@Test
	void testUpdateReviewInvalidBook() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			ReviewEntity entity = reviewList.get(0);
			ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);
			pojoEntity.setId(entity.getId());
			reviewService.updateReview(0L, entity.getId(), pojoEntity);
		});
		
	}
	
	/**
	 * Prueba para actualizar un Review que no existe de un libro.
	 */
	@Test
	void testUpdateInvalidReview(){
		assertThrows(EntityNotFoundException.class, ()->{
			ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);
			reviewService.updateReview(bookList.get(1).getId(), 0L, pojoEntity);
		});
	}

	/**
     * Prueba para eliminar un Review.
     */
	@Test
	void testDeleteReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		reviewService.deleteReview(bookList.get(0).getId(), entity.getId());
		ReviewEntity deleted = entityManager.find(ReviewEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	/**
     * Prueba para eliminar un Reviewm de un libro que no existe.
     */
	@Test
	void testDeleteReviewInvalidBook()  {
		assertThrows(EntityNotFoundException.class, ()->{
			ReviewEntity entity = reviewList.get(0);
			reviewService.deleteReview(0L, entity.getId());
		});
	}

	 /**
     * Prueba para eliminarle un review a un book del cual no pertenece.
     */
	@Test
	void testDeleteReviewWithNoAssociatedBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity entity = reviewList.get(0);
			reviewService.deleteReview(bookList.get(1).getId(), entity.getId());
		});
	}

}
