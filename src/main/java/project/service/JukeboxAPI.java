package project.service;

import com.google.gson.JsonArray; // I like using Gson to parse my Json data, but one can essentially use any valid Json parser.
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import project.object.Jukebox;
/**
 * This is the main service layer of the application.
 * It creates the Jukebox database and takes input from {@link project.Controller} to do the desired manipulation.
 * <br>It has the following methods:
 * <br><br>{@link JukeboxAPI#JukeboxAPI()}
 * <br>{@link JukeboxAPI#getJukeboxes()}
 * <br>{@link JukeboxAPI#filterById(String)}
 * <br>{@link JukeboxAPI#filterById(ArrayList)}
 * <br>{@link JukeboxAPI#filterByModel(String)}
 * <br>{@link JukeboxAPI#filterByModel(ArrayList)}
 * <br>{@link JukeboxAPI#filterByComponent(String)}*
 * <br>{@link JukeboxAPI#filterByComponents(HashMap)}
 * <br>{@link JukeboxAPI#settingId(String)}
 * <br>{@link JukeboxAPI#filter(ArrayList, ArrayList)}
 * <br>{@link JukeboxAPI#toJSONString(ArrayList)}
 * <br><br>* not used by this project, added for project overall completeness
 * @author Shuzhao Feng
 */
@Service
public class JukeboxAPI {
    private final ArrayList<Jukebox> arr; // database containing all Jukeboxes
    private final SettingsAPI settings; // setting api
    /**
     * This is the constructor of {@link JukeboxAPI}. It does 2 things:
     * 1-Instantiate a {@link SettingsAPI},
     * 2-Read Jukebox input from the online file and convert it into an arraylist of {@link Jukebox} objects.
     * @throws IOException may occur while reading JSON data from the online source.
     * @author Shuzhao Feng
     */
    public JukeboxAPI() throws IOException {
        this.settings = new SettingsAPI();
        this.arr = new ArrayList<>();
        InputStream input = new URL("http://my-json-server.typicode.com/touchtunes/tech-assignment/jukes").openStream(); // open input stream
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)); // create buffered reader
            StringBuilder build = new StringBuilder(); // create string builder
            int index; // hold count for character index
            while ((index = reader.read()) != -1) { // while the reader is not yet empty
                build.append((char) index); // read next character
            }
            String s = build.toString(); // convert to string
            input.close(); // close input stream
            JsonArray jsonArr = JsonParser.parseString(s).getAsJsonArray(); // map the data into an array of Json elements
            for (JsonElement je : jsonArr) { // iterate through each Json element
                JsonObject jsonObj = je.getAsJsonObject(); // create Json object using the data provided
                String id = jsonObj.getAsJsonPrimitive("id").getAsString(); // find ID
                String model = jsonObj.getAsJsonPrimitive("model").getAsString(); // find model
                ArrayList<String> components = new ArrayList<>(); // create an empty arraylist to store components
                for (JsonElement e : jsonObj.getAsJsonArray("components")) { // iterate through each component piece
                    components.add(e.getAsJsonObject().getAsJsonPrimitive("name").getAsString()); // add the component to the list of components
                }
                arr.add(new Jukebox(id, model, components)); // create Jukebox object and add it to the list
            }
        } catch (Exception e) { // this is a demo assignment, so I don't want to bore anyone with all possible errors that might occur...
            e.printStackTrace();
        }
    }
    /**
     * A simple Jukebox getter method.
     * @return An arraylist of Jukeboxes.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> getJukeboxes() {
        return this.arr;
    }
    /**
     * This method takes a single ID and looks for the Jukebox that correspond to this ID.
     * @param id a string of the ID looking for.
     * @return the Jukebox corresponding to this id, null if no Jukebox was found.
     * @author Shuzhao Feng
     */
    public Jukebox filterById(String id){
        for (Jukebox box : arr) { // iterate through the database
            if (box.getId().equals(id)) { // assume ID is unique to the Jukebox
                return box; // return and break method
            }
        }
        return null; // no Jukebox was found
    }
    /**
     * This method takes multiple IDs simultaneously and looks for all Jukeboxes that correspond to those IDs.
     * @param ids an arraylist of all IDs looking for.
     * @return an arraylist of search results, null if no Jukebox was found.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> filterById(ArrayList<String> ids){
        ArrayList<Jukebox> out = new ArrayList<>(); // create an empty arraylist to store search results
        for (String id : ids){ // iterate through each ID
            Jukebox box = filterById(id); // find the corresponding Jukebox with the ID
            if (box != null){ // Jukebox was found
                out.add(box); // add the Jukebox to the result
            }
        }
        if (out.size() > 0) return out; // if found at least 1 Jukebox return result
        return null; // otherwise, return null
    }
    /**
     * This method takes a single model name and looks for all Jukeboxes that correspond to this model.
     * @param model a string of the model looking for.
     * @return an arraylist of search results, null if no Jukebox was found.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> filterByModel(String model){
        ArrayList<Jukebox> out = new ArrayList<>(); // create an empty arraylist to store search results
        for (Jukebox box : arr){  // iterate through the database
            if (box.getModel().equals(model)){ // compare model name to search keyword
                out.add(box); // add the Jukebox to the results
            }
        }
        if (out.size() > 0) return out; // if found at least 1 Jukebox return result
        return null; // otherwise, return null
    }
    /**
     * This method takes multiple model names simultaneously and looks for all Jukeboxes that correspond to those models.
     * @param models an arraylist of all models looking for.
     * @return an arraylist of search results, null if no Jukebox was found.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> filterByModel(ArrayList<String> models){
        ArrayList<Jukebox> out = new ArrayList<>(); // create an empty arraylist to store search results
        for (String model : models){ // iterate through each model name
            ArrayList<Jukebox> temp = filterByModel(model); // find all results corresponding to the model
            if (temp != null){ // Jukebox was found
                out.addAll(temp); // add models to the results
            }
        }
        if (out.size() > 0) return out; // if found at least 1 Jukebox return result
        return null; // otherwise, return null
    }
    /**
     * This method taking a component name and looks for all Jukeboxes with this component.
     * @param comp The component looking for.
     * @return An arraylist of search result, null of no Jukebox was found.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> filterByComponent(String comp){
        ArrayList<Jukebox> out = new ArrayList<>(); // create an empty arraylist to store search results
        for (Jukebox box : arr) {  // iterate through the database
            String result = box.getComponent(comp); // verify if the component is available for the Jukebox
            if (result != null){ // if found valid result
                out.add(box); // add Jukebox to the results
            }
        }
        if (out.size() > 0) return out; // if found at least 1 Jukebox return result
        return null; // otherwise, return null
    }
    /**
     * This method takes a Hashmap of requirements and find all Jukeboxes that meet those requirements.
     * Note that the number of a component also matters, as a Jukebox is considered invalid if it has a certain component,
     * but its number of that specific component does not meet the requirement.
     * @param whatWeNeed The full requirement of the Setting in a Hashmap.
     * @return An arraylist of search results, null if no Jukebox was found.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> filterByComponents(HashMap<String, Integer> whatWeNeed){
        if (whatWeNeed == null || whatWeNeed.isEmpty()) return null; // invalid input
        ArrayList<Jukebox> out = new ArrayList<>(arr); // create a new arraylist and copy the database to avoid CMException
        for (Jukebox box : arr) {  // iterate through the database
            HashMap<String, Integer> whatWeHave = box.getHashedComponents(); // get all that the Jukebox has
            for (String key : whatWeNeed.keySet()){
                // remove the Jukebox from the list when: 1-it doesn't contain the component, or 2-it contains fewer components than required
                if (whatWeHave.get(key) == null || whatWeHave.get(key) < whatWeNeed.get(key)) out.remove(box);
            }
        }
        if (out.size() > 0) return out; // if at least 1 Jukebox remains return result
        return null; // otherwise, return null
    }
    /**
     * This method calls for {@link JukeboxAPI#filterByComponents(HashMap)} and {@link SettingsAPI#getHashedRequires(String)}
     * to find Jukeboxes that meet the requirement for a specific setting.
     * @param id The setting ID.
     * @return An arraylist of all Jukeboxes satisfying the setting requirement.
     * @author Shuzhao Feng
     */
    public ArrayList<Jukebox> settingId(String id){
        return filterByComponents(this.settings.getHashedRequires(id)); // filter by components using the value of the setting id.
    }
    /**
     * This method takes two arraylists and find elements that is in both arraylists.
     * @param arr1 The first arraylist to go through.
     * @param arr2 The second arraylist to go through.
     * @return An arraylist of all Jukeboxes that are present in both lists.
     * @author Shuzhao Feng
     */
    public static ArrayList<Jukebox> filter(ArrayList<Jukebox> arr1, ArrayList<Jukebox> arr2) {
        ArrayList<Jukebox> out = new ArrayList<>(); // create empty arraylist to store results
        for (Jukebox box : arr1){ // iterate through the Jukeboxes in the first list
            if (arr2.contains(box)){ // if the Jukebox is also present in the second list
                out.add(box); // add the Jukebox to the result
            }
        }
        return out;
    }
    /**
     * This method converts an arraylist of Jukebox objects to an arraylist of Json Strings describing the objects.
     * @param boxes The arraylist of Jukebox objects to be converted.
     * @return The corresponding arraylist in Json String format.
     */
    public static ArrayList<String> toJSONString(ArrayList<Jukebox> boxes){
        ArrayList<String> out = new ArrayList<>(); // create empty arraylist to store results
        for (Jukebox box : boxes){ // iterate through the Jukeboxes
            out.add(box.toString()); // convert the Jukebox to a String and add to the result
        }
        return out;
    }
}
