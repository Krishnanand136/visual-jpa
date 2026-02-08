package io.github.visual_jpa.core.events.state;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Objects;

@Primary
@Component
public class JsonlEntityStateChangeLogger implements EntityStateChangeLogger {

    private static final Object FILE_LOCK = new Object();
    private static final String DEFAULT_FILE = "visual-jpa-entity-states.jsonl";
    private static final String FILE_PROPERTY = "visual.jpa.entity.log.file";

    @Override
    public void log(EntityStateChange change) {
        Path path = resolveLogFile();
        String line = toJsonLine(change);
        synchronized (FILE_LOCK) {
            try {
                Files.createDirectories(path.getParent());
                Files.writeString(
                        path,
                        line,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                System.err.println("[visual-jpa] Failed to write entity state log: " + e.getMessage());
            }
        }
    }

    private Path resolveLogFile() {
        String configured = System.getProperty(FILE_PROPERTY);
        if (configured != null && !configured.isBlank()) {
            return Paths.get(configured).toAbsolutePath();
        }
        String home = System.getProperty("user.home", ".");
        return Paths.get(home, DEFAULT_FILE).toAbsolutePath();
    }

    private String toJsonLine(EntityStateChange change) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("{");
        appendField(sb, "timestamp", Instant.now().toString()).append(",");
        appendField(sb, "transition", safe(change.getTransition())).append(",");
        appendField(sb, "entityName", change.getEntityName()).append(",");
        appendField(sb, "entityId", change.getEntityId()).append(",");
        appendField(sb, "transactionId", change.getTransactionId()).append(",");
        appendField(sb, "originMethod", change.getOriginMethod()).append(",");
        appendField(sb, "repositoryMethod", change.getRepositoryMethod()).append(",");
        appendField(sb, "details", change.getDetails());
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    private StringBuilder appendField(StringBuilder sb, String key, Object value) {
        sb.append("\"").append(key).append("\":");
        if (value == null) {
            sb.append("null");
            return sb;
        }
        if (value instanceof Number || value instanceof Boolean) {
            sb.append(value);
            return sb;
        }
        sb.append("\"").append(escapeJson(value.toString())).append("\"");
        return sb;
    }

    private String escapeJson(String input) {
        StringBuilder out = new StringBuilder(input.length() + 8);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '\"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> out.append(c);
            }
        }
        return out.toString();
    }

    private String safe(Object value) {
        return Objects.toString(value, null);
    }
}
