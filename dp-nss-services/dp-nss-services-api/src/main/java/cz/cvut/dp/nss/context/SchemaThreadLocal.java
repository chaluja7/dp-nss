package cz.cvut.dp.nss.context;

/**
 * Databaze je rozdelena do schemat (postgre). Pred kazdym ctenim musi byt schema urceno.
 * Toto je spravne misto, schema pak putuje unikatne celou aplikaci per thread.
 *
 * Pokud je nicmene schema natvrdo urcene u entity v anotaci (napr. u Person.java) tak se vzdy pouzije dane schema bez ohledu na to, ze se zde neco nastavi)
 * To je zadouci chovani, protoze napr osoby chceme cist vzdy z globalniho schematu
 *
 * Schema by melo byt predano z filtru a po provedeni requestu MUSIME!!! zavolat unset(), garbage collector by to nikdy sam od sebe nesmazal!
 *
 * typicke pouziti:
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SchemaThreadLocal.set(napr. request.path().split['/'][0]);
        chain.doFilter(request, response);
        SchemaThreadLocal.unset();
    }
 *
 * viz https://dzone.com/articles/java-thread-local-%E2%80%93-how-use
 * @author jakubchalupa
 * @since 07.01.17
 */
public class SchemaThreadLocal {

    /**
     * thread local kontext. schvalne pojmenovany camelCase aby bylo jasnejsi, ze je vzdy per request
     */
    private static final ThreadLocal<String> schemaThreadLocal = new ThreadLocal<>();

    public static void set(String schema) {
        schemaThreadLocal.set(schema);
    }

    public static void unset() {
        schemaThreadLocal.remove();
    }

    public static String get() {
        return schemaThreadLocal.get();
    }

}
