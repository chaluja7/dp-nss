package cz.cvut.dp.nss.context;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Vyber tenanta (v nasem pripade schemat db) do ktereho aktualni transakce pristupuje.
 * Tenant musi byt dopredu nastaveny do SchemaThreadLocal, odkud se nyni cte. Nastavit by se mel ve filtru (pokud jde o http pozadavek)
 * Pokud neni threadLocal atribut pritomny, cte se z defaultniho schematu (public)
 *
 * @author jakubchalupa
 * @since 05.01.16
 */
@Component(value = "currentTenantResolverImpl")
public class CurrentTenantResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String identifier = SchemaThreadLocal.get();
        if(identifier == null) {
            identifier = DEFAULT_TENANT;
        }

        return identifier;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
