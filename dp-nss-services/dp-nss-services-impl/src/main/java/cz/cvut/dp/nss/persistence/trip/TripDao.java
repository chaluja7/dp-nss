package cz.cvut.dp.nss.persistence.trip;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.trip.Trip;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of TripDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Repository
public class TripDao extends AbstractGenericJpaDao<Trip, String> {

    public TripDao() {
        super(Trip.class);
    }

}
