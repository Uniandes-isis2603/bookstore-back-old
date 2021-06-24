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

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
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

	public BookEntity addBook(Long authorId, Long bookId) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
		
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
		
		if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		bookEntity.getAuthors().add(authorEntity);
		
		return bookRepository.findById(bookId).orElse(null);
	}
	
	public List<BookEntity> getBooks(Long authorId) {
		AuthorEntity author = authorRepository.findById(authorId).get();
		List<BookEntity> books = bookRepository.findAll();
		List<BookEntity> bookList = new ArrayList<>();
		for(BookEntity b : books) {
			if(b.getAuthors().indexOf(author) >= 0) {
				bookList.add(b);
			}
		}
        return bookList;
    }
	
	/*Obtiene el libro con id bookId del author con id authorId*/
	
	public BookEntity getBook(Long authorId, Long bookId) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorRepository.findById(authorId).orElse(null);
		BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
		
		if(authorEntity == null)
			throw new EntityNotFoundException("The author with the given id was not found");
		
		if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		if(bookEntity.getAuthors().contains(authorEntity))
			return bookEntity;
		
		throw new EntityNotFoundException("The book is not associated to the author");
		
		/*List<AuthorEntity> authors = bookEntity.getAuthors();
				
		int index = authors.indexOf(authorEntity);
        
        if (index >= 0)
        	return bookEntity;*/
       
        //throw new EntityNotFoundException("The book is not associated to the author");
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
