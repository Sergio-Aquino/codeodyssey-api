package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.utils.CourseFactory;
import app.codeodyssey.codeodysseyapi.user.utils.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { DatabaseContainerInitializer.class })
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * TODO: findAllOrderByNameAscEndDateAsc();
     * case 1: returns an empty list
     * case 2: returns a list with 1 element
     * case 3: returns a list with N elements
     * case 4: returns a list with element named A before element B
     * case 5: returns a list with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    void findAllByOrderByNameAscEndDateAsc_givenEmptyDatabase_returnEmptyList() {
        testEntityManager.flush();

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();
        System.out.println(courseList.toString());

        assertThat(courseList).isEmpty();
    }

    @Test
    void findAllByOrderByNameAscEndDateAsc_givenOneStoredRow_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(user);
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(course);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
    }

    @Test
    void findAllByOrderByNameAscEndDateAsc_givenManyStoredRows_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course1 = CourseFactory.sampleCourseWithProfessor(user);
        var course2 = CourseFactory.sampleCourseWithProfessor(user);
        var course3 = CourseFactory.sampleCourseWithProfessor(user);
        course2.setName("Spring Security");
        course2.setSlug("spring-security");
        course3.setName("Spring Cloud");
        course3.setSlug("spring-cloud");
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(course1);
        testEntityManager.persistAndFlush(course2);
        testEntityManager.persistAndFlush(course3);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).containsExactlyInAnyOrder(course1, course2, course3);
    }
}
