package com.sardul3.footypickup.domain;

import org.springframework.context.ApplicationEvent;

public class MatchStartedEvent extends ApplicationEvent {
    public MatchStartedEvent(Match match) {
        super(match);
    }
}
