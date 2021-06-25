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
import co.edu.uniandes.dse.bookstore.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> findAll(){
		List<BookEntity> books = bookService.getBooks();
		return modelMapper.map(books, new TypeToken<List<BookDetailDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		BookEntity bookEntity = bookService.getBook(id);
		return modelMapper.map(bookEntity,BookDetailDTO.class);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO bookDTO) throws IllegalOperationException, EntityNotFoundException {
		BookEntity bookEntity = bookService.createBook(modelMapper.map(bookDTO, BookEntity.class));
		return modelMapper.map(bookEntity, BookDTO.class);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDTO update(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = bookService.updateBook(id, modelMapper.map(bookDTO, BookEntity.class));
		return modelMapper.map(bookEntity, BookDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		bookService.deleteBook(id);
	}
}
