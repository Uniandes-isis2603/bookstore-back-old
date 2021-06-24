package co.edu.uniandes.dse.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;

@Repository
public interface PrizeRepository extends JpaRepository<PrizeEntity, Long> {

}
