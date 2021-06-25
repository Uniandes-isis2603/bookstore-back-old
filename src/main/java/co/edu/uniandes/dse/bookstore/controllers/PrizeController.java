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
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.PrizeService;

@RestController
@RequestMapping("/prizes")
public class PrizeController {
	
	@Autowired
	private PrizeService prizeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrizeDTO> findAll(){
		List<PrizeEntity> prizes = prizeService.getPrizes();
		return modelMapper.map(prizes, new TypeToken<List<PrizeDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrizeDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		PrizeEntity prizeEntity = prizeService.getPrize(id);
		return modelMapper.map(prizeEntity, PrizeDTO.class);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PrizeDTO create(@RequestBody PrizeDTO prizeDTO) throws EntityNotFoundException, IllegalOperationException {
		PrizeEntity prizeEntity = prizeService.createPrize(modelMapper.map(prizeDTO, PrizeEntity.class));
		return modelMapper.map(prizeEntity, PrizeDTO.class);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrizeDTO update(@PathVariable("id") Long id, @RequestBody PrizeDTO prizeDTO) throws EntityNotFoundException {
		PrizeEntity prizeEntity = prizeService.updatePrize(id, modelMapper.map(prizeDTO, PrizeEntity.class));
		return modelMapper.map(prizeEntity, PrizeDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		prizeService.deletePrize(id);
	}
	
	
}
