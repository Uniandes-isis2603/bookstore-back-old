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

import co.edu.uniandes.dse.bookstore.dto.BookDTO;
import co.edu.uniandes.dse.bookstore.dto.BookDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.AuthorBookService;

/**
 * Clase que implementa el recurso "authors/{id}/books".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/authors")
public class AuthorBookController {

	@Autowired
	private AuthorBookService authorBookService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve el libro con el ID recibido en la URL, relativo a un autor.
	 *
	 * @param authorId El ID del autor del cual se busca el libro
	 * @param bookId   El ID del libro que se busca
	 * @return {@link BookDetailDTO} - El libro encontrado en el autor.
	 */
	@GetMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO getBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = authorBookService.getBook(authorId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}

	/**
	 * Busca y devuelve todos los libros que existen en un autor.
	 *
	 * @param authorsId El ID del autor del cual se buscan los libros
	 * @return JSONArray {@link BookDetailDTO} - Los libros encontrados en el autor.
	 *         Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{authorId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> getBooks(@PathVariable("authorId") Long authorId) throws EntityNotFoundException {
		List<BookEntity> bookEntity = authorBookService.getBooks(authorId);
		return modelMapper.map(bookEntity, new TypeToken<List<BookDetailDTO>>() {
		}.getType());
	}

	/**
	 * Asocia un libro existente con un autor existente
	 *
	 * @param authorId El ID del autor al cual se le va a asociar el libro
	 * @param bookId   El ID del libro que se asocia
	 * @return JSON {@link BookDetailDTO} - El libro asociado.
	 */
	@PostMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO addBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException {
		BookEntity bookEntity = authorBookService.addBook(authorId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}

	/**
	 * Actualiza la lista de libros de un autor con la lista que se recibe en el
	 * cuerpo
	 *
	 * @param authorId El ID del autor al cual se le va a asociar el libro
	 * @param books    JSONArray {@link BookDTO} - La lista de libros que se desea
	 *                 guardar.
	 * @return JSONArray {@link BookDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{authorId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> replaceBooks(@PathVariable("authorId") Long authorId, @RequestBody List<BookDTO> books)
			throws EntityNotFoundException {
		List<BookEntity> entities = modelMapper.map(books, new TypeToken<List<BookEntity>>() {
		}.getType());
		List<BookEntity> booksList = authorBookService.addBooks(authorId, entities);
		return modelMapper.map(booksList, new TypeToken<List<BookDetailDTO>>() {
		}.getType());

	}

	/**
	 * Elimina la conexión entre el libro y e autor recibidos en la URL.
	 *
	 * @param authorId El ID del autor al cual se le va a desasociar el libro
	 * @param bookId   El ID del libro que se desasocia
	 */
	@DeleteMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException {
		authorBookService.removeBook(authorId, bookId);
	}
}
