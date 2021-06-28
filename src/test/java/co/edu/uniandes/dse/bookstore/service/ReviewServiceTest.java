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

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.ReviewService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
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

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EditorialEntity").executeUpdate();
	}

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

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

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

	@Test
	void testDeleteReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		reviewService.deleteReview(bookList.get(0).getId(), entity.getId());
		ReviewEntity deleted = entityManager.find(ReviewEntity.class, entity.getId());
		assertNull(deleted);
	}

	@Test
	void testDeleteReviewWithNoAssociatedBook() {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity entity = reviewList.get(0);
			reviewService.deleteReview(bookList.get(1).getId(), entity.getId());
		});
	}

}
