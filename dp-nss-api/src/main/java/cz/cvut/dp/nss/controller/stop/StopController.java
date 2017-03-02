package cz.cvut.dp.nss.controller.stop;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.services.stop.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author jakubchalupa
 * @since 02.03.17
 */
@RestController
@RequestMapping(value = "/stop")
public class StopController extends AbstractController {

    @Autowired
    private StopService stopService;

    @RequestMapping(method = RequestMethod.GET)
    public Set<String> getStopsByStartPattern(@RequestParam("startsWith") String startsWith) {
        return stopService.findStopNamesByStartPattern(startsWith);
    }

}
