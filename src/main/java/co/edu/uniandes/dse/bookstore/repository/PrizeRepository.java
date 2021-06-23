package co.edu.uniandes.dse.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;

public interface PrizeRepository extends JpaRepository<PrizeEntity, Long> {

}
