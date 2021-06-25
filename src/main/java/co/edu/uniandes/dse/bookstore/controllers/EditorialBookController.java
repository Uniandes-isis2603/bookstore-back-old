package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import co.edu.uniandes.dse.bookstore.services.EditorialBookService;

@RestController
@RequestMapping("/editorials")
public class EditorialBookController {

	@Autowired
	private EditorialBookService editorialBookService; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping(value = "/{editorialId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDTO addBook(@PathVariable("editorialId") Long editorialId, @PathVariable("bookId") Long booklId) throws EntityNotFoundException{
		BookEntity bookEntity = editorialBookService.addBook(booklId, editorialId);
		return modelMapper.map(bookEntity, BookDTO.class);
	}
	
	@GetMapping(value = "/{editorialId}/books")
	@ResponseStatus(code = HttpStatus.OK)
    public List<BookDetailDTO> getBooks(@PathVariable("editorialId") Long editorialId) throws EntityNotFoundException {
		List<BookEntity> bookList = editorialBookService.getBooks(editorialId);
		return modelMapper.map(bookList, new TypeToken<List<BookDetailDTO>>() {}.getType());
    }
	
	@GetMapping(value = "/{editorialId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
    public BookDetailDTO getBook(@PathVariable("editorialId") Long editorialId, @PathVariable("bookId") Long bookId) throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = editorialBookService.getBook(editorialId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
    }
	
	@PutMapping(value = "/{editorialId}/books")
	@ResponseStatus(code = HttpStatus.OK)
    public List<BookDetailDTO> replaceBooks(@PathVariable("editorialId") Long editorialsId, @RequestBody List<BookDetailDTO> books) throws EntityNotFoundException {
        List<BookEntity> booksList = modelMapper.map(books, new TypeToken<List<BookEntity>>() {}.getType());
        List<BookEntity> result = editorialBookService.replaceBooks(editorialsId, booksList);
        return modelMapper.map(result, new TypeToken<List<BookDetailDTO>>() {}.getType());
    }
}
