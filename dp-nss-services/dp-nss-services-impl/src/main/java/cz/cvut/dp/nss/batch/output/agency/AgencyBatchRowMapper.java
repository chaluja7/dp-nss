package cz.cvut.dp.nss.batch.output.agency;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper exportu dopravce.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class AgencyBatchRowMapper implements RowMapper<AgencyBatchWrapper> {

    @Override
    public AgencyBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        AgencyBatchWrapper agency = new AgencyBatchWrapper();
        agency.setId(rs.getString("id"));
        agency.setName(rs.getString("name"));
        agency.setPhone(rs.getString("phone"));
        agency.setUrl(rs.getString("url"));

        return agency;
    }

}
