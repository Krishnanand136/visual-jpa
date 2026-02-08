package io.github.visual_jpa.core.events.hibernate;

import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class VisualJpaEntityInterceptor implements Interceptor {

    @Override
    public boolean onSave(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types
    ) {
        System.out.println("[visual-jpa] onSave: " + entity.getClass().getName());
        return false;
    }

    @Override
    public boolean onLoad(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types
    ) {
//        System.out.println("[visual-jpa] onLoad: " + entity.getClass().getName());
        return false;
    }
}
