package co.edu.uniandes.dse.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;

@Repository
public interface EditorialRepository extends JpaRepository<EditorialEntity, Long> {
	List<EditorialEntity> findByName(String name);
}
