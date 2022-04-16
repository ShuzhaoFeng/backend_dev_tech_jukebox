package project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * This is a secondary service layer of the application solely to treat setting data.
 * It creates the setting database and takes orders from {@link JukeboxAPI} to assist in selections that involves setting IDs.
 * <br>It contains the following methods:
 * <br><br>{@link SettingsAPI#SettingsAPI()}
 * <br>{@link SettingsAPI#getSettings()}*
 * <br>{@link SettingsAPI#getRequires(String)}
 * <br>{@link SettingsAPI#getRequires(ArrayList)}*
 * <br>{@link SettingsAPI#getHashedRequires(String)}
 * <br><br>* not used by this project, added for project overall completeness
 * @author Shuzhao Feng
 */
public class SettingsAPI {
    private final HashMap<String, ArrayList<String>> arr; // database containing all settings
    /**
     * This is the constructor of {@link SettingsAPI}.
     * It reads setting input from the online file and convert it into a hashmap of {id, requirement} pairs.
     * @throws IOException may occur while reading JSON data from the online source.
     * @author Shuzhao Feng
     */
    public SettingsAPI() throws IOException{
        this.arr = new HashMap<>(); // create new Hashmap to store results
        InputStream input = new URL("http://my-json-server.typicode.com/touchtunes/tech-assignment/settings").openStream(); // open input stream
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)); // create buffered reader
            StringBuilder build = new StringBuilder(); // create string builder
            int index; // hold count for character index
            while ((index = reader.read()) != -1) { // while the reader is not yet empty
                build.append((char) index); // read next character
            }
            String s = build.toString(); // convert to string
            input.close(); // close input stream
            JsonArray jsonArr = JsonParser.parseString(s).getAsJsonObject().getAsJsonArray("settings"); // map the data into an array of Json elements
            for (JsonElement je : jsonArr) { // iterate through each Json element
                JsonObject jsonObj = je.getAsJsonObject(); // create Json object using the data provided
                String id = jsonObj.getAsJsonPrimitive("id").getAsString(); // find ID
                ArrayList<String> requires = new ArrayList<>(); // create an empty arraylist to store requirements
                for (JsonElement e : jsonObj.getAsJsonArray("requires")) { // iterate through each requirement piece
                    requires.add(e.getAsString()); // add the requirement to the list of requirements
                }
                arr.put(id, requires); // create key/value pair and add it to the list
            }
        } catch (Exception e) { // this is a demo assignment, so I don't want to bore anyone with all possible errors that might occur...
            e.printStackTrace();
        }
    }
    /**
     * A simple setting getter method.
     * @return A hashmap of settings.
     * @author Shuzhao Feng
     */
    public HashMap<String, ArrayList<String>> getSettings(){
        return this.arr;
    }
    /**
     * This method takes a single ID and looks for the list of requirements that correspond to this ID.
     * @param id a key/ID looking for.
     * @return the arraylist/value corresponding to the key.
     * @author Shuzhao Feng
     */
    public ArrayList<String> getRequires(String id){
        return arr.get(id); // get value using key
    }
    /**
     * This method takes multiple IDs simultaneously and looks for all requirements that correspond to those IDs.
     * @param ids an arraylist of all IDs looking for.
     * @return an arraylist of search results, null if no value was found.
     * @author Shuzhao Feng
     */
    public ArrayList<ArrayList<String>> getRequires(ArrayList<String> ids){
        ArrayList<ArrayList<String>> out = new ArrayList<>(); // create an empty arraylist to store search results
        for (String id : ids){ // iterate through each ID
            ArrayList<String> req = getRequires(id); // find the corresponding value with the ID
            if (req != null){ // value was found
                out.add(req); // add the requirement to the result
            }
        }
        if (out.size() > 0) return out; // if found at least 1 value return result
        return null; // otherwise, return null
    }
    /**
     * This method return the Setting's requirements in form of a key/value pair to ease manipulation.
     * @return A hashmap of key/value pair where the key is the String name of the requirements,
     * and the value is the number of occurrences of this requirement in the Setting.
     * @author Shuzhao Feng
     */
    public HashMap<String, Integer> getHashedRequires(String id){
        HashMap<String, Integer> out = new HashMap<>(); // create empty hashmap to store the result
        ArrayList<String> setting = getRequires(id); // find the particular setting
        if (setting == null) return null; // id not found
        for (String s : setting){ // iterate over all requirements of the particular setting
            out.computeIfAbsent(s, s1 -> Collections.frequency(getRequires(id), s1)); // add key/value pair if not yet added
        }
        return out;
    }
}
