package cz.cvut.dp.nss.filter;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.apache.commons.lang.ArrayUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jakubchalupa
 * @since 02.03.17
 */
public class SchemaHandlerFilter implements Filter {

    private static final String SCHEMA_PREFIX = "x-";

    private String apiPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        apiPath = filterConfig.getInitParameter("apiPath");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            //vytahnu si URI, na kterou prisel dotaz
            String requestURI = httpRequest.getRequestURI();

            //odstranim api prefix (urcite tam je, jinak by se neprovadel tento filtr)
            String tmpUri = requestURI.substring(apiPath.length(), requestURI.length());

            //rozseknu po lomitkach
            String[] split = tmpUri.split("/");

            //vytahnu prvni hodnotu, coz by mohlo byt oznaceni pozadovaneho schematu s prefixem 'x-'
            String schema = split[0];
            if(schema.startsWith(SCHEMA_PREFIX)) {
                //odstranim x prefix
                schema = schema.substring(SCHEMA_PREFIX.length(), schema.length());

                //zjistim, jestli nazev schematu odpovida nejakemu existujicimu
                if(SchemaThreadLocal.AVAILABLE_SCHEMAS.contains(schema)) {
                    //pokud ano, tak do threadLocal schema nastavim
                    SchemaThreadLocal.set(schema);

                    //pred slozenim redirect url musim odstranit nazev schematu
                    String[] withoutSchema = (String[]) ArrayUtils.remove(split, 0);
                    String newUri = apiPath + String.join("/", withoutSchema);

                    //a provedu server side redirect, ten uz chytne prislusny controller
                    httpRequest.getRequestDispatcher(newUri).forward(request, response);

                    //po provedeni requestu nezapomenu schema smazat z threadLocal!!!
                    SchemaThreadLocal.unset();
                    return;
                }
            }
        }

        //pokud nedoslo k presmerovani tak necham request dobehnout tak, jak prisel
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
