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
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.EditorialRepository;
import lombok.Data;

@Data
@Service
public class EditorialService {
	
	@Autowired
	EditorialRepository editorialRepository;
	
	@Transactional
	public EditorialEntity createEditorial(EditorialEntity editorialEntity) throws IllegalOperationException {
		if(editorialRepository.findByName(editorialEntity.getName()).size() > 0) {
			throw new IllegalOperationException("Editorial name already exists");
		}
		
		return editorialRepository.save(editorialEntity);
	}
	
	@Transactional
	public List<EditorialEntity> getEditorials(){
		return editorialRepository.findAll();
	}
	
	@Transactional
	public EditorialEntity getEditorial(Long editorialId) throws EntityNotFoundException {
		Optional<EditorialEntity> editorial = editorialRepository.findById(editorialId);
		if(editorial.isEmpty()) 
			throw new EntityNotFoundException("The editorial with the given id was not found");
			
		return editorial.get();
	}
	
	@Transactional
	public EditorialEntity updateEditorial(Long editorialId, EditorialEntity editorial) throws EntityNotFoundException {
		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(editorialId);
		if(editorialEntity.isEmpty())
			throw new EntityNotFoundException("The editorial with the given id was not found");
		
		editorial.setId(editorialId);
		return editorialRepository.save(editorial);
	}
	
	@Transactional
	public void deleteEditorial(Long editorialId) throws EntityNotFoundException, IllegalOperationException {
		Optional<EditorialEntity> editorialEntity = editorialRepository.findById(editorialId);
		if(editorialEntity.isEmpty())
			throw new EntityNotFoundException("The editorial with the given id was not found");
		
		List<BookEntity> books = editorialEntity.get().getBooks();
		
		if(books != null && !books.isEmpty()) {
			throw new IllegalOperationException("Unable to delete editorial with id = " + editorialId + " because it has associated books");
		}
	
		editorialRepository.deleteById(editorialId);
	}
}
