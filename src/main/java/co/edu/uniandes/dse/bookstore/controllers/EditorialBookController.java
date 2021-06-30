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

/**
 * Clase que implementa el recurso "editorials/{id}/books".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/editorials")
public class EditorialBookController {

	@Autowired
	private EditorialBookService editorialBookService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un libro dentro de una editorial con la informacion que recibe el la
	 * URL. Se devuelve el libro que se guarda en la editorial.
	 *
	 * @param editorialId Identificador de la editorial que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param bookId      Identificador del libro que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link BookDTO} - El libro guardado en la editorial.
	 */
	@PostMapping(value = "/{editorialId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDTO addBook(@PathVariable("editorialId") Long editorialId, @PathVariable("bookId") Long booklId)
			throws EntityNotFoundException {
		BookEntity bookEntity = editorialBookService.addBook(booklId, editorialId);
		return modelMapper.map(bookEntity, BookDTO.class);
	}

	/**
	 * Busca y devuelve todos los libros que existen en la editorial.
	 *
	 * @param editorialId Identificador de la editorial que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link BookDetailDTO} - Los libros encontrados en la
	 *         editorial. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{editorialId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> getBooks(@PathVariable("editorialId") Long editorialId) throws EntityNotFoundException {
		List<BookEntity> bookList = editorialBookService.getBooks(editorialId);
		return modelMapper.map(bookList, new TypeToken<List<BookDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el libro con el id asociado dentro de la editorial con id asociado.
	 *
	 * @param editorialId Identificador de la editorial que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param bookId      Identificador del libro que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link BookDetailDTO} - El libro buscado
	 */
	@GetMapping(value = "/{editorialId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO getBook(@PathVariable("editorialId") Long editorialId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = editorialBookService.getBook(editorialId, bookId);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Book asociadas a una instancia de Editorial
	 *
	 * @param editorialId Identificador de la editorial que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param books       JSONArray {@link BookDTO} El arreglo de libros nuevo para
	 *                    la editorial.
	 * @return JSON {@link BookDetailDTO} - El arreglo de libros guardado en la
	 *         editorial.
	 */
	@PutMapping(value = "/{editorialId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> replaceBooks(@PathVariable("editorialId") Long editorialsId,
			@RequestBody List<BookDetailDTO> books) throws EntityNotFoundException {
		List<BookEntity> booksList = modelMapper.map(books, new TypeToken<List<BookEntity>>() {
		}.getType());
		List<BookEntity> result = editorialBookService.replaceBooks(editorialsId, booksList);
		return modelMapper.map(result, new TypeToken<List<BookDetailDTO>>() {
		}.getType());
	}
}
