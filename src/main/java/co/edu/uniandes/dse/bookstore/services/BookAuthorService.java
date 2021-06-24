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
import co.edu.uniandes.dse.bookstore.repositories.AuthorRepository;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import lombok.Data;

@Data
@Service
public class BookAuthorService {
	
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	public AuthorEntity addAuthor(Long authorId, Long bookId) {
		AuthorEntity authorEntity = authorRepository.findById(authorId).get();
		BookEntity bookEntity = bookRepository.findById(bookId).get();
		
		bookEntity.getAuthors().add(authorEntity);
		
		return authorRepository.findById(authorId).get();
	}
	
	public List<AuthorEntity> getAuthors(Long bookId) {
        return bookRepository.findById(bookId).get().getAuthors();
    }
	
	/*Obtiene el author con id authorId del libro con id bookId*/
	
	public AuthorEntity getAuthor(Long bookId, Long authorId) {
        List<AuthorEntity> authors = bookRepository.findById(bookId).get().getAuthors();
        AuthorEntity authorEntity = authorRepository.findById(authorId).get();
        
        int index = authors.indexOf(authorEntity);
        
        if (index >= 0) {
            return authors.get(index);
        }
        return null;
    }
	
	public List<AuthorEntity> replaceAuthors(Long bookId, List<AuthorEntity> list) {
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        bookEntity.setAuthors(list);
        return bookRepository.findById(bookId).get().getAuthors();
    }
	
	public void removeAuthor(Long bookId, Long authorId) {
        AuthorEntity authorEntity = authorRepository.findById(authorId).get();
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        bookEntity.getAuthors().remove(authorEntity);
    }
}
