package io.github.visual_jpa.core.context;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ExecutionContext {

    private static final ThreadLocal<Deque<String>> SERVICE_METHOD_STACK =
            ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<Deque<String>> REPOSITORY_METHOD_STACK =
            ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<String> LAST_SERVICE_METHOD =
            new ThreadLocal<>();
    private static final ThreadLocal<String> LAST_REPOSITORY_METHOD =
            new ThreadLocal<>();

    private ExecutionContext() {
    }

    public static void pushServiceMethod(String method) {
        if (method == null) {
            return;
        }
        SERVICE_METHOD_STACK.get().push(method);
        LAST_SERVICE_METHOD.set(method);
    }

    public static void popServiceMethod() {
        Deque<String> stack = SERVICE_METHOD_STACK.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
        if (stack.isEmpty()) {
            SERVICE_METHOD_STACK.remove();
        }
    }

    public static String currentServiceMethod() {
        Deque<String> stack = SERVICE_METHOD_STACK.get();
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return LAST_SERVICE_METHOD.get();
    }

    public static void pushRepositoryMethod(String method) {
        if (method == null) {
            return;
        }
        REPOSITORY_METHOD_STACK.get().push(method);
        LAST_REPOSITORY_METHOD.set(method);
    }

    public static void popRepositoryMethod() {
        Deque<String> stack = REPOSITORY_METHOD_STACK.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
        if (stack.isEmpty()) {
            REPOSITORY_METHOD_STACK.remove();
        }
    }

    public static String currentRepositoryMethod() {
        Deque<String> stack = REPOSITORY_METHOD_STACK.get();
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return LAST_REPOSITORY_METHOD.get();
    }

    public static void clear() {
        SERVICE_METHOD_STACK.remove();
        REPOSITORY_METHOD_STACK.remove();
        LAST_SERVICE_METHOD.remove();
        LAST_REPOSITORY_METHOD.remove();
    }
}
