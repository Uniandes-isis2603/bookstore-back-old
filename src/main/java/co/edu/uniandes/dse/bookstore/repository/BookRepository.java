package co.edu.uniandes.dse.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
	List<BookRepository> findByIsbn(String isbn);
}
