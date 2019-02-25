package tasks;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import controllers.Ampel;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


public class AmpelUpdate {
    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public AmpelUpdate(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        this.actorSystem.scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS), // initialDelay
                Duration.create(30, TimeUnit.SECONDS), // interval
                () -> Ampel.ampelAktualisieren(),
                this.executionContext
        );
    }
}
