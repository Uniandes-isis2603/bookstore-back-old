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

package co.edu.uniandes.dse.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.Data;

@Data
@Service
public class BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	EditorialRepository editorialRepository;
	
	public BookEntity createBook(BookEntity bookEntity) throws EntityNotFoundException, IllegalOperationException {
		EditorialEntity editorial = editorialRepository.findById(bookEntity.getEditorial().getId()).orElse(null);
		if(bookEntity.getEditorial()==null || editorial == null)
			throw new IllegalOperationException("Editorial is not valid");
		
		if(!validateISBN(bookEntity.getIsbn()))
			throw new IllegalOperationException("ISBN is not valid");
		
		if(bookRepository.findByIsbn(bookEntity.getIsbn()).size() > 0 )
			throw new IllegalOperationException("ISBN already exists");
		
		return bookRepository.save(bookEntity);
	}
	
	public List<BookEntity> getBooks(){
		return bookRepository.findAll();
	}
	
	public BookEntity getBook(Long bookId) throws EntityNotFoundException {
		BookEntity book = bookRepository.findById(bookId).orElse(null);
		if(book == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		return book;
	}
	
	public BookEntity updateBook(Long bookId, BookEntity bookEntity) throws EntityNotFoundException, IllegalOperationException {
		BookEntity book = bookRepository.findById(bookId).orElse(null);
		if(book == null)
			throw new EntityNotFoundException("The book with the given id was not found");
	
		
		if(!validateISBN(bookEntity.getIsbn()))
			throw new IllegalOperationException("ISBN is not valid");
	
		bookEntity.setId(bookId);
		return bookRepository.save(bookEntity);
	}
	
	public void deleteBook(Long bookId) throws EntityNotFoundException {
		BookEntity book = bookRepository.findById(bookId).orElse(null);
		if(book == null)
			throw new EntityNotFoundException("The book with the given id was not found");
	
		List<AuthorEntity> authors = getBook(bookId).getAuthors();
		
		if(authors != null && !authors.isEmpty())
			throw new EntityNotFoundException("Unable to delete book because it has associated authors");
	
		bookRepository.deleteById(bookId);
	}
	
	private boolean validateISBN(String isbn) {
        return !(isbn == null || isbn.isEmpty());
    }
}
