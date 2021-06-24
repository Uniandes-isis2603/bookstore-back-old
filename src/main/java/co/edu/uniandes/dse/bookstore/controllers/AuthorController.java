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
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorEntity> findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		return new ResponseEntity<>(authorService.getAuthor(id), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<AuthorEntity>> findAll(){
		return new ResponseEntity<>(authorService.getAuthors(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity <AuthorEntity> create(@RequestBody AuthorEntity authorEntity) {
		return new ResponseEntity<>(authorService.createAuthor(authorEntity), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity <AuthorEntity> update(@PathVariable("id") Long id, @RequestBody AuthorEntity authorEntity) throws EntityNotFoundException {
		return new ResponseEntity<>(authorService.updateAuthor(id, authorEntity), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		authorService.deleteAuthor(id);
	}
	
	
}
