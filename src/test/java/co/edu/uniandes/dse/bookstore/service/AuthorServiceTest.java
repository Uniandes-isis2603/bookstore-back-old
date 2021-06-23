package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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

	private List<AuthorEntity> data = new ArrayList<>();

	void insertData() {
		
	}

	@BeforeEach
	public void setUp() {
		insertData();
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

	@Test
	void testGetAuthors() {
		List<AuthorEntity> authorsList = authorService.getAuthors();
		assertEquals(data.size(), authorsList.size());

		for (AuthorEntity authorEntity : authorsList) {
			boolean found = false;
			for (AuthorEntity storedEntity : data) {
				if (authorEntity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	@Test
	void testGetAuthor() {
		AuthorEntity authorEntity = data.get(0);

		Optional<AuthorEntity> resultEntity = authorService.getAuthor(authorEntity.getId());
		assertNotNull(resultEntity);

		assertEquals(authorEntity.getId(), resultEntity.get().getId());
		assertEquals(authorEntity.getName(), resultEntity.get().getName());
		assertEquals(authorEntity.getBirthDate(), resultEntity.get().getBirthDate());
		assertEquals(authorEntity.getDescription(), resultEntity.get().getDescription());
	}

	@Test
	void testUpdateAuthor() {
		AuthorEntity authorEntity = data.get(0);
		AuthorEntity pojoEntity = factory.manufacturePojo(AuthorEntity.class);

		pojoEntity.setId(authorEntity.getId());

		authorService.updateAuthor(pojoEntity);

		AuthorEntity response = entityManager.find(AuthorEntity.class, authorEntity.getId());

		assertEquals(pojoEntity.getId(), response.getId());
		assertEquals(pojoEntity.getName(), response.getName());
		assertEquals(pojoEntity.getBirthDate(), response.getBirthDate());
		assertEquals(pojoEntity.getDescription(), response.getDescription());
	}

	@Test
	void testDeleteAuthor() throws BusinessLogicException {
		AuthorEntity authorEntity = data.get(0);
		authorService.deleteAuthor(authorEntity.getId());
		AuthorEntity deleted = entityManager.find(AuthorEntity.class, authorEntity.getId());
		assertNull(deleted);
	}

	@Test
	public void testDeleteAuthorWithBooks() {
		assertThrows(BusinessLogicException.class, ()-> {
			authorService.deleteAuthor(data.get(2).getId());
		});
	}
	
	@Test
    public void testDeleteAuthorWithPrize() {
		assertThrows(BusinessLogicException.class, ()->{
			authorService.deleteAuthor(data.get(1).getId());
		});
    }

}
