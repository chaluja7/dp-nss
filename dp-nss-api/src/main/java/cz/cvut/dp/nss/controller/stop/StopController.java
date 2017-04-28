package cz.cvut.dp.nss.controller.stop;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.stop.AdminStopController;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.wrapper.output.stop.StopWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return stopService.findStopNamesByPattern(startsWith, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public StopWrapper getStop(@PathVariable("id") String id) {
        Stop stop = stopService.get(id);
        if(stop == null) throw new ResourceNotFoundException();

        return AdminStopController.getStopWrapper(stop);
    }

}
