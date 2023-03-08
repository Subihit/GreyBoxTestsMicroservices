package ITs;

import com.bezkoder.spring.jpa.postgresql.SpringBootJpaPostgresqlApplication;
import com.bezkoder.spring.jpa.postgresql.controller.TutorialController;
import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest(classes = SpringBootJpaPostgresqlApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TutorialTests {
//Add JBehave
    @Autowired
    TutorialController tutorialController;

    Tutorial tutorial;

    Long id;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withUsername("password")
            .withPassword("user")
            .withDatabaseName("test_db");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        System.out.println("start postgress");
        postgres.start();

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeAll
    public void createTutorial() {

        tutorial = new Tutorial("testTitle121", "testDesc212", false);


        ResponseEntity<Tutorial> responseEntity = tutorialController.createTutorial(tutorial);

        id = responseEntity.getBody().getId();
    }

    @Test
    public void getTutorial() {

        ResponseEntity<Tutorial> responseEntity = tutorialController.getTutorialById(id);

        Assertions.assertEquals(responseEntity.getBody().getId(), id);
        Assertions.assertEquals(responseEntity.getBody().getDescription(), "testDesc212");
        Assertions.assertEquals(responseEntity.getBody().getTitle(), "testTitle121");
        Assertions.assertEquals(responseEntity.getBody().isPublished(), false);
    }

    @Test
    public void deleteTutorial() {

        ResponseEntity<HttpStatus> responseEntity = tutorialController.deleteTutorial(id);

        Assertions.assertEquals(responseEntity.getStatusCode().is2xxSuccessful(), true);

        ResponseEntity<Tutorial> responseEntity1 = tutorialController.getTutorialById(id);
        Assertions.assertEquals(responseEntity1.getStatusCode(), HttpStatus.NOT_FOUND);

    }
}