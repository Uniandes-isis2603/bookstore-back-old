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

import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.EditorialService;

@RestController
@RequestMapping("/editorials")
public class EditorialController {

	@Autowired
	private EditorialService editorialService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<EditorialEntity> findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		return new ResponseEntity<>(editorialService.getEditorial(id), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<EditorialEntity>> findAll(){
		return new ResponseEntity<>(editorialService.getEditorials(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity <EditorialEntity> create(@RequestBody EditorialEntity editorialEntity) throws IllegalOperationException {
		return new ResponseEntity<>(editorialService.createEditorial(editorialEntity), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity <EditorialEntity> update(@PathVariable("id") Long id, @RequestBody EditorialEntity editorialEntity) throws EntityNotFoundException {
		return new ResponseEntity<>(editorialService.updateEditorial(id, editorialEntity), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		editorialService.deleteEditorial(id);
	}
	
	
}
