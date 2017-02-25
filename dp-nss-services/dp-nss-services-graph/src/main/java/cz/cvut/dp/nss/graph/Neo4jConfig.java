package cz.cvut.dp.nss.graph;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * pro verzi 4.1.6 viz
 * http://docs.spring.io/spring-data/neo4j/docs/current/reference/html/#_configuration
 *
 * Simuluje se zde vlastni "multi tenancy", protoze neo4j a spring data to zatim nepodporuje
 * Drzi se session factory na kazdou instanci neo4j (pro ruzne jizdni radi)
 * Session se pak bere v requestu dle nastaveni v SchemaThreadLocal stejne jako je tomu v CurrentTenantResolverImpl.java
 * Tam funguje implementace hibernate multi tenancy
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "cz.cvut.dp.nss.graph.repository")
@EnableTransactionManagement
public class Neo4jConfig extends Neo4jConfiguration {

    private static final String BOLT_DRIVER_CLASS = "org.neo4j.ogm.drivers.bolt.driver.BoltDriver";

    private static final String ENTITY_PACKAGES = "cz.cvut.dp.nss.graph.services";

    private static final Map<String, org.neo4j.ogm.config.Configuration> CONFIGURATION_MAP;

    static {
        org.neo4j.ogm.config.Configuration configDefault = new org.neo4j.ogm.config.Configuration();
        configDefault.driverConfiguration()
            .setDriverClassName(BOLT_DRIVER_CLASS)
            .setURI("bolt://neo4j:neo@localhost:7687");

        Map<String, org.neo4j.ogm.config.Configuration> map = new HashMap<>();
        map.put(SchemaThreadLocal.SCHEMA_DEFAULT, configDefault);
        //TODO ukazuje na jednu databazi - opravit po pridani nove
        map.put(SchemaThreadLocal.SCHEMA_PID, configDefault);
        //mapa nemusi byt synchronized - je definovana jako static final a nikdy se do ni uz nebude zapisovat
        CONFIGURATION_MAP = Collections.unmodifiableMap(map);
    }

    @Bean
    public Map<String, SessionFactory> getSessionFactoryMaps() {
        Map<String, SessionFactory> map = new HashMap<>();

        for(Map.Entry<String, org.neo4j.ogm.config.Configuration> entry : CONFIGURATION_MAP.entrySet()) {
            map.put(entry.getKey(), new SessionFactory(entry.getValue(), ENTITY_PACKAGES));
        }

        return Collections.unmodifiableMap(map);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        //bude pouzito jen pro metadata, proto si mohu dovolit si zde vybrat jednu
        //tato metoda totiz musi byt prepsana :)
        return getSessionFactoryMaps().get(SchemaThreadLocal.SCHEMA_DEFAULT);
    }

    @Bean
    @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        String identifier = SchemaThreadLocal.getOrDefault();

        SessionFactory sessionFactory = getSessionFactoryMaps().get(identifier);
        Assert.notNull(sessionFactory, "You must provide a SessionFactory instance in your Spring configuration classes");
        return sessionFactory.openSession();
    }

}