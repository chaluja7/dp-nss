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

    @Override
    public String resolveCurrentTenantIdentifier() {
        return SchemaThreadLocal.getOrDefault();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
