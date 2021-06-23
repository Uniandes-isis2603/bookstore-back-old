package co.edu.uniandes.dse.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.BookRepository;
import co.edu.uniandes.dse.bookstore.repository.EditorialRepository;
import lombok.Data;

@Data
@Service
public class BookService {
	BookRepository bookRepository;
	EditorialRepository editorialRepository;
	
	public BookEntity createBook(BookEntity bookEntity) throws BusinessLogicException {
		if(bookEntity.getEditorial() == null || editorialRepository.findById(bookEntity.getEditorial().getId()) == null) {
			throw new BusinessLogicException("Editorial is not valid");
		}
		
		if(!validateISBN(bookEntity.getIsbn())) {
			throw new BusinessLogicException("ISBN is not valid");
		}
		
		if(bookRepository.findByIsbn(bookEntity.getIsbn()) != null) {
			throw new BusinessLogicException("ISBN already exists");
		}
		
		return this.bookRepository.save(bookEntity);
	}
	
	public List<BookEntity> getBooks(){
		return this.bookRepository.findAll();
	}
	
	public Optional<BookEntity> getBook(Long bookId) {
		return this.bookRepository.findById(bookId);
	}
	
	public BookEntity updateBook(BookEntity bookEntity) throws BusinessLogicException {
		if(!validateISBN(bookEntity.getIsbn())) {
			throw new BusinessLogicException("ISBN is not valid");
		}
		
		return this.bookRepository.save(bookEntity);
	}
	
	public void deleteBook(Long bookId) throws BusinessLogicException {
		List<AuthorEntity> authors = getBook(bookId).get().getAuthors();
		if(authors != null && !authors.isEmpty()) {
			throw new BusinessLogicException("Unable to delete book with id = " + bookId + " because it has associated authors");
		}
	
		this.bookRepository.deleteById(bookId);
	}
	
	private boolean validateISBN(String isbn) {
        return !(isbn == null || isbn.isEmpty());
    }
}
