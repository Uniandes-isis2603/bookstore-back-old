package co.edu.uniandes.dse.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
	ReviewEntity findByBookIdAndId(Long bookId, Long id);
}
