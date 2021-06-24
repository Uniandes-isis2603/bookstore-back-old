package co.edu.uniandes.dse.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repository.BookRepository;
import lombok.Data;

@Data
@Service
public class AuthorBookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	public BookEntity addBook(Long authorId, Long bookId) {
		AuthorEntity authorEntity = authorRepository.findById(authorId).get();
		BookEntity bookEntity = bookRepository.findById(bookId).get();
		
		bookEntity.getAuthors().add(authorEntity);
		
		bookRepository.save(bookEntity);
		return bookRepository.findById(bookId).get();
	}
	
	public List<BookEntity> getBooks(Long authorId) {
        return authorRepository.findById(authorId).get().getBooks();
    }
	
	public BookEntity getBook(Long authorId, Long bookId) throws BusinessLogicException {
        /*List<BookEntity> books = authorRepository.findById(authorId).get().getBooks();
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        int index = books.indexOf(bookEntity);
        
        if (index >= 0) {
            return books.get(index);
        }
        throw new BusinessLogicException("El libro no está asociado al autor");*/
		BookEntity book =  bookRepository.getById(bookId);
		AuthorEntity author = authorRepository.getById(authorId);
		
		List<AuthorEntity> authors = book.getAuthors();
		
		int index = authors.indexOf(author);
        
        if (index >= 0) {
        	return book;
        }
        throw new BusinessLogicException("El libro no está asociado al autor");
    }
	
	public List<BookEntity> replaceBooks(Long authorId, List<BookEntity> books) {
        AuthorEntity authorEntity = authorRepository.findById(authorId).get();
        List<BookEntity> bookList = bookRepository.findAll();
        for (BookEntity book : bookList) {
            if (books.contains(book)) {
                if (!book.getAuthors().contains(authorEntity)) {
                    book.getAuthors().add(authorEntity);
                }
            } else {
                book.getAuthors().remove(authorEntity);
            }
        }
        authorEntity.setBooks(books);
        return authorEntity.getBooks();
    }
	
	public void removeBook(Long authorId, Long bookId) {
        AuthorEntity authorEntity = authorRepository.findById(authorId).get();
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        bookEntity.getAuthors().remove(authorEntity);
    }
}
