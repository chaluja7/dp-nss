package cz.cvut.dp.nss.batch.output.stop;

import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopBatchRowMapper implements RowMapper<StopBatchWrapper> {

    @Override
    public StopBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        StopBatchWrapper stop = new StopBatchWrapper();
        stop.setId(rs.getString("id"));
        stop.setName(rs.getString("name"));
        stop.setLat(rs.getDouble("lat"));
        stop.setLon(rs.getDouble("lon"));
        stop.setParentId(rs.getString("parent_stop_id"));

        String wheelChairType = rs.getString("wheelChair");
        if(!StringUtils.isEmpty(wheelChairType)) {
            stop.setWheelChairType(EnumUtils.fromName(wheelChairType, StopWheelchairBoardingType.class).getCode());
        }

        return stop;
    }

}
