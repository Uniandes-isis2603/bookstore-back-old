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
import co.edu.uniandes.dse.bookstore.services.AuthorService;

/**
 * Clase que implementa el recurso "authors".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorService authorService;

	private final ModelMapper modelMapper;
	
	@Autowired
	public AuthorController(AuthorService authorService, ModelMapper modelMapper){
		this.authorService = authorService;
		this.modelMapper = modelMapper;
	}

	/**
	 * Busca y devuelve todos los autores que existen en la aplicacion.
	 *
	 * @return JSONArray {@link AuthorDetailDTO} - Los autores encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<AuthorDetailDTO> findAll() {
		List<AuthorEntity> authors = authorService.getAuthors();
		return modelMapper.map(authors, new TypeToken<List<AuthorDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el autor con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del autor que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link AuthorDetailDTO} - El autor buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		AuthorEntity authorEntity = authorService.getAuthor(id);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
	}

	/**
	 * Crea un nuevo autor con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param authorDTO {@link AuthorDTO} - EL autor que se desea guardar.
	 * @return JSON {@link AuthorDTO} - El autor guardado con el atributo id
	 *         autogenerado.
	 * @throws IllegalOperationException 
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public AuthorDTO create(@RequestBody AuthorDTO authorDTO) throws IllegalOperationException {
		AuthorEntity authorEntity = authorService.createAuthor(modelMapper.map(authorDTO, AuthorEntity.class));
		return modelMapper.map(authorEntity, AuthorDTO.class);
	}

	/**
	 * Actualiza el autor con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id     Identificador del autor que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param author {@link AuthorDTO} El autor que se desea guardar.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public AuthorDTO update(@PathVariable("id") Long id, @RequestBody AuthorDTO authorDTO)
			throws EntityNotFoundException {
		AuthorEntity authorEntity = authorService.updateAuthor(id, modelMapper.map(authorDTO, AuthorEntity.class));
		return modelMapper.map(authorEntity, AuthorDTO.class);
	}

	/**
	 * Borra el autor con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del autor que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		authorService.deleteAuthor(id);
	}

}
