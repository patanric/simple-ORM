package ch.riccardo.reflection.beanmanager;

import ch.riccardo.reflection.annotations.Inject;
import ch.riccardo.reflection.annotations.Provides;
import ch.riccardo.reflection.provider.H2ConnectionProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BeanManager {

    private static final BeanManager instance = new BeanManager();
    private final Map<Class<?>, Supplier<?>> registry = new HashMap<>();

    public static BeanManager getInstance() {
        return instance;
    }

    private BeanManager() {
        List<Class<?>> classes = List.of(H2ConnectionProvider.class);
        for (Class<?> clss : classes) {
            Method[] methods = clss.getDeclaredMethods();
            for (Method method : methods) {
                Provides provides = method.getAnnotation(Provides.class);
                if (provides != null) {
                    Class<?> returnType = method.getReturnType();
                    Supplier<?> supplier = () -> {
                        try {
                            if (!Modifier.isStatic(method.getModifiers())) {
                                Object o = clss.getConstructor().newInstance();
                                return method.invoke(o);
                            } else {
                                return method.invoke(null);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    };
                    registry.put(returnType, supplier);
                }
            }
        }
    }

    public <T> T getInstance(Class<T> clss) {
        try {
            // get an instance of clss
            T t = clss.getConstructor().newInstance();
            // check if clss contains fields annotated with @Inject
            Field[] fields = clss.getDeclaredFields();
            for (Field field : fields) {
                Inject inject = field.getAnnotation(Inject.class);
                if (inject != null) {
                    // construct the object to be injected
                    Class<?> injectedFieldType = field.getType();
                    Supplier<?> supplier = registry.get(injectedFieldType);
                    Object objectToInject = supplier.get();
                    field.setAccessible(true);
                    // inject the object to clss before providing it to the client code
                    field.set(t, objectToInject);
                }
            }

            return t;

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
