package cn.bluerhino.driver.view.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

public class ViewBuilder {

    @Inherited
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ViewInject {
        public int id() default -1;
    }

    public static void findView(Activity activity, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(ViewInject.class)) {
            ViewInject inject = field.getAnnotation(ViewInject.class);
            int id = inject.id();
            if (id > 0) {
                field.setAccessible(true);
                field.set(activity, activity.findViewById(id));
            }
        }
    }

    public static void findView(View view, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(ViewInject.class)) {
            ViewInject inject = field.getAnnotation(ViewInject.class);
            int id = inject.id();
            if (id > 0) {
                field.setAccessible(true);
                field.set(view, view.findViewById(id));
            }
        }
    }

    public static void buildAllView(Activity activity) throws IllegalAccessException {
        Class<?> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            findView(activity, field);
        }
    }

    public static void buildAllView(View view) throws IllegalAccessException {
        Class<?> cls = view.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            findView(view, field);
        }
    }

    @SuppressWarnings("unchecked")
    public static final <T extends View> T findView(View parentView, int id) {
        return (T) parentView.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static final <T extends View> T findView(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}
