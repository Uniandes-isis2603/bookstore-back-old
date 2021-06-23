package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class AuthorServiceTest1 {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private AuthorService authorService;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<AuthorEntity> data = new ArrayList<>();
	
	public void insertData() {
		for (int i = 0; i < 3; i++) {
			AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
			authorEntity.setId(null);
			entityManager.persist(authorEntity);
			data.add(authorEntity);
		}

		/*AuthorEntity authorEntity = data.get(2);
		BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
		bookEntity.getAuthors().add(authorEntity);
		entityManager.persist(bookEntity);
		authorEntity.getBooks().add(bookEntity);

		PrizeEntity prize = factory.manufacturePojo(PrizeEntity.class);
		prize.setAuthor(data.get(1));
		entityManager.persist(prize);
		data.get(1).getPrizes().add(prize);*/
	}
	
	@BeforeEach
	public void setUp() {
		System.out.println("Hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		for (int i = 0; i < 3; i++) {
			AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
			authorEntity.setId(null);
			this.entityManager.persist(authorEntity);
			data.add(authorEntity);
		}
	}
	
	@Test
	public void test() {
		System.out.println("testing");
		assertTrue(true);
	}
	
	@Test
	void testCreateAuthor() {
		AuthorEntity newEntity = factory.manufacturePojo(AuthorEntity.class);
		AuthorEntity result = authorService.createAuthor(newEntity);
		assertNotNull(result);

		AuthorEntity authorEntity = entityManager.find(AuthorEntity.class, result.getId());

		assertEquals(authorEntity.getId(), result.getId());
		assertEquals(authorEntity.getName(), result.getName());
		assertEquals(authorEntity.getBirthDate(), result.getBirthDate());
		assertEquals(authorEntity.getDescription(), result.getDescription());
	}
}
