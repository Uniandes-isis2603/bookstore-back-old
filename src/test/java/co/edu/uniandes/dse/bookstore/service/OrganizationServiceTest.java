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

import co.edu.uniandes.dse.bookstore.entities.OrganizationEntity;
import co.edu.uniandes.dse.bookstore.entities.PrizeEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
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

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrizeEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OrganizationEntity").executeUpdate();
	}

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

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	@Test
	void testCreateOrganization() throws BusinessLogicException {
		OrganizationEntity newEntity = factory.manufacturePojo(OrganizationEntity.class);
		OrganizationEntity result = organizationService.createOrganization(newEntity);

		assertNotNull(result);
		OrganizationEntity entity = entityManager.find(OrganizationEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getTipo(), entity.getTipo());
	}

	@Test
	void testCreateOrganizationWithSameName() {
		assertThrows(BusinessLogicException.class, () -> {
			OrganizationEntity newEntity = factory.manufacturePojo(OrganizationEntity.class);
			newEntity.setName(organizationList.get(0).getName());
			organizationService.createOrganization(newEntity);
		});
	}

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

	@Test
	void testGetOrganization() {
		OrganizationEntity entity = organizationList.get(0);
		OrganizationEntity resultEntity = organizationService.getOrganization(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
		assertEquals(entity.getTipo(), resultEntity.getTipo());
	}

	@Test
	void testUpdateOrganization() throws BusinessLogicException {
		OrganizationEntity entity = organizationList.get(0);
		OrganizationEntity pojoEntity = factory.manufacturePojo(OrganizationEntity.class);

		pojoEntity.setId(entity.getId());

		organizationService.updateOrganization(pojoEntity);

		OrganizationEntity resp = entityManager.find(OrganizationEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
		assertEquals(pojoEntity.getTipo(), resp.getTipo());
	}

	@Test
	void testDeleteOrganization() throws BusinessLogicException {
		OrganizationEntity entity = organizationList.get(0);
		organizationService.deleteOrganization(entity.getId());
		OrganizationEntity deleted = entityManager.find(OrganizationEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	@Test
    void testDeleteOrganizationWithPrize() {
        assertThrows(BusinessLogicException.class, ()->{
        	organizationService.deleteOrganization(organizationList.get(2).getId());
        });
    }

}
