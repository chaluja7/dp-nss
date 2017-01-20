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

@Configuration
@EnableNeo4jRepositories(basePackages = "cz.cvut.dp.nss.graph.repository")
@EnableTransactionManagement
public class Neo4jConfig extends Neo4jConfiguration {

    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory("cz.cvut.dp.nss.graph.services");
    }

    // needed for session in view in web-applications
    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    //TODO scope session!!!
    public Session getSession() throws Exception {
        return super.getSession();
    }

}