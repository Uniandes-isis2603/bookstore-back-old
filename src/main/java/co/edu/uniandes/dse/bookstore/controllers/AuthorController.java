package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.dto.AuthorDTO;
import co.edu.uniandes.dse.bookstore.dto.AuthorDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> findAll(){
		List<AuthorEntity> authors = authorService.getAuthors();
		return modelMapper.map(authors, new TypeToken<List<AuthorDetailDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorService.getAuthor(id);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public AuthorDTO create(@RequestBody AuthorDTO authorDTO) {
		AuthorEntity authorEntity = authorService.createAuthor(modelMapper.map(authorDTO, AuthorEntity.class));
		return modelMapper.map(authorEntity, AuthorDTO.class);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDTO update(@PathVariable("id") Long id, @RequestBody AuthorDTO authorDTO) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorService.updateAuthor(id, modelMapper.map(authorDTO, AuthorEntity.class));
		return modelMapper.map(authorEntity, AuthorDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		authorService.deleteAuthor(id);
	}
	
	
}
