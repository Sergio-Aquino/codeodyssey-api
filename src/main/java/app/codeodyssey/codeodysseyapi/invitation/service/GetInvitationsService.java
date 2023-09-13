package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetInvitationsService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final InvitationMapper invitationMapper;

    public List<InvitationResponse> execute(UUID courseId, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(courseId, Resource.COURSE));

        return invitationMapper.to(invitationRepository.findAllByCourseId(course.getId()));
    }
}
