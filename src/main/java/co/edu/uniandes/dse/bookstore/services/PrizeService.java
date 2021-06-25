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

package co.edu.uniandes.dse.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.OrganizationRepository;
import co.edu.uniandes.dse.bookstore.repositories.PrizeRepository;
import lombok.Data;

@Data
@Service
public class PrizeService {

	@Autowired
	PrizeRepository prizeRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Transactional
	public PrizeEntity createPrize(PrizeEntity prizeEntity) throws EntityNotFoundException, IllegalOperationException {
		if(prizeEntity.getOrganization() == null) 
			throw new IllegalOperationException("Organization is not valid");
		
		OrganizationEntity organizationEntity = organizationRepository.findById(prizeEntity.getOrganization().getId()).orElse(null);
		if(organizationEntity == null)
			throw new IllegalOperationException("Organization is not valid");
		
		
		if(organizationEntity.getPrize() != null)
			throw new IllegalOperationException("Organization already holds a prize");
		
		
		return prizeRepository.save(prizeEntity);
	}
	
	@Transactional
	public List<PrizeEntity> getPrizes(){
		return prizeRepository.findAll();
	}
	
	@Transactional
	public PrizeEntity getPrize(Long prizeId) throws EntityNotFoundException {
		PrizeEntity prizeEntity = prizeRepository.findById(prizeId).orElse(null);
		if(prizeEntity == null)
			throw new EntityNotFoundException("The prize with the given id was not found");
		
		
		return prizeEntity;
	}
	
	@Transactional
	public PrizeEntity updatePrize(Long prizeId, PrizeEntity prizeEntity) throws EntityNotFoundException {
		PrizeEntity prize = prizeRepository.findById(prizeId).orElse(null);
		if(prize == null)
			throw new EntityNotFoundException("The prize with the given id was not found");
		
		prizeEntity.setId(prize.getId());
		return prizeRepository.save(prizeEntity);
	}
	
	@Transactional
	public void deletePrize(Long prizeId) throws EntityNotFoundException, IllegalOperationException {
		PrizeEntity prize = prizeRepository.findById(prizeId).orElse(null);
		if(prize == null)
			throw new EntityNotFoundException("The prize with the given id was not found");
		
		if(prizeRepository.findById(prizeId).get().getAuthor() != null ) {
			throw new IllegalOperationException("Unable to delete prize because it has an associated author");
		}
	
		prizeRepository.deleteById(prizeId);
	}
}
