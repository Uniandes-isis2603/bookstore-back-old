package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import co.edu.uniandes.dse.bookstore.entities.AuthorEntity;
import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.PrizeAuthorService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Prize - Author
 *
 * @author ISIS2603
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import(PrizeAuthorService.class)
class PrizeAuthorServiceTest {

	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private PrizeAuthorService prizeAuthorService;

	@Autowired
	private TestEntityManager entityManager;

	private List<AuthorEntity> authorsList = new ArrayList<>();
	private List<PrizeEntity> prizesList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrizeEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OrganizationEntity ").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from AuthorEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PrizeEntity prizes = factory.manufacturePojo(PrizeEntity.class);
			OrganizationEntity org = factory.manufacturePojo(OrganizationEntity.class);

			prizes.setOrganization(org);
			org.setPrize(prizes);

			entityManager.persist(org);
			entityManager.persist(prizes);
			prizesList.add(prizes);
		}
		for (int i = 0; i < 3; i++) {
			AuthorEntity entity = factory.manufacturePojo(AuthorEntity.class);
			entityManager.persist(entity);
			authorsList.add(entity);
			if (i == 0) {
				prizesList.get(i).setAuthor(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Prizes existente a un Author.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddAuthor() throws EntityNotFoundException {
		AuthorEntity entity = authorsList.get(0);
		PrizeEntity prizeEntity = prizesList.get(1);
		AuthorEntity response = prizeAuthorService.addAuthor(entity.getId(), prizeEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para consultar un Author.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetAuthor() throws EntityNotFoundException {
		PrizeEntity entity = prizesList.get(0);
		AuthorEntity resultEntity = prizeAuthorService.getAuthor(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getAuthor().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para remplazar las instancias de Prizes asociadas a una instancia de
	 * Author.
	 * @throws EntityNotFoundException 
	 */
	@Test
	void testReplaceAuthor() throws EntityNotFoundException {
		AuthorEntity entity = authorsList.get(0);
		prizeAuthorService.replaceAuthor(prizesList.get(1).getId(), entity.getId());
		entity = prizeAuthorService.getAuthor(prizesList.get(1).getId());
		assertTrue(entity.getPrizes().contains(prizesList.get(1)));
	}

	@Test
	void testRemoveAuthor() {
		fail("Not yet implemented");
	}

}
