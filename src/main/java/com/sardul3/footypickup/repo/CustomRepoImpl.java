package com.sardul3.footypickup.repo;


import com.sardul3.footypickup.domain.GoalEvent;
import com.sardul3.footypickup.domain.Match;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomRepoImpl implements CustomRepo{

    private final ReactiveMongoTemplate mongoTemplate;

    public CustomRepoImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Match> addGoalEvent(String matchId, String teamId, String playerId) {
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

        var newGoal = GoalEvent.builder()
                .id(teamId)
                .goalBy(playerId)
                .isCurrent(true)
                .build();

        Query query = new Query(Criteria.where("id").is(matchId));
        Update goalUpdate = new Update().push("goals", newGoal);

        return mongoTemplate.findAndModify(query, goalUpdate, findAndModifyOptions, Match.class);
    }

    public Flux<ObjectId> getScoreCard(String matchId) {
        var score =  mongoTemplate.query(Match.class)
                .distinct("goals.id")
                .as(ObjectId.class)
                .all();

        return score;
    }
}
