package co.edu.uniandes.dse.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookstoreApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	public void applicationContextTest() {
		BookstoreApplication.main(new String[] {});
	}

}
