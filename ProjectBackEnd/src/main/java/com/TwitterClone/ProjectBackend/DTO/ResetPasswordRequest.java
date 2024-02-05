package com.TwitterClone.ProjectBackend.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResetPasswordRequest {
    private String password;
}
