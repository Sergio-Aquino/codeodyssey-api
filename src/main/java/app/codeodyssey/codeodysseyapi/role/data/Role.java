package app.codeodyssey.codeodysseyapi.role.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
        this.id = UUID.randomUUID();
    }

    public Role(UUID id, RoleType type) {
        this.id = id;
        this.role = type;
    }
}
