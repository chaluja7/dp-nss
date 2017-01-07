package cz.cvut.dp.nss.context;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Kazda nova transakce se otevira pro zvoleneho tenanta (v nasem pripade schema db)
 * Tenant se zvoli v CurrentTenantResolverImpl.
 * Zde se sahne do connection poolu a vytahne se connection. Te se nasledne dle jmena tenantu (schematu) nastavi schema pomoci SET search_path TO ...
 *
 * Existuje tedy pouze jeden connection pool. Viz https://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html/ch16.html
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component(value = "schemaPerTenantConnectionProvider")
public class SchemaPerTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    private DataSource dataSource;

    @Autowired
    public SchemaPerTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            connection.createStatement().execute("SET search_path TO " + tenantIdentifier);
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }

        return connection;
    }

    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("SET search_path TO public");
        } catch (SQLException e) {
            // on error, throw an exception to make sure the connection is not returned to the pool.
            // your requirements may differ
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }

        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

}
