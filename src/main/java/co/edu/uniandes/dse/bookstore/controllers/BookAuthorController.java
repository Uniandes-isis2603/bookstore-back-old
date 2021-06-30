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

/**
 * Clase que implementa el recurso "books/{id}/authors".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/books")
public class BookAuthorController {

	@Autowired
	private BookAuthorService bookAuthorService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia un autor existente con un libro existente
	 *
	 * @param authorId El ID del autor que se va a asociar
	 * @param bookId   El ID del libro al cual se le va a asociar el autor
	 * @return JSON {@link AuthorDetailDTO} - El autor asociado.
	 */
	@PostMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO addAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException {
		AuthorEntity authorEntity = bookAuthorService.addAuthor(bookId, authorId);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}

	/**
	 * Busca y devuelve el autor con el ID recibido en la URL, relativo a un libro.
	 *
	 * @param authorId El ID del autor que se busca
	 * @param bookId   El ID del libro del cual se busca el autor
	 * @return {@link AuthorDetailDTO} - El autor encontrado en el libro.
	 */
	@GetMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO getAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException, IllegalOperationException {
		AuthorEntity authorEntity = bookAuthorService.getAuthor(bookId, authorId);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}

	/**
	 * Actualiza la lista de autores de un libro con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param bookId  El ID del libro al cual se le va a asociar la lista de autores
	 * @param authors JSONArray {@link AuthorDTO} - La lista de autores que se desea
	 *                guardar.
	 * @return JSONArray {@link AuthorDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{bookId}/authors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> addAuthors(@PathVariable("bookId") Long bookId, @RequestBody List<AuthorDTO> authors)
			throws EntityNotFoundException {
		List<AuthorEntity> entities = modelMapper.map(authors, new TypeToken<List<AuthorEntity>>() {
		}.getType());
		List<AuthorEntity> authorsList = bookAuthorService.replaceAuthors(bookId, entities);
		return modelMapper.map(authorsList, new TypeToken<List<AuthorDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve todos los autores que existen en un libro.
	 *
	 * @param booksd El ID del libro del cual se buscan los autores
	 * @return JSONArray {@link AuthorDetailDTO} - Los autores encontrados en el
	 *         libro. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{bookId}/authors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> getAuthors(@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		List<AuthorEntity> authorEntity = bookAuthorService.getAuthors(bookId);
		return modelMapper.map(authorEntity, new TypeToken<List<AuthorDetailDTO>>() {
		}.getType());
	}

	/**
	 * Elimina la conexión entre el autor y el libro recibidos en la URL.
	 *
	 * @param bookId   El ID del libro al cual se le va a desasociar el autor
	 * @param authorId El ID del autor que se desasocia
	 */
	@DeleteMapping(value = "/{bookId}/authors/{authorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException {
		bookAuthorService.removeAuthor(bookId, authorId);
	}
}
