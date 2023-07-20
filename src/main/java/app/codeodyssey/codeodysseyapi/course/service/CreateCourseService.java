package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.User;
import app.codeodyssey.codeodysseyapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;

    public CourseResponse execute(CreateCourseCommand command) {
        User professor = userService.findById(command.professorId());

        Course course = courseRepository.save(new Course(
                command.name(),
                command.slug(),
                command.startDate(),
                command.endDate(),
                professor
        ));

        return courseMapper.to(course);
    }
}
