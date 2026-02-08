package io.github.visual_jpa.core.events.tx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Objects;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class JsonlTransactionEventLogger implements TransactionEventLogger {

    private static final Object FILE_LOCK = new Object();
    private static final String DEFAULT_FILE = "visual-jpa-transactions.jsonl";
    private static final String FILE_PROPERTY = "visual.jpa.tx.log.file";

    @Override
    public void log(TransactionEvent event) {
        Path path = resolveLogFile();
        String line = toJsonLine(event);
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
                // Avoid crashing app due to logging failures
                System.err.println("[visual-jpa] Failed to write tx event log: " + e.getMessage());
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

    private String toJsonLine(TransactionEvent event) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("{");
        appendField(sb, "timestamp", Instant.now().toString()).append(",");
        appendField(sb, "type", safe(event.getType())).append(",");
        appendField(sb, "transactionName", event.getTransactionName()).append(",");
        appendField(sb, "transactionId", event.getTransactionId()).append(",");
        appendField(sb, "originMethod", event.getOriginMethod()).append(",");
        appendField(sb, "propagation", event.getPropagation()).append(",");
        appendField(sb, "nestingLevel", event.getNestingLevel()).append(",");
        appendField(sb, "newTransaction", event.getNewTransaction()).append(",");
        appendField(sb, "hasSavepoint", event.getHasSavepoint()).append(",");
        appendField(sb, "transactionActive", event.getTransactionActive()).append(",");
        appendField(sb, "readOnly", event.isReadOnly()).append(",");
        appendField(sb, "managedEntityCount", event.getManagedEntityCount()).append(",");
        appendField(sb, "details", event.getDetails());
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
