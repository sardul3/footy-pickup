package com.sardul3.footypickup.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalEvent {
    @Id
    private String id;
    @NotNull
    private String goalBy;
    private String assistBy = "NA";
    private boolean isCurrent = true;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoalEvent other = (GoalEvent) obj;
        return Objects.equals(id, other.id);
    }
}
