package com.TwitterClone.ProjectBackend.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class StatisticRequest {
    private String name;
    private int value;
}
