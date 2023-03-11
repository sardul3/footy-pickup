package com.sardul3.footypickup.domain.events;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class RedCardEvent {

    private String eventId;
    private String receivedBy;
    private boolean isCurrent = true;

}
