package co.edu.uniandes.dse.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.EditorialRepository;
import lombok.Data;

@Data
@Service
public class EditorialService {
	
	@Autowired
	EditorialRepository editorialRepository;
	
	public EditorialEntity createEditorial(EditorialEntity editorialEntity) throws BusinessLogicException {
		if(editorialRepository.findByName(editorialEntity.getName()).size() > 0) {
			throw new BusinessLogicException("Editorial name already exists");
		}
		
		return this.editorialRepository.save(editorialEntity);
	}
	
	public List<EditorialEntity> getEditorials(){
		return this.editorialRepository.findAll();
	}
	
	public EditorialEntity getEditorial(Long editorialId) {
		return this.editorialRepository.findById(editorialId).get();
	}
	
	public EditorialEntity updateEditorial(EditorialEntity editorialEntity) throws BusinessLogicException {
		return this.editorialRepository.save(editorialEntity);
	}
	
	public void deleteEditorial(Long editorialId) throws BusinessLogicException {
		List<BookEntity> books = getEditorial(editorialId).getBooks();
		if(books != null && !books.isEmpty()) {
			throw new BusinessLogicException("Unable to delete editorial with id = " + editorialId + " because it has associated books");
		}
	
		this.editorialRepository.deleteById(editorialId);
	}
}
