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
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.OrganizationService;
import co.edu.uniandes.dse.bookstore.services.PrizeService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@Import({ PrizeService.class, OrganizationService.class })

class PrizeServiceTest {

	@Autowired
	private PrizeService prizeService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<OrganizationEntity> organizationList = new ArrayList<>();
	private List<PrizeEntity> prizeList = new ArrayList<PrizeEntity>();

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrizeEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PrizeEntity entity = factory.manufacturePojo(PrizeEntity.class);
			OrganizationEntity orgEntity = factory.manufacturePojo(OrganizationEntity.class);
			entityManager.persist(orgEntity);
			entity.setOrganization(orgEntity);
			orgEntity.setPrize(entity);
			entityManager.persist(entity);
			prizeList.add(entity);
			organizationList.add(orgEntity);
		}

		AuthorEntity author = factory.manufacturePojo(AuthorEntity.class);
		entityManager.persist(author);
		author.getPrizes().add(prizeList.get(2));
		prizeList.get(2).setAuthor(author);
	}

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	@Test
	void testCreatePrize() throws EntityNotFoundException, IllegalOperationException {
		PrizeEntity newEntity = factory.manufacturePojo(PrizeEntity.class);
		OrganizationEntity newOrgEntity = factory.manufacturePojo(OrganizationEntity.class);

		newOrgEntity = organizationService.createOrganization(newOrgEntity);
		newEntity.setOrganization(newOrgEntity);
		PrizeEntity result = prizeService.createPrize(newEntity);
		assertNotNull(result);
		PrizeEntity entity = entityManager.find(PrizeEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
		assertEquals(newEntity.getDescription(), entity.getDescription());
		assertEquals(newEntity.getPremiationDate(), entity.getPremiationDate());
	}

	@Test
	void testCreatePrizeWithNoValidOrganization() {
		assertThrows(IllegalOperationException.class, () -> {
			PrizeEntity newEntity = factory.manufacturePojo(PrizeEntity.class);
			newEntity.setOrganization(null);
			prizeService.createPrize(newEntity);
		});
	}

	@Test
	void testCreatePrizeWithNoValidOrganization2() throws EntityNotFoundException {
		assertThrows(IllegalOperationException.class, () -> {
			PrizeEntity newEntity = factory.manufacturePojo(PrizeEntity.class);
			newEntity.setOrganization(organizationList.get(0));
			prizeService.createPrize(newEntity);
		});
	}

	@Test
	void testGetPrizes() {
		List<PrizeEntity> list = prizeService.getPrizes();
		assertEquals(prizeList.size(), list.size());
		for (PrizeEntity entity : list) {
			boolean found = false;
			for (PrizeEntity storedEntity : prizeList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	@Test
	void testGetPrize() throws EntityNotFoundException {
		PrizeEntity entity = prizeList.get(0);
		PrizeEntity resultEntity = prizeService.getPrize(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getName(), resultEntity.getName());
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getDescription(), resultEntity.getDescription());
		assertEquals(entity.getPremiationDate(), resultEntity.getPremiationDate());
	}

	@Test
	void testUpdatePrize() throws EntityNotFoundException {
		PrizeEntity entity = prizeList.get(0);
		PrizeEntity pojoEntity = factory.manufacturePojo(PrizeEntity.class);

		pojoEntity.setId(entity.getId());

		prizeService.updatePrize(entity.getId(), pojoEntity);

		PrizeEntity resp = entityManager.find(PrizeEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
		assertEquals(pojoEntity.getDescription(), resp.getDescription());
		assertEquals(pojoEntity.getPremiationDate(), resp.getPremiationDate());
	}

	@Test
	void testDeletePrize() {
		assertThrows(IllegalOperationException.class, ()->{
			 PrizeEntity entity = prizeList.get(2);
		     prizeService.deletePrize(entity.getId());
		});
	}

}
