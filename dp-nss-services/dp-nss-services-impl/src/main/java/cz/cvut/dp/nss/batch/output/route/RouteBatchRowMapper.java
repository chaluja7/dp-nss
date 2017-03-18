package cz.cvut.dp.nss.batch.output.route;

import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.RouteType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class RouteBatchRowMapper implements RowMapper<RouteBatchWrapper> {

    @Override
    public RouteBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        RouteBatchWrapper route = new RouteBatchWrapper();
        route.setId(rs.getString("id"));
        route.setAgencyId(rs.getString("agency_id"));
        route.setShortName(rs.getString("shortName"));
        route.setLongName(rs.getString("longName"));
        route.setColor(rs.getString("color"));

        String routeType = rs.getString("routeType");
        if(!StringUtils.isEmpty(routeType)) {
            route.setRouteType(EnumUtils.fromName(routeType, RouteType.class).getCode());
        }

        return route;
    }

}
