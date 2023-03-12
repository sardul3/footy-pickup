package com.sardul3.footypickup.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ScoreCard {
    private Map<String, Integer> scoreSheet = new HashMap<String, Integer>();
}
