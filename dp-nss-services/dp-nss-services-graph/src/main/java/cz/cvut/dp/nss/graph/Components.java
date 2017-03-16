package cz.cvut.dp.nss.graph;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Vlastni implementace component. Oproti originalu navic umi pracovat s vice configuracemi a drivery
 * Tedy umoznuje mit aplikaci pripojenou na vice instanci neo4j
 * Soucasne ale zanedbava kontroly existence, kofiguraci je tedy nutne opravdu spravne definovat v Neo4jConfig.java
 *
 * @author vince
 * @author jakubchalupa
 */
public class Components {

    private Components() {}

    private static final Logger logger = LoggerFactory.getLogger(Components.class);

    private static final Map<String, Configuration> configurations = Collections.synchronizedMap(new HashMap<>());

    private static final Map<String, Driver> drivers = Collections.synchronizedMap(new HashMap<>());

    /**
     * Configure the OGM from a pre-built Configuration class
     *
     * @param configuration The configuration to use
     */
    public static void configure(String schema, Configuration configuration) {
        Components.configurations.put(schema, configuration);
    }

    /**
     * Returns the current OGM {@link Driver}
     *
     * Normally only one instance of the driver exists for the lifetime of the application
     *
     * You cannot use this method to find out if a driver is initialised because it will attempt to
     * initialise the driver if it is not.
     *
     * @return an instance of the {@link Driver} to be used by the OGM
     */
    public synchronized static Driver driver(String schema) {
        if(!Components.drivers.containsKey(schema)) {
            loadDriver(schema);
        }

        return Components.drivers.get(schema);
    }

    /**
     * Loads the configured Neo4j {@link Driver} and stores it on this class
     */
    private static void loadDriver(String schema) {
        setDriver(schema, DriverService.load(Components.configurations.get(schema).driverConfiguration()));
    }

    /**
     * Sets a new {@link Driver} to be used by the OGM.
     *
     * If a different driver is in use, it will be closed first. In addition, the {@link Configuration} is updated
     * to reflect the correct classname for the new driver.
     *
     * @param driver an instance of {@link Driver} to be used by the OGM.
     */
    public static void setDriver(String schema, Driver driver) {

        logger.debug("Setting driver to: {}", driver.getClass().getName());
        Components.getConfiguration(schema).driverConfiguration().setDriverClassName(driver.getClass().getCanonicalName());
        Components.drivers.put(schema, driver);
    }

    /**
     * There is a single configuration object, which should never be null, associated with the Components class
     * You can update this configuration in-situ, or you can replace the configuration with another.
     *
     * @return the current Configuration object
     */
    public static Configuration getConfiguration(String schema) {
        return Components.configurations.get(schema);
    }

}
