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
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import(EditorialService.class)
class EditorialServiceTest {

	@Autowired
	private EditorialService editorialService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EditorialEntity> editorialList = new ArrayList<>();

	private List<BookEntity> bookList = new ArrayList<>();

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from BookEntity");
		entityManager.getEntityManager().createQuery("delete from EditorialEntity");
	}

	private void insertData() {
		
		for (int i = 0; i < 3; i++) {
			EditorialEntity editorialEntity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(editorialEntity);
			editorialList.add(editorialEntity);
		}
		
		for (int i = 0; i < 3; i++) {
			BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
			entityManager.persist(bookEntity);
			bookList.add(bookEntity);
		}
		bookList.get(0).setEditorial(editorialList.get(0));
		editorialList.get(0).getBooks().add(bookList.get(0));
	}

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	@Test
	void testCreateEditorial() throws BusinessLogicException {
		EditorialEntity newEntity = factory.manufacturePojo(EditorialEntity.class);
		EditorialEntity result = editorialService.createEditorial(newEntity);
		assertNotNull(result);

		EditorialEntity entity = entityManager.find(EditorialEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
	}

	@Test
	void testCreateEditorialWithSameName() {
		assertThrows(BusinessLogicException.class, () -> {
			EditorialEntity newEntity = factory.manufacturePojo(EditorialEntity.class);
			newEntity.setName(editorialList.get(0).getName());
			editorialService.createEditorial(newEntity);
		});
	}

	@Test
	void testGetEditorials() {
		List<EditorialEntity> list = editorialService.getEditorials();
		assertEquals(editorialList.size(), list.size());
		for (EditorialEntity entity : list) {
			boolean found = false;
			for (EditorialEntity storedEntity : editorialList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	@Test
	void testGetEditorial() {
		EditorialEntity entity = editorialList.get(0);
		EditorialEntity resultEntity = editorialService.getEditorial(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
	}

	@Test
	void testUpdateEditorial() throws BusinessLogicException {
		EditorialEntity entity = editorialList.get(0);
		EditorialEntity pojoEntity = factory.manufacturePojo(EditorialEntity.class);
		pojoEntity.setId(entity.getId());
		editorialService.updateEditorial(pojoEntity);
		EditorialEntity resp = entityManager.find(EditorialEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
	}

	@Test
	void testDeleteEditorial() throws BusinessLogicException {
		EditorialEntity entity = editorialList.get(1);
		editorialService.deleteEditorial(entity.getId());
		EditorialEntity deleted = entityManager.find(EditorialEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	@Test
    void testDeleteEditorialWithBooks() {
        assertThrows(BusinessLogicException.class, ()-> {
        	EditorialEntity entity = editorialList.get(0);
            editorialService.deleteEditorial(entity.getId());
        });
    }
}
