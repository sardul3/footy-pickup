package com.sardul3.footypickup.service.eventlisteners;

import com.sardul3.footypickup.domain.events.RedCardEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedCardListener  {

    public void onApplicationEvent(RedCardEvent event) {
        String eventType = event.getEventId();
        String player = event.getReceivedBy();
        boolean isCurrent = event.isCurrent();

        log.info("{} from {} received a {}", player, isCurrent, eventType);
    }
}
