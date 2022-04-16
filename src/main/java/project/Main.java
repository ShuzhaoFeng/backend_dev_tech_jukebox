package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 * This is the main entry point of the program. It does only 2 things:
 * start the application, and calls for {@link Controller} to receive queries.
 * <br>It has the following methods:
 * <br><br>{@link Main#main}
 * <br>{@link Main#home}
 * @author Shuzhao Feng
 */
@SpringBootApplication
@RestController
public class Main {
    /**
     * The only psvm you need to run the program!
     * @author Shuzhao Feng
     */
    public static void main(String[] args){
        SpringApplication.run(Controller.class, args);
    }
    /**
     * This methods displays a pre-written HTML page at the home page.
     * @return A pre-written HTML page stored in resources.static.
     * @author Shuzhao Feng
     */
    @GetMapping()
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView(); // create viewer
        modelAndView.setViewName("help.html"); // navigate to the project
        return modelAndView;
    }
}
