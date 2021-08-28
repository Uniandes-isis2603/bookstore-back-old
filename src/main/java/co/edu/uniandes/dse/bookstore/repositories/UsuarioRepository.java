package co.edu.uniandes.dse.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.bookstore.entities.UsuarioEntity;



public interface UsuarioRepository  extends JpaRepository<UsuarioEntity, Long>{

}
