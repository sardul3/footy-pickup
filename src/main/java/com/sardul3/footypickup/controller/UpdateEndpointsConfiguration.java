package com.sardul3.footypickup.controller;

import com.sardul3.footypickup.service.CaseInsensitiveRequestPredicate;
import com.sardul3.footypickup.service.UpdateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UpdateEndpointsConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(UpdateHandler handler) {
        return route(pre(POST("/v1/players/{id}/update")), handler::updatePlayer)
                .andRoute(pre(POST("/v1/teams/{id}/update")), handler::updateTeam)
                .andRoute(pre(POST("/v1/matches/{id}/update")), handler::updateMatch);
    }

    private static RequestPredicate pre(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
