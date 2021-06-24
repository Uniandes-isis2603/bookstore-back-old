package co.edu.uniandes.dse.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
	List<BookEntity> findByIsbn(String isbn);
}
