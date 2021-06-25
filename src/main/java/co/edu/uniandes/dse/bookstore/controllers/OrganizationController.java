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

import co.edu.uniandes.dse.bookstore.dto.OrganizationDTO;
import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.OrganizationService;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<OrganizationDTO> findAll(){
		List<OrganizationEntity> organizations = organizationService.getOrganizations();
		return modelMapper.map(organizations, new TypeToken<List<OrganizationDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OrganizationDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		OrganizationEntity organizationEntity = organizationService.getOrganization(id);
		return modelMapper.map(organizationEntity, OrganizationDTO.class);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public OrganizationDTO create(@RequestBody OrganizationDTO organizationDTO) throws IllegalOperationException {
		OrganizationEntity organizationEntity = organizationService.createOrganization(modelMapper.map(organizationDTO, OrganizationEntity.class));
		return modelMapper.map(organizationEntity, OrganizationDTO.class);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OrganizationDTO update(@PathVariable("id") Long id, @RequestBody OrganizationDTO organizationDTO) throws EntityNotFoundException {
		OrganizationEntity organizationEntity = organizationService.updateOrganization(id, modelMapper.map(organizationDTO, OrganizationEntity.class));
		return modelMapper.map(organizationEntity, OrganizationDTO.class);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		organizationService.deleteOrganization(id);
	}
	
	
}
