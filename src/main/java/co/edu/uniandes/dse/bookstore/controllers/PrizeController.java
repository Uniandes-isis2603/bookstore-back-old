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

import co.edu.uniandes.dse.bookstore.dto.PrizeDTO;
import co.edu.uniandes.dse.bookstore.dto.PrizeDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.PrizeService;

/**
 * Clase que implementa el recurso "prizes".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/prizes")
public class PrizeController {

	@Autowired
	private PrizeService prizeService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos los premios que existen en la aplicacion.
	 *
	 * @return JSONArray {@link PrizeDetailDTO} - Los premios encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrizeDetailDTO> findAll() {
		List<PrizeEntity> prizes = prizeService.getPrizes();
		return modelMapper.map(prizes, new TypeToken<List<PrizeDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el premio con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del premio que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link PrizeDetailDTO} - El premio buscado
	 * @throws WebApplicationException {@link WebApplicationExceptionMapper} - Error
	 *                                 de lógica que se genera cuando no se
	 *                                 encuentra el premio.
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrizeDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		PrizeEntity prizeEntity = prizeService.getPrize(id);
		return modelMapper.map(prizeEntity, PrizeDetailDTO.class);
	}

	/**
	 * Crea un nuevo premio con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param prize {@link PrizeDTO} - EL premio que se desea guardar.
	 * @return JSON {@link PrizeDTO} - El premio guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PrizeDTO create(@RequestBody PrizeDTO prizeDTO) throws IllegalOperationException {
		PrizeEntity prizeEntity = prizeService.createPrize(modelMapper.map(prizeDTO, PrizeEntity.class));
		return modelMapper.map(prizeEntity, PrizeDTO.class);
	}

	/**
	 * Actualiza el premio con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id    Identificador del premio que se desea actualizar. Este debe ser
	 *              una cadena de dígitos.
	 * @param prize {@link PrizeDTO} El premio que se desea guardar.
	 * @return JSON {@link PrizeDetailDTO} - El premio guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrizeDetailDTO update(@PathVariable("id") Long id, @RequestBody PrizeDTO prizeDTO)
			throws EntityNotFoundException {
		PrizeEntity prizeEntity = prizeService.updatePrize(id, modelMapper.map(prizeDTO, PrizeEntity.class));
		return modelMapper.map(prizeEntity, PrizeDetailDTO.class);
	}

	/**
	 * Borra el premio con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del premio que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		prizeService.deletePrize(id);
	}

}
