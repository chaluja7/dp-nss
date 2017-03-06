package cz.cvut.dp.nss.controller.interceptor;

import cz.cvut.dp.nss.services.role.Role;

import java.lang.annotation.*;

/**
 * Vlastni anotace pro kontrolu pristupu k metodam.
 *
 * @author jakubchalupa
 * @since 06.03.17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface CheckAccess {

    /**
     * nutne role pro pristup
     */
    Role.Type[] value() default {Role.Type.USER};

}
