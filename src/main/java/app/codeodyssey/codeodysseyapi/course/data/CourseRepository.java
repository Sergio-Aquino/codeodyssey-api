package app.codeodyssey.codeodysseyapi.course.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    boolean existsBySlug(String slug);
}
