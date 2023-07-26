package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Resource {
    USER("User"),
    REFRESH_TOKEN("RefreshToken"),
    ACCESS_TOKEN("AccessToken"),
    COURSE("Course");

    private final String name;
}
