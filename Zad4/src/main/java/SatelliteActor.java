import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SatelliteActor extends AbstractBehavior<SatelliteActor.SatelliteQuery> {

    public int satelliteId;

    public static class SatelliteQuery {
        public final int query_id;
        public final ActorRef<Dispatcher.DispatcherMessage> replyTo;
        public final int timeout;

        public SatelliteQuery(int query_id, ActorRef<Dispatcher.DispatcherMessage> replyTo, int timeout) {
            this.replyTo = replyTo;
            this.query_id = query_id;
            this.timeout = timeout;
        }
    }

    public static Behavior<SatelliteActor.SatelliteQuery> create(int satelliteId) {
        return Behaviors.setup(context -> new SatelliteActor(context, satelliteId));
    }

    public SatelliteActor(ActorContext<SatelliteQuery> context, int satelliteId) {
        super(context);
        this.satelliteId = satelliteId;
    }

    @Override
    public Receive<SatelliteQuery> createReceive() {
        return newReceiveBuilder()
                .onMessage(SatelliteActor.SatelliteQuery.class, this::onQuery)
                .build();
    }

    private Behavior<SatelliteActor.SatelliteQuery> onQuery(SatelliteActor.SatelliteQuery command) {

        var x = CompletableFuture.supplyAsync(() -> SatelliteAPI.getStatus(satelliteId))
                .orTimeout(command.timeout, TimeUnit.MILLISECONDS)
                .whenComplete((res, error) -> {
                    boolean isTimedOut = error != null;
                    command.replyTo.tell(new Dispatcher.SatelliteResponse(command.query_id, satelliteId, isTimedOut, res));
                });

        try {
            x.get(command.timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | TimeoutException | InterruptedException ignored) {
        }

        return this;
    }
}

