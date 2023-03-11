package com.sardul3.footypickup.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private int goals;
    private boolean isCurrent;
}
