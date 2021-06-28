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

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.OrganizationService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Organizations
 * 
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OrganizationService.class)
class OrganizationServiceTest {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<OrganizationEntity> organizationList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from OrganizationEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			OrganizationEntity entity = factory.manufacturePojo(OrganizationEntity.class);
			entityManager.persist(entity);
			organizationList.add(entity);
		}
		PrizeEntity prize = factory.manufacturePojo(PrizeEntity.class);
		entityManager.persist(prize);
		prize.setOrganization(organizationList.get(2));
		organizationList.get(2).setPrize(prize);
	}

	/**
	 * Prueba para crear una Organization.
	 */
	@Test
	void testCreateOrganization() throws EntityNotFoundException, IllegalOperationException {
		OrganizationEntity newEntity = factory.manufacturePojo(OrganizationEntity.class);
		OrganizationEntity result = organizationService.createOrganization(newEntity);

		assertNotNull(result);
		OrganizationEntity entity = entityManager.find(OrganizationEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getTipo(), entity.getTipo());
	}

	/**
	 * Prueba para crear una Organization con nombre repetido.
	 */
	@Test
	void testCreateOrganizationWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			OrganizationEntity newEntity = factory.manufacturePojo(OrganizationEntity.class);
			newEntity.setName(organizationList.get(0).getName());
			organizationService.createOrganization(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Organizations.
	 */
	@Test
	void testGetOrganizations() {
		List<OrganizationEntity> list = organizationService.getOrganizations();
		assertEquals(organizationList.size(), list.size());

		for (OrganizationEntity entity : list) {
			boolean found = false;
			for (OrganizationEntity storedEntity : organizationList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar una Organization.
	 */
	@Test
	void testGetOrganization() throws EntityNotFoundException {
		OrganizationEntity entity = organizationList.get(0);
		OrganizationEntity resultEntity = organizationService.getOrganization(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
		assertEquals(entity.getTipo(), resultEntity.getTipo());
	}

	/**
	 * Prueba para actualizar un Organization.
	 */
	@Test
	void testUpdateOrganization() throws EntityNotFoundException {
		OrganizationEntity entity = organizationList.get(0);
		OrganizationEntity pojoEntity = factory.manufacturePojo(OrganizationEntity.class);

		pojoEntity.setId(entity.getId());

		organizationService.updateOrganization(entity.getId(), pojoEntity);

		OrganizationEntity resp = entityManager.find(OrganizationEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
		assertEquals(pojoEntity.getTipo(), resp.getTipo());
	}

	/**
	 * Prueba para eliminar un Organization.
	 */
	@Test
	void testDeleteOrganization() throws EntityNotFoundException, IllegalOperationException {
		OrganizationEntity entity = organizationList.get(0);
		organizationService.deleteOrganization(entity.getId());
		OrganizationEntity deleted = entityManager.find(OrganizationEntity.class, entity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Organization con un premio.
	 */
	@Test
	void testDeleteOrganizationWithPrize() {
		assertThrows(IllegalOperationException.class, () -> {
			organizationService.deleteOrganization(organizationList.get(2).getId());
		});
	}

}
