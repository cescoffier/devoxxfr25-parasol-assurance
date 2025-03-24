package me.escoffier.parasol;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ClaimService {

    private static final AtomicInteger ID = new AtomicInteger(0);

    private final ValueCommands<String, ClaimInfo> values;
    private final RedisDataSource database;
    private final InsuranceClaimAssistant assistant;

    @Inject
    EventBus bus;

    public ClaimService(RedisDataSource ds, InsuranceClaimAssistant assistant) {
        this.database = ds;
        this.values = ds.value(ClaimInfo.class);
        this.assistant = assistant;
    }

    @Inject
    ObjectMapper mapper;

    public int process(String claim) {
        var summary = assistant.summarize(claim);
        var sentiment = assistant.analyzeSentiment(claim);

        ClaimInfo info = new ClaimInfo();
        info.summary = summary.summary;
        info.time = summary.date;
        info.location = summary.location;
        info.sentiment = sentiment;
        info.subject = summary.title;
        info.policyNumber = summary.policyNumber;
        info.body = claim;

        // We also fire an event that a new claim has been created.
        bus.send("claimCreated", info);

        return persist(info);
    }


    public int persist(ClaimInfo claim) {
        if (claim.claimId <= 0) {
            claim.claimId = ID.incrementAndGet();
        }
        values.set("claim:" + claim.claimId, claim);
        return claim.claimId;
    }

    public void update(ClaimInfo claim, int claimId) {
        if (claim.claimId != claimId) {
            throw new IllegalArgumentException("Claim ID mismatch");
        }
        values.set("claim:" + claim.claimId, claim);
    }

    public ClaimInfo get(int claimId) {
        return values.get("claim:" + claimId);
    }

    public boolean delete(int claimId) {
        return values.getdel("claim:" + claimId) != null;
    }

    public void deleteAllClaims() {
        database.key().del("claim:*");
    }

    public List<Integer> getClaimIds() {
        return database.key().keys("claim:*").stream()
                .map(s -> Integer.parseInt(s.substring(6)))
                .toList();
    }

    public List<ClaimInfo> getAllClaims() {
        return getClaimIds().stream()
                .map(this::get)
                .toList();
    }

}
