package app.codeodyssey.codeodysseyapi.invitation.api;

import app.codeodyssey.codeodysseyapi.invitation.service.GetInvitationsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@AllArgsConstructor
public class GetInvitationsEndpoint {
    private final GetInvitationsService getInvitationsService;

    @GetMapping("{courseId}")
    public ResponseEntity<List<InvitationResponse>> get(@PathVariable @Valid UUID courseId, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return ResponseEntity.ok(getInvitationsService.execute(courseId, username));
    }
}
