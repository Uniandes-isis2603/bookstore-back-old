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
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
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

	private List<BookEntity> bookList = new ArrayList<>();
	private List<EditorialEntity> editorialList = new ArrayList<>();

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from BookEntity");
		entityManager.getEntityManager().createQuery("delete from EditorialEntity");
		entityManager.getEntityManager().createQuery("delete from AuthorEntity");
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EditorialEntity editorialEntity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(editorialEntity);
			editorialList.add(editorialEntity);
		}

		for (int i = 0; i < 3; i++) {
			BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
			bookEntity.setEditorial(editorialList.get(0));
			entityManager.persist(bookEntity);
			bookList.add(bookEntity);
		}

		AuthorEntity authorEntity = factory.manufacturePojo(AuthorEntity.class);
		entityManager.persist(authorEntity);
		authorEntity.getBooks().add(bookList.get(0));
		bookList.get(0).getAuthors().add(authorEntity);
	}

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	@Test
	void testCreateBook() throws BusinessLogicException {
		BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
		newEntity.setEditorial(editorialList.get(0));
		BookEntity result = bookService.createBook(newEntity);
		assertNotNull(result);

		BookEntity entity = entityManager.find(BookEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getDescription(), entity.getDescription());
		assertEquals(newEntity.getImage(), entity.getImage());
		assertEquals(newEntity.getPublishDate(), entity.getPublishDate());
		assertEquals(newEntity.getIsbn(), entity.getIsbn());
	}

	@Test
	void testCreateBookWithNoValidISBN() {
		assertThrows(BusinessLogicException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn("");
			bookService.createBook(newEntity);
		});
	}

	@Test
	void testCreateBookWithNoValidISBN2() {
		assertThrows(BusinessLogicException.class, () -> {
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn(null);
			bookService.createBook(newEntity);
		});
	}

	@Test
	void testCreateBookWithStoredISBN() {
		assertThrows(BusinessLogicException.class, ()->{
			BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
			newEntity.setEditorial(editorialList.get(0));
			newEntity.setIsbn(bookList.get(0).getIsbn());
			bookService.createBook(newEntity);
		});
	}
	
	@Test
    void testCreateBookWithNullEditorial() {
        assertThrows(BusinessLogicException.class, ()->{
        	BookEntity newEntity = factory.manufacturePojo(BookEntity.class);
            newEntity.setEditorial(null);
            bookService.createBook(newEntity);
        });
    }

	@Test
	void testGetBooks() {
		List<BookEntity> list = bookService.getBooks();
        assertEquals(bookList.size(), list.size());
        for (BookEntity entity : list) {
            boolean found = false;
            for (BookEntity storedEntity : bookList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
	}

	@Test
	void testGetBook() {
		BookEntity entity = bookList.get(0);
        BookEntity resultEntity = bookService.getBook(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getDescription(), resultEntity.getDescription());
        assertEquals(entity.getIsbn(), resultEntity.getIsbn());
        assertEquals(entity.getImage(), resultEntity.getImage());
	}

	@Test
	void testUpdateBook() throws BusinessLogicException {
		 BookEntity entity = bookList.get(0);
	        BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
	        pojoEntity.setId(entity.getId());
	        bookService.updateBook(pojoEntity);
	        BookEntity resp = entityManager.find(BookEntity.class, entity.getId());
	        assertEquals(pojoEntity.getId(), resp.getId());
	        assertEquals(pojoEntity.getName(), resp.getName());
	        assertEquals(pojoEntity.getDescription(), resp.getDescription());
	        assertEquals(pojoEntity.getIsbn(), resp.getIsbn());
	        assertEquals(pojoEntity.getImage(), resp.getImage());
	}

	@Test
    void testUpdateBookWithNoValidISBN() {
        assertThrows(BusinessLogicException.class, ()->{
        	BookEntity entity = bookList.get(0);
            BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
            pojoEntity.setIsbn("");
            pojoEntity.setId(entity.getId());
            bookService.updateBook(pojoEntity);
        });
    }
	
	@Test
    void testUpdateBookWithNoValidISBN2(){
        assertThrows(BusinessLogicException.class, ()->{
        	BookEntity entity = bookList.get(0);
            BookEntity pojoEntity = factory.manufacturePojo(BookEntity.class);
            pojoEntity.setIsbn(null);
            pojoEntity.setId(entity.getId());
            bookService.updateBook(pojoEntity);
        });
    }
	
	@Test
	void testDeleteBook() throws BusinessLogicException {
		BookEntity entity = bookList.get(1);
        bookService.deleteBook(entity.getId());
        BookEntity deleted = entityManager.find(BookEntity.class, entity.getId());
        assertNull(deleted);
	}
	
	@Test
    void testDeleteBookWithAuthor() {
        assertThrows(BusinessLogicException.class, ()->{
        	BookEntity entity = bookList.get(0);
            bookService.deleteBook(entity.getId());
        });
    }

}
