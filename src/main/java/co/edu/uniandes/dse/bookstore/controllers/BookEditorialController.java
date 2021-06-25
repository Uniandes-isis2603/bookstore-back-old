package co.edu.uniandes.dse.bookstore.controllers;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import co.edu.uniandes.dse.bookstore.dto.BookDetailDTO;
import co.edu.uniandes.dse.bookstore.dto.EditorialDTO;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.BookEditorialService;

@RestController
@RequestMapping("/books")
public class BookEditorialController {

	@Autowired
	private BookEditorialService bookEditorialService; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PutMapping(value = "/{bookId}/editorial")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO replaceEditorial(@PathVariable("bookId") Long bookId, @RequestBody EditorialDTO editorialDTO ) throws EntityNotFoundException{
		BookEntity bookEntity =  bookEditorialService.replaceEditorial(bookId, editorialDTO.getId());
		return modelMapper.map(bookEntity, BookDetailDTO.class);	
	}
		
}
