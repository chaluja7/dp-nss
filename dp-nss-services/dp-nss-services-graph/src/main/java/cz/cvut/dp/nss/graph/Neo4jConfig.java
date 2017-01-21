package cz.cvut.dp.nss.graph;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * pro verzi 4.1.6 viz
 * http://docs.spring.io/spring-data/neo4j/docs/current/reference/html/#_configuration
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "cz.cvut.dp.nss.graph.repository")
@EnableTransactionManagement
public class Neo4jConfig extends Neo4jConfiguration {

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config.driverConfiguration()
            .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
            .setURI("bolt://neo4j:neo@localhost:7687");
//            .setEncryptionLevel("NONE")
//            .setTrustStrategy("TRUST_ON_FIRST_USE")
//            .setTrustCertFile("/tmp/cert");

        return config;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        //parametr jsou package, kde jsou oanotovane graph entity
        return new SessionFactory(getConfiguration(), "cz.cvut.dp.nss.graph.services");
    }

    //FIXME nemel by zde byt session scope?
    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        Session session = super.getSession();
        return session;
    }

}