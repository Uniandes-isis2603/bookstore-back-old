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

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.EditorialEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.EditorialService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Editorials
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(EditorialService.class)
class EditorialServiceTest {

	@Autowired
	private EditorialService editorialService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EditorialEntity> editorialList = new ArrayList<>();

	private List<BookEntity> bookList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from BookEntity");
		entityManager.getEntityManager().createQuery("delete from EditorialEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		for (int i = 0; i < 3; i++) {
			EditorialEntity editorialEntity = factory.manufacturePojo(EditorialEntity.class);
			entityManager.persist(editorialEntity);
			editorialList.add(editorialEntity);
		}

		for (int i = 0; i < 3; i++) {
			BookEntity bookEntity = factory.manufacturePojo(BookEntity.class);
			entityManager.persist(bookEntity);
			bookList.add(bookEntity);
		}
		bookList.get(0).setEditorial(editorialList.get(0));
		editorialList.get(0).getBooks().add(bookList.get(0));
	}

	/**
	 * Prueba para crear un Editorial.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testCreateEditorial() throws EntityNotFoundException, IllegalOperationException {
		EditorialEntity newEntity = factory.manufacturePojo(EditorialEntity.class);
		EditorialEntity result = editorialService.createEditorial(newEntity);
		assertNotNull(result);

		EditorialEntity entity = entityManager.find(EditorialEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getName(), entity.getName());
	}

	/**
	 * Prueba para crear un Editorial con el mismo nombre de un Editorial que ya
	 * existe.
	 *
	 * @throws IllegalOperationException
	 */
	@Test
	void testCreateEditorialWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			EditorialEntity newEntity = factory.manufacturePojo(EditorialEntity.class);
			newEntity.setName(editorialList.get(0).getName());
			editorialService.createEditorial(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Editorials.
	 */
	@Test
	void testGetEditorials() {
		List<EditorialEntity> list = editorialService.getEditorials();
		assertEquals(editorialList.size(), list.size());
		for (EditorialEntity entity : list) {
			boolean found = false;
			for (EditorialEntity storedEntity : editorialList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar un Editorial.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetEditorial() throws EntityNotFoundException {
		EditorialEntity entity = editorialList.get(0);
		EditorialEntity resultEntity = editorialService.getEditorial(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getName(), resultEntity.getName());
	}
	
	/**
	 * Prueba para consultar un Editorial que no existe.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetEditorialInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			editorialService.getEditorial(0L);
		});
	}

	/**
	 * Prueba para actualizar una Editorial.
	 */
	@Test
	void testUpdateEditorial() throws EntityNotFoundException {
		EditorialEntity entity = editorialList.get(0);
		EditorialEntity pojoEntity = factory.manufacturePojo(EditorialEntity.class);
		pojoEntity.setId(entity.getId());
		editorialService.updateEditorial(entity.getId(), pojoEntity);
		EditorialEntity resp = entityManager.find(EditorialEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getName(), resp.getName());
	}
	
	/**
	 * Prueba para actualizar una Editorial que no existe.
	 */
	@Test
	void testUpdateEditorialInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			EditorialEntity pojoEntity = factory.manufacturePojo(EditorialEntity.class);
			pojoEntity.setId(0L);
			editorialService.updateEditorial(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Editorial.
	 */
	@Test
	void testDeleteEditorial() throws EntityNotFoundException, IllegalOperationException {
		EditorialEntity entity = editorialList.get(1);
		editorialService.deleteEditorial(entity.getId());
		EditorialEntity deleted = entityManager.find(EditorialEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar una Editorial que no existe.
	 */
	@Test
	void testDeleteEditorialInvalid(){
		assertThrows(EntityNotFoundException.class, ()->{
			editorialService.deleteEditorial(0L);
		});
	}


	/**
	 * Prueba para eliminar un Editorial con books asociados.
	 */
	@Test
	void testDeleteEditorialWithBooks() {
		assertThrows(IllegalOperationException.class, () -> {
			EditorialEntity entity = editorialList.get(0);
			editorialService.deleteEditorial(entity.getId());
		});
	}
}
