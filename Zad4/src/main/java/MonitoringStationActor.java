import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashMap;
import java.util.LinkedList;

public class MonitoringStationActor extends AbstractBehavior<MonitoringStationActor.MonitorMessage> {

    ActorRef<Dispatcher.DispatcherMessage> dispatcher;
    String stationName;

    public interface MonitorMessage {};

    public static class QueryResponse implements MonitorMessage{
        public final int query_id;
        public final HashMap<Integer, SatelliteAPI.Status> wrongStatuses;
        public final double rightTimePercentage;
        public final long startTime;

        public QueryResponse(int query_id, HashMap<Integer, SatelliteAPI.Status> wrongStatuses, double rightTimePercentage, long startTime) {
            this.query_id = query_id;
            this.wrongStatuses = wrongStatuses;
            this.rightTimePercentage = rightTimePercentage;
            this.startTime = startTime;
        }
    }

    public static class MakeQueries implements MonitorMessage{
        public final LinkedList<Dispatcher.DispatcherQuery> queries;

        public MakeQueries(LinkedList<Dispatcher.DispatcherQuery> queries) {
            this.queries = queries;
        }
    }

    public static Behavior<MonitoringStationActor.MonitorMessage> create(ActorRef<Dispatcher.DispatcherMessage> dispatcher, String name) {
        return Behaviors.setup(context -> new MonitoringStationActor(context, dispatcher, name));
    }

    public MonitoringStationActor(ActorContext<MonitorMessage> context, ActorRef<Dispatcher.DispatcherMessage> dispatcher, String name) {
        super(context);
        this.dispatcher = dispatcher;
        this.stationName = name;
    }

    @Override
    public Receive<MonitorMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(QueryResponse.class, this::onQueryResponse)
                .onMessage(MakeQueries.class, this::onMakeQuery)
                .build();
    }


    private Behavior<MonitorMessage> onQueryResponse(QueryResponse response) {
        long endTime = System.nanoTime();
        double waitingTime = (endTime-response.startTime)/1000000.0;
        String id = this.stationName + " query id " + response.query_id;
        System.out.println(id + " waiting time: " + waitingTime + " ms");
        System.out.println(id + " no timeout %: " + response.rightTimePercentage);
        System.out.println(id + " error number: " + response.wrongStatuses.keySet().size());

        for (Integer satNum: response.wrongStatuses.keySet()) {
            SatelliteAPI.Status status = response.wrongStatuses.get(satNum);

            System.out.println(this.stationName + ": " + satNum + " " + status);
        }
        return this;
    }

    private Behavior<MonitorMessage> onMakeQuery(MakeQueries queries) {
        for (Dispatcher.DispatcherQuery query: queries.queries) {
            query.replyTo = getContext().getSelf();
            query.startTime = System.nanoTime();
            dispatcher.tell(query);
        }
        return this;
    }

}
