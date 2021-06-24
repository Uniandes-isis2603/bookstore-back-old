package co.edu.uniandes.dse.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.OrganizationRepository;
import lombok.Data;

@Data
@Service
public class OrganizationService {
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	public OrganizationEntity createOrganization(OrganizationEntity organizationEntity) throws BusinessLogicException {
		if(organizationRepository.findByName(organizationEntity.getName()).size() > 0) {
			throw new BusinessLogicException("Organization name already exists");
		}
		
		return this.organizationRepository.save(organizationEntity);
	}
	
	public List<OrganizationEntity> getOrganizations(){
		return this.organizationRepository.findAll();
	}
	
	public OrganizationEntity getOrganization(Long organizationId) {
		return this.organizationRepository.findById(organizationId).get();
	}
	
	public OrganizationEntity updateOrganization(OrganizationEntity organizationEntity) throws BusinessLogicException {
		return this.organizationRepository.save(organizationEntity);
	}
	
	public void deleteOrganization(Long organizationId) throws BusinessLogicException {
		PrizeEntity prize = getOrganization(organizationId).getPrize();
		if(prize != null) {
			throw new BusinessLogicException("Unable to delete organization with id = " + organizationId + " because it has an associated prize");
		}
	
		this.organizationRepository.deleteById(organizationId);
	}
}
