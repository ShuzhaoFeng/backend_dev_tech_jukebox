package project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import project.service.JukeboxAPI;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * The Controller class serves as the main controller for the API program.
 * All query parameters received will be taken by the controller and sent to {@link JukeboxAPI}
 * for processing result. The result will be returned to the controller who will send the result through the endpoint.
 * It contains the following methods:
 * <br><br>{@link Controller#Controller(JukeboxAPI)}
 * <br>{@link Controller#cropByLimit(String, int)}
 * <br>{@link Controller#init(Integer, Integer)}
 * <br>And a series of similar methods treating query parameters (I could've merged them into 1 big method, but I feel like it wouldn't be too readable)
 * @author Shuzhao Feng
 */
@SpringBootApplication
@RestController
@RequestMapping(path="/api")
public class Controller {
    private final JukeboxAPI api;
    /**
     * This is the constructor for the {@link Controller} class.
     * It is autowired with a JukeboxAPI object that will be automatically instantiated once started.
     * @param api Jukebox API, used to manipulate data and do selections.
     * @author Shuzhao Feng
     */
    @Autowired
    public Controller(JukeboxAPI api) {
        this.api = api;
    }
    /**
     * This method crops a String by a line number limit. Everything above the line limit will be deleted.
     * @param s The initial String.
     * @param limit Line limit.
     * @return Lines 1 to limit of the initial String.
     * @author Shuzhao Feng
     */
    private static String cropByLimit(String s, int limit){
        if (s.lines().count() > limit){ // if the number of lines exceeds the limit
            Object[] obj = s.lines().toArray(); // separate the String into array
            String[] temp = new String[obj.length]; // create a temporary array of Strings (as direct downcast is not allowed)
            for (int i = 0; i < obj.length; i++) { // downcast every object into String
                temp[i] = (String) obj[i];
            }
            s = ""; // empty String
            for (int i =0; i < limit-1; i++){ // concatenate everything back to the String up to the limit
                s = s.concat(temp[i]+"\n");
            }
            s = s.concat(temp[limit-1]); // concatenate last line without line break
        } // if the number of lines doesn't exceed the limit, do nothing
        return s;
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping()
    @ResponseBody
    public String init(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(api.getJukeboxes()).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = "id")
    @ResponseBody
    public String id(@RequestParam(value = "id") String[] id, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+api.filterById(new ArrayList<>(Arrays.asList(id))).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = "settingid")
    @ResponseBody
    public String settingId(@RequestParam(value = "settingid") String id, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(api.settingId(id)).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = "model")
    @ResponseBody
    public String model(@RequestParam(value = "model") String[] model, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(api.filterByModel(new ArrayList<>(Arrays.asList(model)))).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = {"model","settingid"})
    @ResponseBody
    public String modelAndSettingId(@RequestParam(value = "model") String[] model, @RequestParam(value = "settingid") String id, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(JukeboxAPI.filter(api.filterByModel(new ArrayList<>(Arrays.asList(model))), api.settingId(id))).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = {"id","settingid"})
    @ResponseBody
    public String idAndSettingId(@RequestParam(value = "id") String[] id, @RequestParam(value = "settingid") String sid, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(JukeboxAPI.filter(api.filterById(new ArrayList<>(Arrays.asList(id))), api.settingId(sid))).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = {"id","model"})
    @ResponseBody
    public String idAndModel(@RequestParam(value = "id") String[] id, @RequestParam(value = "model") String[] model, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(JukeboxAPI.filter(api.filterById(new ArrayList<>(Arrays.asList(id))), api.filterByModel(new ArrayList<>(Arrays.asList(model))))).toString(), limit);
    }
    /**
     * This method is one of the methods to support query parameters on the endpoint.
     * @return A Json-format list of all Jukeboxes that satisfies the query parameters, converted to a single String.
     * @author Shuzhao Feng
     */
    @GetMapping(params = {"id","model","settingid"})
    @ResponseBody
    public String idSettingIdAndModel(@RequestParam(value = "id") String[] id, @RequestParam(value = "model") String[] model, @RequestParam(value = "settingid") String sid, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        if (offset == null) offset = 0; // prevent NPException
        if (limit == null) limit = (int) Double.POSITIVE_INFINITY;
        return Controller.cropByLimit("\n".repeat(offset)+JukeboxAPI.toJSONString(JukeboxAPI.filter(api.settingId(sid),JukeboxAPI.filter(api.filterById(new ArrayList<>(Arrays.asList(id))), api.filterByModel(new ArrayList<>(Arrays.asList(model)))))).toString(), limit);
    }
}
