package co.edu.uniandes.dse.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

}
