package cz.cvut.dp.nss.controller.admin.calendar;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarFilter;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.calendarDate.CalendarExceptionType;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.wrapper.output.calendar.CalendarWrapper;
import cz.cvut.dp.nss.wrapper.output.calendarDate.CalendarDateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 12.03.17
 */
@RestController
@RequestMapping(value = "/admin/calendar")
public class AdminCalendarController extends AdminAbstractController {

    @Autowired
    private CalendarService calendarService;

    private static final String FILTER_START_DATE = "startDate";
    private static final String FILTER_END_DATE = "endDate";
    private static final String FILTER_ALL_OPTIONS = "allOptions";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CalendarWrapper>> getCalendars(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                                              @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                                              @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                                              @RequestParam(value = FILTER_ID, required = false) String id,
                                                              @RequestParam(value = FILTER_START_DATE, required = false) String startDate,
                                                              @RequestParam(value = FILTER_END_DATE, required = false) String endDate,
                                                              @RequestParam(value = FILTER_ALL_OPTIONS, required = false) Boolean allOptions) throws BadRequestException {

        List<Calendar> calendars;
        List<CalendarWrapper> wrappers = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        if(Boolean.TRUE.equals(allOptions)) {
            //allOptions vsechno prebiji a nic jineho se neaplikuje, hledaji se vsechny stanice
            calendars = calendarService.getAllOrdered();
        } else {
            final OrderWrapper order = getOrderFromHeader(xOrder);
            final CalendarFilter filter = getFilterFromParams(id, startDate, endDate);
            calendars = calendarService.getByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
            httpHeaders.add(X_COUNT_HEADER, calendarService.getCountByFilter(filter) + "");
        }

        for(Calendar calendar : calendars) {
            wrappers.add(getCalendarWrapper(calendar));
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CalendarWrapper getCalendar(@PathVariable("id") String id) {
        Calendar calendar = calendarService.get(id);
        if(calendar == null) throw new ResourceNotFoundException();

        CalendarWrapper calendarWrapper = getCalendarWrapper(calendar);
        if(calendarService.canBeDeleted(calendar.getId())) calendarWrapper.setCanBeDeleted(true);
        return calendarWrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CalendarWrapper> createCalendar(@RequestBody CalendarWrapper wrapper) throws BadRequestException {
        Calendar calendar = getCalendar(wrapper);
        calendarService.create(calendar);

        return getResponseCreated(getCalendarWrapper(calendarService.get(calendar.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CalendarWrapper updateCalendar(@PathVariable("id") String id, @RequestBody CalendarWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        Calendar existingCalendar = calendarService.get(id);
        if(existingCalendar == null) throw new ResourceNotFoundException();

        Calendar calendar = getCalendar(wrapper);
        calendar.setId(id);
        calendarService.update(calendar);

        return getCalendarWrapper(calendar);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCalendar(@PathVariable("id") String id) throws BadRequestException {
        Calendar calendar = calendarService.get(id);
        if(calendar == null) {
            //ok, jiz neni v DB
            return;
        }

        if(!calendarService.canBeDeleted(calendar.getId())) {
            throw new BadRequestException("calendar can not be deleted");
        }

        calendarService.delete(calendar.getId());
    }

    private static CalendarWrapper getCalendarWrapper(Calendar calendar) {
        if(calendar == null) return null;

        CalendarWrapper wrapper = new CalendarWrapper();
        wrapper.setId(calendar.getId());
        if(calendar.getStartDate() != null) {
            wrapper.setStartDate(calendar.getStartDate().format(DateTimeUtils.DATE_FORMATTER));
        }
        if(calendar.getEndDate() != null) {
            wrapper.setEndDate(calendar.getEndDate().format(DateTimeUtils.DATE_FORMATTER));
        }
        wrapper.setMonday(calendar.isMonday());
        wrapper.setTuesday(calendar.isTuesday());
        wrapper.setWednesday(calendar.isWednesday());
        wrapper.setThursday(calendar.isThursday());
        wrapper.setFriday(calendar.isFriday());
        wrapper.setSaturday(calendar.isSaturday());
        wrapper.setSunday(calendar.isSunday());

        List<CalendarDateWrapper> calendarDateWrappers = new ArrayList<>();
        if(calendar.getCalendarDates() != null) {
            for(CalendarDate calendarDate : calendar.getCalendarDates()) {
                calendarDateWrappers.add(getCalendarDateWrapper(calendarDate));
            }
        }
        wrapper.setCalendarDates(calendarDateWrappers);

        return wrapper;
    }

    private static CalendarDateWrapper getCalendarDateWrapper(CalendarDate calendarDate) {
        if(calendarDate == null) return null;

        CalendarDateWrapper wrapper = new CalendarDateWrapper();
        wrapper.setId(calendarDate.getId());
        if(calendarDate.getDate() != null) {
            wrapper.setDate(calendarDate.getDate().format(DateTimeUtils.DATE_FORMATTER));
        }
        wrapper.setExceptionType(calendarDate.getExceptionType() != null ? calendarDate.getExceptionType().getCode() : null);

        return wrapper;
    }

    private Calendar getCalendar(CalendarWrapper wrapper) {
        if(wrapper == null) return null;

        Calendar calendar = new Calendar();
        calendar.setId(wrapper.getId());
        if(!StringUtils.isBlank(wrapper.getStartDate())) {
            calendar.setStartDate(LocalDate.parse(wrapper.getStartDate(), DateTimeUtils.DATE_FORMATTER));
        }
        if(!StringUtils.isBlank(wrapper.getEndDate())) {
            calendar.setEndDate(LocalDate.parse(wrapper.getEndDate(), DateTimeUtils.DATE_FORMATTER));
        }
        calendar.setMonday(wrapper.isMonday());
        calendar.setTuesday(wrapper.isTuesday());
        calendar.setWednesday(wrapper.isWednesday());
        calendar.setThursday(wrapper.isThursday());
        calendar.setFriday(wrapper.isFriday());
        calendar.setSaturday(wrapper.isSaturday());
        calendar.setSunday(wrapper.isSunday());

        if(wrapper.getCalendarDates() != null) {
            for(CalendarDateWrapper calendarDateWrapper : wrapper.getCalendarDates()) {
                calendar.addCalendarDate(getCalendarDate(calendarDateWrapper));
            }
        }

        return calendar;
    }

    private CalendarDate getCalendarDate(CalendarDateWrapper wrapper) {
        if(wrapper == null) return null;

        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setId(wrapper.getId());
        if(!StringUtils.isBlank(wrapper.getDate())) {
            calendarDate.setDate(LocalDate.parse(wrapper.getDate(), DateTimeUtils.DATE_FORMATTER));
        }
        if(wrapper.getExceptionType() != null) {
            calendarDate.setExceptionType(EnumUtils.fromCode(wrapper.getExceptionType(), CalendarExceptionType.class));
        }

        return calendarDate;
    }

    private static CalendarFilter getFilterFromParams(String id, String startDate, String endDate) {
        CalendarFilter calendarFilter = new CalendarFilter();
        calendarFilter.setId(id);
        if(!StringUtils.isBlank(startDate)) {
            calendarFilter.setStartDate(LocalDate.parse(startDate, DateTimeUtils.DATE_FORMATTER));
        }
        if(!StringUtils.isBlank(endDate)) {
            calendarFilter.setEndDate(LocalDate.parse(endDate, DateTimeUtils.DATE_FORMATTER));
        }

        return calendarFilter;
    }
}
