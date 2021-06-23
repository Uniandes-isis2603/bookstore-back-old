package co.edu.uniandes.dse.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.AuthorRepository;
import lombok.Data;

@Data
@Service
public class AuthorService {
	@Autowired
	AuthorRepository authorRepository;
	
	public AuthorEntity createAuthor(AuthorEntity authorEntity) {
		return this.authorRepository.save(authorEntity);
	}
	
	public List<AuthorEntity> getAuthors(){
		return this.authorRepository.findAll();
	}
	
	public Optional<AuthorEntity> getAuthor(Long authorId) {
		return this.authorRepository.findById(authorId);
	}
	
	public AuthorEntity updateAuthor(AuthorEntity authorEntity) {
		return this.authorRepository.save(authorEntity);
	}
	
	public void deleteAuthor(Long authorId) throws BusinessLogicException {
		List<BookEntity> books = getAuthor(authorId).get().getBooks();
		if(books != null && !books.isEmpty()) {
			throw new BusinessLogicException("Unable to delete author with id = " + authorId + " because it has associated books");
		}
		
		List<PrizeEntity> prizes = getAuthor(authorId).get().getPrizes();
		if(prizes != null && !prizes.isEmpty()) {
			throw new BusinessLogicException("Unable to delete author with id = " + authorId + " because it has associated prizes");
		}
		this.authorRepository.deleteById(authorId);
	}
	
}
