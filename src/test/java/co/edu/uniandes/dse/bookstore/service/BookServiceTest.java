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
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import(BookService.class)

class BookServiceTest {
	
	@Autowired
	private BookService bookService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<BookEntity> data = new ArrayList<>();

	@BeforeEach
	void setUp() {
		
	}

	@Test
	void testCreateBook() throws BusinessLogicException {
		BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
		BookEntity result = bookService.createBook(newEntity);
		assertNotNull(result);

		BookEntity bookEntity = entityManager.find(BookEntity.class, result.getId());

		assertEquals(bookEntity.getId(), result.getId());
		assertEquals(bookEntity.getName(), result.getName());
		assertEquals(bookEntity.getDescription(), result.getDescription());
		assertEquals(bookEntity.getImage(), result.getImage());
		assertEquals(bookEntity.getPublishDate(), result.getPublishDate());
		assertEquals(bookEntity.getIsbn(), result.getIsbn());
	}

	@Test
	void testGetBooks() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBook() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateBook() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteBook() {
		fail("Not yet implemented");
	}

}
