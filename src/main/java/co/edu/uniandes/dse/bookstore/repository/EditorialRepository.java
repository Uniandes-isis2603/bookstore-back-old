package co.edu.uniandes.dse.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;

public interface EditorialRepository extends JpaRepository<EditorialEntity, Long> {
	List<EditorialRepository> findByName(String name);
}
