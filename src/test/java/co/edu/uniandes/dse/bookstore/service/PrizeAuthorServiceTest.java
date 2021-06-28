/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
@ExtendWith(SpringExtension.class)
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
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceAuthor() throws EntityNotFoundException {
		AuthorEntity entity = authorsList.get(0);
		prizeAuthorService.replaceAuthor(prizesList.get(1).getId(), entity.getId());

		PrizeEntity prize = entityManager.find(PrizeEntity.class, prizesList.get(1).getId());
		assertTrue(prize.getAuthor().equals(entity));
	}

	/**
	 * Prueba para desasociar un Prize existente de un Author existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemovePrize() throws EntityNotFoundException {
		prizeAuthorService.removeAuthor(prizesList.get(0).getId());
		PrizeEntity prize = entityManager.find(PrizeEntity.class, prizesList.get(0).getId());
		assertNull(prize.getAuthor());
	}

	/**
	 * Prueba para desasociar un Prize existente de un Author existente.
	 */
	@Test
	void testRemoveAuthor() {
		assertThrows(EntityNotFoundException.class, () -> {
			prizeAuthorService.removeAuthor(prizesList.get(1).getId());
		});
	}

}
