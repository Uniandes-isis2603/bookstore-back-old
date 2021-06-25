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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.Data;

@Data
@Service
public class BookEditorialService {

	@Autowired
    private BookRepository bookRepository;

    @Autowired
    private EditorialRepository editorialRepository;
    
    @Transactional
    public BookEntity replaceEditorial(Long bookId, Long editorialId) throws EntityNotFoundException {
    	
    	BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
		if(bookEntity == null)
			throw new EntityNotFoundException("The book with the given id was not found");
		
		EditorialEntity editorialEntity = editorialRepository.findById(editorialId).orElse(null);
		if(editorialEntity == null)
			throw new EntityNotFoundException("The editorial with the given id was not found");
    	
		bookEntity.setEditorial(editorialEntity);
		return bookEntity;
    }
    
    @Transactional
    public void removeEditorial(Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        EditorialEntity editorialEntity = editorialRepository.findById(bookEntity.getEditorial().getId()).get();
        bookEntity.setEditorial(null);
        editorialEntity.getBooks().remove(bookEntity);
    }
}
