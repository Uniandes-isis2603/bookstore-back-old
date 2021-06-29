/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
	public List<AuthorDetailDTO> addAuthors(@PathVariable("bookId") Long bookId, @RequestBody List<AuthorDTO> authors) throws EntityNotFoundException {
		List<AuthorEntity> entities = modelMapper.map(authors, new TypeToken<List<AuthorEntity>>() {}.getType());
		List<AuthorEntity> authorsList = bookAuthorService.replaceAuthors(bookId, entities);
		return modelMapper.map(authorsList, new TypeToken<List<AuthorDetailDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{bookId}/authors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> getAuthors(@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		List<AuthorEntity> authorEntity = bookAuthorService.getAuthors(bookId);
		return modelMapper.map(authorEntity, new TypeToken<List<AuthorDetailDTO>>() {}.getType());
	}
	
	@DeleteMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		bookAuthorService.removeAuthor(bookId, authorId);
	}
}
