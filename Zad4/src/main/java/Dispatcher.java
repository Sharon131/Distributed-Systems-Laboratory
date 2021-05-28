import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashMap;
import java.util.LinkedList;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;

public class Dispatcher extends AbstractBehavior<Dispatcher.DispatcherMessage> {

    public LinkedList<ActorRef<SatelliteActor.SatelliteQuery>> satellites = new LinkedList<>();
    public HashMap<Integer, QueryData> queriesData = new HashMap<>();

    public interface DispatcherMessage {
    }

    public static class DispatcherQuery implements DispatcherMessage {
        public final int query_id;
        public final int first_sat_id;
        public final int range;
        public final int timeout;
        public long startTime;
        public ActorRef<MonitoringStationActor.MonitorMessage> replyTo;

        public DispatcherQuery(int query_id, int first_sat_id, int range, int timeout) {
            this.query_id = query_id;
            this.first_sat_id = first_sat_id;
            this.range = range;
            this.timeout = timeout;
        }
    }

    public static class SatelliteResponse implements DispatcherMessage {
        public final int query_id;
        public final int satelliteId;
        public final boolean isTimedOut;
        public final SatelliteAPI.Status status;

        public SatelliteResponse(int query_id, int satelliteId, boolean isTimedOut, SatelliteAPI.Status status) {
            this.query_id = query_id;
            this.satelliteId = satelliteId;
            this.isTimedOut = isTimedOut;
            this.status = status;
        }
    }

    public static class QueryData {
        public final int query_id;
        public final int first_sat_id;
        public final int range;
        public final int timeout;
        public final long startTime;
        public final ActorRef<MonitoringStationActor.MonitorMessage> replyTo;
        public int timeoutsNo = 0;
        public int responses = 0;
        public HashMap<Integer, SatelliteAPI.Status> wrongStatuses = new HashMap<>();

        public QueryData(int query_id, int first_sat_id, int range, int timeout, long startTime,
                         ActorRef<MonitoringStationActor.MonitorMessage> replyTo) {
            this.query_id = query_id;
            this.first_sat_id = first_sat_id;
            this.range = range;
            this.timeout = timeout;
            this.startTime = startTime;
            this.replyTo = replyTo;
        }
    }

    public static Behavior<Dispatcher.DispatcherMessage> create() {
        return Behaviors.setup(Dispatcher::new);
    }

    public Dispatcher(ActorContext<Dispatcher.DispatcherMessage> context) {
        super(context);

        for (int i = 0; i < 100; i++) {
            satellites.addLast(getContext().spawn(Behaviors.supervise(SatelliteActor.create(i + 100))
                    .onFailure(Exception.class, SupervisorStrategy.restart()), "actorSatellite" + i));
        }
    }

    @Override
    public Receive<Dispatcher.DispatcherMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(DispatcherQuery.class, this::onQuery)
                .onMessage(Dispatcher.SatelliteResponse.class, this::onResponse)
                .build();
    }

    private Behavior<Dispatcher.DispatcherMessage> onQuery(DispatcherQuery command) {
        QueryData queryData = new QueryData(command.query_id, command.first_sat_id, command.range, command.timeout,
                command.startTime, command.replyTo);
        queriesData.put(command.query_id, queryData);

        for (int i = command.first_sat_id; i < command.first_sat_id + command.range; i++) {
            satellites.get(i - 100).tell(new SatelliteActor.SatelliteQuery(command.query_id, getContext().getSelf(), command.timeout));
        }
        return this;
    }

    private Behavior<Dispatcher.DispatcherMessage> onResponse(Dispatcher.SatelliteResponse response) {
        QueryData queryData = queriesData.get(response.query_id);
        queryData.responses++;

        if (response.isTimedOut) {
            queryData.timeoutsNo++;
        } else if (response.status != SatelliteAPI.Status.OK) {
            queryData.wrongStatuses.put(response.satelliteId, response.status);
        }

        if (queryData.responses >= queryData.range) {
            double rightTimePercentage = (100 - ((double) queryData.timeoutsNo * 100) / queryData.range);

            queryData.replyTo.tell(new MonitoringStationActor.QueryResponse(queryData.query_id, queryData.wrongStatuses,
                    rightTimePercentage, queryData.startTime));
            queriesData.remove(response.query_id);
        }

        return this;
    }
}
