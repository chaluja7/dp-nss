package cz.cvut.dp.nss.context;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jakubchalupa
 * @since 16.05.17
 */
public class ThreadScope implements Scope {

    /**
     * This map contains for each bean name or ID the created object. The
     * objects are created with a spring object factory. The map is ThreadLocal,
     * so the bean are defined only in the current thread!
     */
    private final ThreadLocal<Map<String, Object>> threadLocalObjectMap = ThreadLocal.withInitial(HashMap::new);

    /** {@inheritDoc} */
    public Object get(final String beanName,
                      final ObjectFactory<?> objectFactory) {
        Object object = threadLocalObjectMap.get().get(beanName);

        if (object == null) {
            object = objectFactory.getObject();
            threadLocalObjectMap.get().put(beanName, object);
        }
        return object;
    }

    /** {@inheritDoc} */
    public String getConversationId() {
        // In this case, it returns the thread name.
        return Thread.currentThread().getName();
    }

    /** {@inheritDoc} */
    public void registerDestructionCallback(final String beanName, final Runnable theCallback) {
        //empty
    }

    /** {@inheritDoc} */
    public Object remove(final String beanName) {
        return threadLocalObjectMap.get().remove(beanName);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    /**
     * Invoke this method to cleanUp the ThreadLocal beans. This call is
     * required since in case of run in a thread pool, the thread will never be
     * removed and the threadLocal variables would be shared between two
     * different executions!
     */
    public void cleanUpThreadScopedBeans() {
        threadLocalObjectMap.remove();
    }
}
