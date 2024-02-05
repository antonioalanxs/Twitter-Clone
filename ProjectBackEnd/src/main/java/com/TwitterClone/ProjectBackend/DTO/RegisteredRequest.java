package com.TwitterClone.ProjectBackend.DTO;

import lombok.*;

/**
 * Class with the finality to transport information of a new user from the front to the backend
 */
@Data
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RegisteredRequest {
    private final String email;
    private String username;
    private String password;
}
