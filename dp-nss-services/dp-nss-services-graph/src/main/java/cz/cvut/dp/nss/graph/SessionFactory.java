package cz.cvut.dp.nss.graph;

import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.autoindex.AutoIndexManager;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.event.EventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Vlastni implementace sessionFactory, ktera umoznuje mit soucasne vice konfiguraci pro ruzna schemata
 * V tomto pripade jsou to ve skutecnosti zcela oddelene neo4j instance bezici na jinych portech
 * Klidne ale mohou bezet i na uplne jinych serverech.
 *
 * @author Vince Bickers
 * @author Luanne Misquitta
 * @author Mark Angrish
 * @author jakubchalupa
 */
public class SessionFactory {

    private final MetaData metaData;
    private final AutoIndexManager autoIndexManager;
    private final List<EventListener> eventListeners;
    private final String schema;

    private SessionFactory(String schema, Configuration configuration, MetaData metaData) {
        if (configuration != null) {
            Components.configure(schema, configuration);
        }
        this.schema = schema;
        this.metaData = metaData;
        this.autoIndexManager = new AutoIndexManager(this.metaData, Components.driver(schema));
        this.autoIndexManager.build();
        this.eventListeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructs a new {@link org.neo4j.ogm.session.SessionFactory} by initialising the object-graph mapping meta-data from the given list of domain
     * object packages, and also sets the configuration to be used.
     * <p>
     * The package names passed to this constructor should not contain wildcards or trailing full stops, for example,
     * "org.springframework.data.neo4j.example.domain" would be fine.  The default behaviour is for sub-packages to be scanned
     * and you can also specify fully-qualified class names if you want to cherry pick particular classes.
     * </p>
     * Indexes will also be checked or built if configured.
     *
     * @param configuration The configuration to use
     * @param packages The packages to scan for domain objects
     */
    public SessionFactory(String schema, Configuration configuration, String... packages) {
        this(schema, configuration, new MetaData(packages));
    }

    /**
     * Opens a new Neo4j mapping {@link Session} using the Driver specified in the OGM configuration
     * The driver should be configured to connect to the database using the appropriate
     * DriverConfig
     *
     * @return A new {@link Session}
     */
    public Session openSession() {
        return new Neo4jSession(metaData, Components.driver(schema), eventListeners);
    }

}
