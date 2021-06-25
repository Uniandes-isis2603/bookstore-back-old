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
import co.edu.uniandes.dse.bookstore.services.BookAuthorService;

@RestController
@RequestMapping("/books")
public class BookAuthorController {

	@Autowired
	private BookAuthorService bookAuthorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO addAuthor(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		AuthorEntity authorEntity = bookAuthorService.addAuthor(bookId, authorId);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}
	
	@GetMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO getAuthor(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		AuthorEntity authorEntity = bookAuthorService.getAuthor(bookId, authorId);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}
	
	@PutMapping(value = "/{bookId}/authors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> addAuthors(@PathVariable("bookId") Long bookId, @RequestBody List<AuthorDTO> authors) throws EntityNotFoundException, IllegalOperationException {
		List<AuthorEntity> entities = modelMapper.map(authors, new TypeToken<List<AuthorEntity>>() {}.getType());
		List<AuthorEntity> authorsList = bookAuthorService.addAuthors(bookId, entities);
		return modelMapper.map(authorsList, new TypeToken<List<AuthorDetailDTO>>() {}.getType());
		
	}
	
	@GetMapping(value = "/{bookId}/authors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> getAuthors(@PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		List<AuthorEntity> authorEntity = bookAuthorService.getAuthors(bookId);
		return modelMapper.map(authorEntity, new TypeToken<List<AuthorDetailDTO>>() {}.getType());
	}
	
	@DeleteMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		bookAuthorService.removeAuthor(bookId, authorId);
	}
}
