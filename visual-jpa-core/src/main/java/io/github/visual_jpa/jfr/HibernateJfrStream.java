package io.github.visual_jpa.jfr;


import jdk.jfr.consumer.RecordingStream;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class HibernateJfrStream implements SmartLifecycle {

    @Override
    public void start() {
        Thread thread = new Thread(this::run, "visual-jpa-jfr-stream");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() {

    }

    public boolean isRunning() {
        return false;
    }

    private void run() {
        try (RecordingStream rs = new RecordingStream()) {
            // Enable Hibernate JFR events
            rs.enable("org.hibernate.orm.query");
            rs.enable("org.hibernate.orm.entity_load");
            rs.enable("org.hibernate.orm.flush");

            rs.onEvent(event -> {
                String name = event.getEventType().getName();

                if (!name.startsWith("org.hibernate.orm")) {
                    return;
                }
                System.out.println("Event name : "  + event.getEventType().getName() + " - " + event.getDuration());
            });
            rs.start();
        }

    }

    public boolean isAutoStartup() {
        return true;
    }
}
