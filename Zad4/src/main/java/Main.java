import akka.actor.typed.*;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Random rand = new Random();

    public static Behavior<Void> create(LinkedList<String> stationsNames) {
        return Behaviors.setup(
                context -> {
                    ActorRef<Dispatcher.DispatcherMessage> dispatcher = context.spawn(
                            Behaviors.supervise(Dispatcher.create())
                                    .onFailure(Exception.class, SupervisorStrategy.restart()), "dispatcher");

                    int query_id = 0;
                    for (String name : stationsNames) {
                        ActorRef<MonitoringStationActor.MonitorMessage> station =
                                context.spawn(Behaviors.supervise(MonitoringStationActor.create(dispatcher, name))
                                        .onFailure(Exception.class, SupervisorStrategy.restart()), name);
                        LinkedList<Dispatcher.DispatcherQuery> queries = new LinkedList<>();
                        queries.add(new Dispatcher.DispatcherQuery(query_id, 100 + rand.nextInt(50), 50, 300));
                        queries.add(new Dispatcher.DispatcherQuery(query_id + 1, 100 + rand.nextInt(50), 50, 300));
                        query_id += 2;
                        station.tell(new MonitoringStationActor.MakeQueries(queries));
                    }

                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                });
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("Main: Application started");

        System.out.println("Enter number of stations:");
        int numberOfStations = Integer.parseInt(reader.nextLine());

        LinkedList<String> names = new LinkedList<>();
        System.out.println("Enter stations names in separate lines:");
        for (int i = 0; i < numberOfStations; i++) {
            names.add(reader.nextLine());
        }

        File configFile = new File("src/main/resources/dispatcher.conf");
        Config config = ConfigFactory.parseFile(configFile);

        ActorSystem.create(Main.create(names), "Main", config);

        System.out.println("Main: Actor system ready");
    }
}
