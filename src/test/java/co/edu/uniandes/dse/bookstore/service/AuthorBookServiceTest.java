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
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorBookService;
import co.edu.uniandes.dse.bookstore.services.BookService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import({ AuthorBookService.class, BookService.class })
class AuthorBookServiceTest {

	@Autowired
	private AuthorBookService authorBookService;

	@Autowired
	private BookService bookService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private AuthorEntity author = new AuthorEntity();
	private EditorialEntity editorial = new EditorialEntity();
	private List<BookEntity> bookList = new ArrayList<>();

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from AuthorEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
	}

	private void insertData() {
		editorial = factory.manufacturePojo(EditorialEntity.class);
		entityManager.persist(editorial);

		author = factory.manufacturePojo(AuthorEntity.class);
		author.setBooks(new ArrayList<>());
		entityManager.persist(author);

		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setEditorial(editorial);
			entity.setAuthors(new ArrayList<>());
			entity.getAuthors().add(author);
			entityManager.persist(entity);
			bookList.add(entity);
			//author.getBooks().add(entity);
		}
	}

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	@Test
	void testAddBook() throws EntityNotFoundException, IllegalOperationException {
		BookEntity newBook = factory.manufacturePojo(BookEntity.class);
		newBook.setEditorial(editorial);
		bookService.createBook(newBook);

		BookEntity bookEntity = authorBookService.addBook(author.getId(), newBook.getId());
		assertNotNull(bookEntity);

		assertEquals(bookEntity.getId(), newBook.getId());
		assertEquals(bookEntity.getName(), newBook.getName());
		assertEquals(bookEntity.getDescription(), newBook.getDescription());
		assertEquals(bookEntity.getIsbn(), newBook.getIsbn());
		assertEquals(bookEntity.getImage(), newBook.getImage());

		BookEntity lastBook = authorBookService.getBook(author.getId(), newBook.getId());

		assertEquals(lastBook.getId(), newBook.getId());
		assertEquals(lastBook.getName(), newBook.getName());
		assertEquals(lastBook.getDescription(), newBook.getDescription());
		assertEquals(lastBook.getIsbn(), newBook.getIsbn());
		assertEquals(lastBook.getImage(), newBook.getImage());

	}

	@Test
	void testGetBooks() {
		List<BookEntity> bookEntities = authorBookService.getBooks(author.getId());

		assertEquals(bookList.size(), bookEntities.size());

		for (int i = 0; i < bookList.size(); i++) {
			assertTrue(bookEntities.contains(bookList.get(0)));
		}
	}

	@Test
	void testGetBook() throws EntityNotFoundException {
		BookEntity bookEntity = bookList.get(0);
		BookEntity book = authorBookService.getBook(author.getId(), bookEntity.getId());
		assertNotNull(book);

		assertEquals(bookEntity.getId(), book.getId());
		assertEquals(bookEntity.getName(), book.getName());
		assertEquals(bookEntity.getDescription(), book.getDescription());
		assertEquals(bookEntity.getIsbn(), book.getIsbn());
		assertEquals(bookEntity.getImage(), book.getImage());
	}

	@Test
	void testReplaceBooks() throws EntityNotFoundException, IllegalOperationException {
		List<BookEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			BookEntity entity = factory.manufacturePojo(BookEntity.class);
			entity.setAuthors(new ArrayList<>());
			entity.getAuthors().add(author);
			entity.setEditorial(editorial);
			bookService.createBook(entity);
			nuevaLista.add(entity);
		}
		authorBookService.replaceBooks(author.getId(), nuevaLista);
		List<BookEntity> bookEntities = authorBookService.getBooks(author.getId());
		for (BookEntity aNuevaLista : nuevaLista) {
			assertTrue(bookEntities.contains(aNuevaLista));
		}
	}

	@Test
	void testRemoveBook() {
		for (BookEntity book : bookList) {
			authorBookService.removeBook(author.getId(), book.getId());
		}
		assertTrue(authorBookService.getBooks(author.getId()).isEmpty());
	}

}
