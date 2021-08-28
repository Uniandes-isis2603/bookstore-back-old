package co.edu.uniandes.dse.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.UsuarioEntity;
import co.edu.uniandes.dse.bookstore.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Transactional
	public List<UsuarioEntity> getUsuarios() {
		return usuarioRepository.findAll();
	}

	@Transactional
	public UsuarioEntity getPrimerUsuario() {
		List<UsuarioEntity> usuarios = usuarioRepository.findAll();
		if( usuarios.size() > 0 ) {
			return usuarios.get( 0 );
		}
		return null;
	}
}