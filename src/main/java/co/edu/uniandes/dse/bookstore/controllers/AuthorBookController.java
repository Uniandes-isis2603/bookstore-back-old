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

import co.edu.uniandes.dse.bookstore.dto.BookDTO;
import co.edu.uniandes.dse.bookstore.dto.BookDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorBookService;

@RestController
@RequestMapping("/authors")
public class AuthorBookController {

	@Autowired
	private AuthorBookService authorBookService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO addBook(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		BookEntity bookEntity = authorBookService.addBook(authorId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}
	
	@GetMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO getBook(@PathVariable("authorId") Long authorId, 
			@PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = authorBookService.getBook(authorId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}
	
	@PutMapping(value = "/{authorId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> replaceBooks(@PathVariable("authorId") Long authorId, @RequestBody List<BookDTO> books) throws EntityNotFoundException, IllegalOperationException {
		List<BookEntity> entities = modelMapper.map(books, new TypeToken<List<BookEntity>>() {}.getType());
		List<BookEntity> booksList = authorBookService.addBooks(authorId, entities);
		return modelMapper.map(booksList, new TypeToken<List<BookDetailDTO>>() {}.getType());
		
	}
	
	@GetMapping(value = "/{authorId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> getBooks(@PathVariable("authorId") Long authorId) throws EntityNotFoundException, IllegalOperationException {
		List<BookEntity> bookEntity = authorBookService.getBooks(authorId);
		return modelMapper.map(bookEntity, new TypeToken<List<BookDetailDTO>>() {}.getType());
	}
	
	@DeleteMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		authorBookService.removeBook(authorId, bookId);
	}
}
