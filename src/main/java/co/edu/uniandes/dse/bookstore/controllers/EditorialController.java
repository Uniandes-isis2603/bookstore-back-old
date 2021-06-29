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

import co.edu.uniandes.dse.bookstore.dto.EditorialDTO;
import co.edu.uniandes.dse.bookstore.dto.EditorialDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.EditorialService;

@RestController
@RequestMapping("/editorials")
public class EditorialController {

	@Autowired
	private EditorialService editorialService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public EditorialDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		EditorialEntity editorialEntity = editorialService.getEditorial(id);
		return modelMapper.map(editorialEntity, EditorialDetailDTO.class);
	}
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<EditorialDetailDTO> findAll(){
		List<EditorialEntity> editorials = editorialService.getEditorials();
		return modelMapper.map(editorials, new TypeToken<List<EditorialDetailDTO>>() {}.getType());
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public EditorialDTO create(@RequestBody EditorialDTO editorialDTO) throws IllegalOperationException {
		EditorialEntity editorialEntity = editorialService.createEditorial(modelMapper.map(editorialDTO, EditorialEntity.class));
		return modelMapper.map(editorialEntity, EditorialDTO.class);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public EditorialDTO update(@PathVariable("id") Long id, @RequestBody EditorialDTO editorialDTO) throws EntityNotFoundException {
		EditorialEntity editorialEntity = editorialService.updateEditorial(id, modelMapper.map(editorialDTO, EditorialEntity.class));
		return modelMapper.map(editorialEntity, EditorialDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		editorialService.deleteEditorial(id);
	}
}
