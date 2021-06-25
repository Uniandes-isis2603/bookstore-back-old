package co.edu.uniandes.dse.bookstore.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.dto.AuthorDTO;
import co.edu.uniandes.dse.bookstore.dto.AuthorDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.PrizeAuthorService;

@RestController
@RequestMapping("/prizes")
public class PrizeAuthorController {
	
	@Autowired
	private PrizeAuthorService prizeAuthorService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@PostMapping(value = "/{prizeId}/author/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
    public AuthorDTO addAuthor(@PathVariable("prizeId") Long prizeId, @PathVariable("authorId") Long authorId) throws EntityNotFoundException {
        AuthorEntity authorEntity = prizeAuthorService.addAuthor(authorId, prizeId);
        return modelMapper.map(authorEntity, AuthorDTO.class);
    }
	
	@GetMapping(value = "/{prizeId}/author")
	@ResponseStatus(code = HttpStatus.OK)
    public AuthorDetailDTO getAuthor(@PathVariable("prizeId") Long prizeId) throws EntityNotFoundException {
        AuthorEntity authorEntity = prizeAuthorService.getAuthor(prizeId);
        return modelMapper.map(authorEntity, AuthorDetailDTO.class);
    }
	
	@PutMapping(value = "/{prizeId}/author/{authorId}")
	@ResponseStatus(code = HttpStatus.OK)
    public AuthorDetailDTO replaceAuthor(@PathVariable("prizeId") Long prizeId, @PathVariable("authorId") Long authorId) throws EntityNotFoundException {
        AuthorEntity authorEntity = prizeAuthorService.replaceAuthor(prizeId, authorId);
		return modelMapper.map(authorEntity, AuthorDetailDTO.class);
    }
	
	@DeleteMapping(value = "/{prizeId}/author")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("prizeId") Long prizeId) throws EntityNotFoundException {
        prizeAuthorService.removeAuthor(prizeId);
    }
	
}
