package co.edu.uniandes.dse.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.EditorialRepository;
import lombok.Data;

@Data
@Service
public class EditorialService {
	EditorialRepository editorialRepository;
	
	public EditorialEntity createEditorial(EditorialEntity editorialEntity) throws BusinessLogicException {
		if(editorialRepository.findByName(editorialEntity.getName()) != null) {
			throw new BusinessLogicException("Editorial name already exists");
		}
		
		return this.editorialRepository.save(editorialEntity);
	}
	
	public List<EditorialEntity> getEditorials(){
		return this.editorialRepository.findAll();
	}
	
	public Optional<EditorialEntity> getEditorial(Long editorialId) {
		return this.editorialRepository.findById(editorialId);
	}
	
	public EditorialEntity updateEditorial(EditorialEntity editorialEntity) throws BusinessLogicException {
		return this.editorialRepository.save(editorialEntity);
	}
	
	public void deleteEditorial(Long editorialId) throws BusinessLogicException {
		List<BookEntity> books = getEditorial(editorialId).get().getBooks();
		if(books != null && !books.isEmpty()) {
			throw new BusinessLogicException("Unable to delete editorial with id = " + editorialId + " because it has associated books");
		}
	
		this.editorialRepository.deleteById(editorialId);
	}
}
