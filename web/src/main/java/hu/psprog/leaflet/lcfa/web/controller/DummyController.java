package hu.psprog.leaflet.lcfa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Dummy controller implementation for testing purposes.
 *
 * @author Peter Smith
 */
@Controller
public class DummyController {

    @GetMapping("/dummy")
    public ModelAndView dummyEndpoint() {
        return new ModelAndView("view/dummyview");
    }
}
