package cz.cvut.dp.nss.controller.admin.agency;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyFilter;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.wrapper.output.agency.AgencyWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 12.03.17
 */
@RestController
@RequestMapping(value = "/admin/agency")
public class AdminAgencyController extends AdminAbstractController {

    @Autowired
    private AgencyService agencyService;

    private static final String FILTER_NAME = "name";
    private static final String FILTER_URL = "url";
    private static final String FILTER_PHONE = "phone";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<AgencyWrapper>> getAgencies(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                      @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                      @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                      @RequestParam(value = FILTER_ID, required = false) String id,
                                      @RequestParam(value = FILTER_NAME, required = false) String name,
                                      @RequestParam(value = FILTER_URL, required = false) String url,
                                      @RequestParam(value = FILTER_PHONE, required = false) String phone,
                                      @RequestParam(value = FILTER_SEARCH_QUERY, required = false) String searchQuery) throws BadRequestException {

        List<Agency> agencies;
        List<AgencyWrapper> wrappers = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        if(!StringUtils.isBlank(searchQuery)) {
            //searchQuery vsechno prebiji a nic jineho se neaplikuje
            agencies = agencyService.findAgenciesBySearchQuery(searchQuery);
        } else {
            final OrderWrapper order = getOrderFromHeader(xOrder);
            final AgencyFilter filter = getFilterFromParams(id, name, url, phone);
            agencies = agencyService.getByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
            httpHeaders.add(X_COUNT_HEADER, agencyService.getCountByFilter(filter) + "");
        }

        for(Agency agency : agencies) {
            wrappers.add(getAgencyWrapper(agency));
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AgencyWrapper getAgency(@PathVariable("id") String id) {
        Agency agency = agencyService.get(id);
        if(agency == null) throw new ResourceNotFoundException();

        AgencyWrapper agencyWrapper = getAgencyWrapper(agency);
        if(agencyService.canBeDeleted(agency.getId())) agencyWrapper.setCanBeDeleted(true);
        return agencyWrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AgencyWrapper> createAgency(@RequestBody AgencyWrapper wrapper) throws BadRequestException {
        Agency agency = getAgency(wrapper);
        agencyService.create(agency);

        return getResponseCreated(getAgencyWrapper(agencyService.get(agency.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AgencyWrapper updateAgency(@PathVariable("id") String id, @RequestBody AgencyWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        Agency existingAgency = agencyService.get(id);
        if(existingAgency == null) throw new ResourceNotFoundException();

        Agency agency = getAgency(wrapper);
        agency.setId(id);
        agencyService.update(agency);

        return getAgencyWrapper(agency);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteAgency(@PathVariable("id") String id) throws BadRequestException {
        Agency agency = agencyService.get(id);
        if(agency == null) {
            //ok, jiz neni v DB
            return;
        }

        if(!agencyService.canBeDeleted(agency.getId())) {
            throw new BadRequestException("agency can not be deleted");
        }

        agencyService.delete(agency.getId());
    }

    private static AgencyWrapper getAgencyWrapper(Agency agency) {
        if(agency == null) return null;

        AgencyWrapper wrapper = new AgencyWrapper();
        wrapper.setId(agency.getId());
        wrapper.setName(agency.getName());
        wrapper.setUrl(agency.getUrl());
        wrapper.setPhone(agency.getPhone());

        return wrapper;
    }

    private Agency getAgency(AgencyWrapper wrapper) {
        if(wrapper == null) return null;

        Agency agency = new Agency();
        agency.setId(wrapper.getId());
        agency.setName(wrapper.getName());
        agency.setUrl(wrapper.getUrl());
        agency.setPhone(wrapper.getPhone());

        return agency;
    }

    private static AgencyFilter getFilterFromParams(String id, String name, String url, String phone) {
        AgencyFilter agencyFilter = new AgencyFilter();
        agencyFilter.setId(id);
        agencyFilter.setName(name);
        agencyFilter.setUrl(url);
        agencyFilter.setPhone(phone);

        return agencyFilter;
    }
}
