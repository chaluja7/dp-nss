package cz.cvut.dp.nss.filter;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.context.ThreadScope;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter, ktery precte vstupni URI a vytahne z neho ID jizdniho radu, pokud tam je. Toto ID ulozi do ThreadLocal.
 * Nasledne posle pozadavek na cilovy controller.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
public class SchemaHandlerFilter implements Filter {

    private static final String SCHEMA_PREFIX = "x-";

    private String apiPath;

    private TimeTableService timeTableService;

    private ThreadScope threadScope;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
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
                    //pokud ano tak nejdrive zjistim, jestli neni schema zamcene kvuli importu
                    TimeTable timeTable = timeTableService.get(schema);
                    if(timeTable.isSynchronizing()) {
                        throw new IllegalStateException("this schema is currenty synchronizing.");
                    }

                    //pokud ano, tak do threadLocal schema nastavim
                    SchemaThreadLocal.set(schema);

                    //pred slozenim redirect url musim odstranit nazev schematu
                    String[] withoutSchema = (String[]) ArrayUtils.remove(split, 0);
                    String newUri = apiPath + String.join("/", withoutSchema);

                    //a provedu server side redirect, ten uz chytne prislusny controller
                    httpRequest.getRequestDispatcher(newUri).forward(request, response);

                    //po provedeni requestu nezapomenu schema smazat z threadLocal!!!
                    SchemaThreadLocal.unset();
                    threadScope.cleanUpThreadScopedBeans();
                    return;
                }
            }
        }

        //pokud nedoslo k presmerovani tak necham request dobehnout tak, jak prisel
        chain.doFilter(request, response);
        threadScope.cleanUpThreadScopedBeans();
    }

    @Override
    public void destroy() {

    }

    @Required
    public void setTimeTableService(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @Required
    public void setThreadScope(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    @Required
    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
