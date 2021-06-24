package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService; 
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<BookEntity> findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<BookEntity>> findAll(){
		return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity <BookEntity> create(@RequestBody BookEntity bookEntity) throws IllegalOperationException, EntityNotFoundException {
		return new ResponseEntity<>(bookService.createBook(bookEntity), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity <BookEntity> update(@PathVariable("id") Long id, @RequestBody BookEntity bookEntity) throws EntityNotFoundException, IllegalOperationException {
		return new ResponseEntity<>(bookService.updateBook(id, bookEntity), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		bookService.deleteBook(id);
	}
	
	
}
