package ui1.raullozano.bigfootball.etl;

import ui1.raullozano.bigfootball.common.utils.Time;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Scheduler {

    private final ScheduledExecutorService scheduler;
    private final Consumer<Void> consumer;
    private final Time.Scale scale;
    private final List<Integer> components;

    private Integer lastComponent;

    public Scheduler(Consumer<Void> consumer, Time.Scale scale, List<Integer> components) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.consumer = consumer;
        this.scale = scale;
        this.components = components;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::execute, 0, 1, timeUnitOf(scale));
    }

    private void execute() {
        Boolean isComponent = isComponent();
        if(isComponent) {
            Integer lastComponent = lastComponent();
            if(this.lastComponent == null || !Objects.equals(lastComponent, this.lastComponent)) {
                this.lastComponent = lastComponent;
                consumer.accept(null);
            }
        } else {
            this.lastComponent = null;
        }
    }

    private Boolean isComponent() {
        return this.components.contains(componentOf(Time.currentInstant()));
    }

    private Integer lastComponent() {
        int component = componentOf(Time.currentInstant());
        for (int i = this.components.size() - 1; i >= 0; i--) {
            if(component > this.components.get(i)) {
                return this.components.get(i);
            }
        }
        return this.components.get(this.components.size() - 1);
    }

    private int componentOf(Instant instant) {
        switch (scale) {
            case H:
                return Time.hourOf(instant);
            case D:
                return Time.monthDayOf(instant);
            case M:
                return Time.monthOf(instant);
        }
        return Time.hourOf(instant);
    }

    private static TimeUnit timeUnitOf(Time.Scale scale) {
        switch (scale) {
            case H:
                return TimeUnit.MINUTES;
            case D:
                return TimeUnit.HOURS;
            case M:
                return TimeUnit.DAYS;
        }
        return TimeUnit.MINUTES;
    }
}
