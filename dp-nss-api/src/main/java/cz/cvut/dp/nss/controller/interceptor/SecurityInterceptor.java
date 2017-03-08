package cz.cvut.dp.nss.controller.interceptor;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.services.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    protected PersonService personService;

    private static final String SECURITY_HEADER = "X-Auth";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //TODO!!! - je treba udelat tabulku user-timetables, kde se bude ukladat, jaky uzivatel ma pravo na jake jizdni rady
        //admin bude mit automaticky pravo na vsechny
        //zde pak z threadlocal vytahnu aktualni jizdni rad a zjistim, zda na nej ma uzivatel pravo

        if(o instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) o;

            //from method
            CheckAccess methodAnnotation = method.getMethodAnnotation(CheckAccess.class);
            if(methodAnnotation == null) {
                //from class
                methodAnnotation = method.getBeanType().getAnnotation(CheckAccess.class);
            }

            if(methodAnnotation == null) {
                //not secured
                return true;
            }

            String securityHeader = httpServletRequest.getHeader(SECURITY_HEADER);
            if(securityHeader == null || securityHeader.length() == 0) {
                throw new UnauthorizedException();
            }

            //pole roli, ktere jsou nutne pro tento resource
            Role.Type[] rolesNeeded = methodAnnotation.value();

            Person person = personService.getByToken(securityHeader);
            if(person != null) {
                //admin ma pravo automaticky na vsechno
                if(person.hasRole(Role.Type.ADMIN)) {
                    return true;
                }

                //najdeme, jestli uzivatel nema pozadovanou roli
                for(Role.Type roleNeeded : rolesNeeded) {
                    if(person.hasRole(roleNeeded)) {
                        //a pokud ano, tak jeste overime pravo na zadany jizdni rad, ktery je ulozeny v ThreadLocal (diky filtru pred api)
                        final String currentSchema = SchemaThreadLocal.get();
                        if(currentSchema == null) {
                            //pokud zde neni schema tak je spatny dotaz, tedy je treba uzivatele zariznout
                            throw new UnauthorizedException();
                        }

                        if(person.ownTimeTable(currentSchema)) {
                            //uzivatel ma pravo na dany jizdni rad :)
                            return true;
                        } else {
                            throw new UnauthorizedException();
                        }
                    }
                }
            }

            throw new UnauthorizedException();
        }

        throw new UnauthorizedException();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //empty
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //empty
    }

}
