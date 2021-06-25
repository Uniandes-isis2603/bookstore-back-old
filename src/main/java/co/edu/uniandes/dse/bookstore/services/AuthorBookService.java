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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import lombok.Data;

@Data
@Service
public class AuthorBookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Transactional
	public BookEntity addBook(Long authorId, Long bookId) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
		
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
		
		if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		bookEntity.getAuthors().add(authorEntity);
		
		return bookEntity;
	}
	
	@Transactional
	public List<BookEntity> getBooks(Long authorId) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
		
		List<BookEntity> books = bookRepository.findAll();
		List<BookEntity> bookList = new ArrayList<>();
		for(BookEntity b : books) {
			if(b.getAuthors().indexOf(authorEntity) >= 0) {
				bookList.add(b);
			}
		}
        return bookList;
    }
	
	/*Obtiene el libro con id bookId del author con id authorId*/
	
	@Transactional
	public BookEntity getBook(Long authorId, Long bookId) throws EntityNotFoundException, IllegalOperationException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
		
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
		
		if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		if(bookEntity.getAuthors().contains(authorEntity))
			return bookEntity;
		
		throw new IllegalOperationException("The book is not associated to the author");
    }
	
	@Transactional
	public List<BookEntity> addBooks(Long authorId, List<BookEntity> books) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
				
		for (BookEntity book: books) {
			
			BookEntity bookEntity = bookRepository.findById(book.getId()).orElse(null);
			if(bookEntity == null)
				throw new EntityNotFoundException("The book with the given id was not found");
			
			if(!bookEntity.getAuthors().contains(authorEntity))
				bookEntity.getAuthors().add(authorEntity);
		}
		
		return books;
    }
	
	@Transactional
	public void removeBook(Long authorId, Long bookId) throws EntityNotFoundException {
        AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
        if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
				
        BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
        if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
        
        bookEntity.getAuthors().remove(authorEntity);
    }
}
