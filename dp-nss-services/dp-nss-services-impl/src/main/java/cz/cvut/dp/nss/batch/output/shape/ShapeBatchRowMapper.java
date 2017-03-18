package cz.cvut.dp.nss.batch.output.shape;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class ShapeBatchRowMapper implements RowMapper<ShapeBatchWrapper> {

    @Override
    public ShapeBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        ShapeBatchWrapper shape = new ShapeBatchWrapper();
        shape.setShapeId(rs.getString("shapeId"));
        shape.setLat(rs.getDouble("lat"));
        shape.setLon(rs.getDouble("lon"));
        shape.setSequence(rs.getInt("sequence"));

        return shape;
    }

}
