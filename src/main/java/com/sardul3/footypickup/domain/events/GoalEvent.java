package com.sardul3.footypickup.domain.events;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class GoalEvent {
    private String eventId;
    private String goalBy;
    private String assistBy = "NA";
    private boolean isCurrent = true;
}
