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

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorBookService;

@RestController
@RequestMapping("/authors")
public class AuthorBookController {

	@Autowired
	private AuthorBookService authorBookService;
	
	@PostMapping(value = "/{authorId}/books/{bookId}")
	public ResponseEntity<BookEntity> addBook(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		return new ResponseEntity<>(authorBookService.addBook(authorId, bookId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{authorId}/books/{bookId}")
	public ResponseEntity<BookEntity> getBook(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		return new ResponseEntity<>(authorBookService.getBook(authorId, bookId), HttpStatus.OK);
	}
}
