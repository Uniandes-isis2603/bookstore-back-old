package co.edu.uniandes.dse.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.OrganizationRepository;
import co.edu.uniandes.dse.bookstore.repository.PrizeRepository;
import lombok.Data;

@Data
@Service
public class PrizeService {

	PrizeRepository prizeRepository;
	OrganizationRepository organizationRepository;
	
	public PrizeEntity createPrize(PrizeEntity prizeEntity) throws BusinessLogicException {
		if(prizeEntity.getOrganization() == null) {
			throw new BusinessLogicException("Organization is not valid");
		}
		
		Optional<OrganizationEntity> organizationEntity = organizationRepository.findById(prizeEntity.getOrganization().getId());
		if(!organizationEntity.isPresent()) {
			throw new BusinessLogicException("Organization is not valid");
		}
		
		if(organizationEntity.get().getPrize() != null) {
			throw new BusinessLogicException("Organization already holds a prize");
		}
		
		return this.prizeRepository.save(prizeEntity);
	}
	
	public List<PrizeEntity> getPrizes(){
		return this.prizeRepository.findAll();
	}
	
	public Optional<PrizeEntity> getPrize(Long prizeId) {
		return this.prizeRepository.findById(prizeId);
	}
	
	public PrizeEntity updatePrize(PrizeEntity prizeEntity) throws BusinessLogicException {
		return this.prizeRepository.save(prizeEntity);
	}
	
	public void deletePrize(Long prizeId) throws BusinessLogicException {
		if(prizeRepository.findById(prizeId).get().getAuthor() != null ) {
			throw new BusinessLogicException("Unable to delete prize with id = " + prizeId + " because it has an associated author");
		}
	
		this.prizeRepository.deleteById(prizeId);
	}
}
