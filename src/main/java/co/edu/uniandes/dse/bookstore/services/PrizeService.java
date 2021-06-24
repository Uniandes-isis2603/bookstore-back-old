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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
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
	
	public PrizeEntity createPrize(PrizeEntity prizeEntity) throws EntityNotFoundException {
		if(prizeEntity.getOrganization() == null) {
			throw new EntityNotFoundException("Organization is not valid");
		}
		
		OrganizationEntity organizationEntity = organizationRepository.findById(prizeEntity.getOrganization().getId()).get();
		if(organizationEntity == null) {
			throw new EntityNotFoundException("Organization is not valid");
		}
		
		if(organizationEntity.getPrize() != null) {
			throw new EntityNotFoundException("Organization already holds a prize");
		}
		
		return this.prizeRepository.save(prizeEntity);
	}
	
	public List<PrizeEntity> getPrizes(){
		return this.prizeRepository.findAll();
	}
	
	public Optional<PrizeEntity> getPrize(Long prizeId) {
		return this.prizeRepository.findById(prizeId);
	}
	
	public PrizeEntity updatePrize(PrizeEntity prizeEntity) throws EntityNotFoundException {
		return this.prizeRepository.save(prizeEntity);
	}
	
	public void deletePrize(Long prizeId) throws EntityNotFoundException {
		if(prizeRepository.findById(prizeId).get().getAuthor() != null ) {
			throw new EntityNotFoundException("Unable to delete prize with id = " + prizeId + " because it has an associated author");
		}
	
		this.prizeRepository.deleteById(prizeId);
	}
}
