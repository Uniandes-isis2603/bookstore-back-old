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
import co.edu.uniandes.dse.bookstore.services.BookService;

/**
 * Clase que implementa el recurso "books".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos los libros que existen en la aplicacion.
	 *
	 * @return JSONArray {@link BookDetailDTO} - Los libros encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<BookDetailDTO> findAll() {
		List<BookEntity> books = bookService.getBooks();
		return modelMapper.map(books, new TypeToken<List<BookDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el libro con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param bookId Identificador del libro que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link BookDetailDTO} - El libro buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		BookEntity bookEntity = bookService.getBook(id);
		return modelMapper.map(bookEntity, BookDetailDTO.class);
	}

	/**
	 * Crea un nuevo libro con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param book {@link BookDTO} - EL libro que se desea guardar.
	 * @return JSON {@link BookDTO} - El libro guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO bookDTO) throws IllegalOperationException, EntityNotFoundException {
		BookEntity bookEntity = bookService.createBook(modelMapper.map(bookDTO, BookEntity.class));
		return modelMapper.map(bookEntity, BookDTO.class);
	}

	/**
	 * Actualiza el libro con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param bookId Identificador del libro que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param book   {@link BookDTO} El libro que se desea guardar.
	 * @return JSON {@link BookDTO} - El libro guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BookDTO update(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO)
			throws EntityNotFoundException, IllegalOperationException {
		BookEntity bookEntity = bookService.updateBook(id, modelMapper.map(bookDTO, BookEntity.class));
		return modelMapper.map(bookEntity, BookDTO.class);
	}

	/**
	 * Borra el libro con el id asociado recibido en la URL.
	 *
	 * @param bookId Identificador del libro que se desea borrar. Este debe ser una
	 *               cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		bookService.deleteBook(id);
	}
}
