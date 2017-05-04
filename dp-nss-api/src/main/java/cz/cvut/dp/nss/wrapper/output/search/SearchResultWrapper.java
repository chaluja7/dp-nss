package cz.cvut.dp.nss.wrapper.output.search;

import java.util.List;

/**
 * Objekt vysledku vyhledavaciho pozadavku.
 *
 * @author jakubchalupa
 * @since 24.02.17
 */
public class SearchResultWrapper {

    private String departureDate;

    private String arrivalDate;

    private Long travelTime;

    private List<SearchStopTimeWrapper> stopTimes;

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travelTime) {
        this.travelTime = travelTime;
    }

    public List<SearchStopTimeWrapper> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<SearchStopTimeWrapper> stopTimes) {
        this.stopTimes = stopTimes;
    }
}
