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

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.OrganizationRepository;
import lombok.Data;

@Data
@Service
public class OrganizationService {
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	public OrganizationEntity createOrganization(OrganizationEntity organizationEntity) throws EntityNotFoundException {
		if(organizationRepository.findByName(organizationEntity.getName()).size() > 0) {
			throw new EntityNotFoundException("Organization name already exists");
		}
		
		return this.organizationRepository.save(organizationEntity);
	}
	
	public List<OrganizationEntity> getOrganizations(){
		return this.organizationRepository.findAll();
	}
	
	public OrganizationEntity getOrganization(Long organizationId) {
		return this.organizationRepository.findById(organizationId).get();
	}
	
	public OrganizationEntity updateOrganization(OrganizationEntity organizationEntity) throws EntityNotFoundException {
		return this.organizationRepository.save(organizationEntity);
	}
	
	public void deleteOrganization(Long organizationId) throws EntityNotFoundException {
		PrizeEntity prize = getOrganization(organizationId).getPrize();
		if(prize != null) {
			throw new EntityNotFoundException("Unable to delete organization with id = " + organizationId + " because it has an associated prize");
		}
	
		this.organizationRepository.deleteById(organizationId);
	}
}
