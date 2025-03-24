package me.escoffier.parasol;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;

@WebSocket(path = "/ws")
public class ClaimWebSocket {

    @Inject
    WebSocketConnection connection;

    @ConsumeEvent("claimCreated")
    public void claimCreated(ClaimInfo claimInfo) {
        connection.broadcast().sendTextAndAwait(claimInfo);
    }

    @OnOpen
    void open() {
        Log.info("Opening websocket connection");
    }
}
