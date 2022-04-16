package project.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/**
 * This class defines a Jukebox object (OOP!!!).
 * An unique Jukebox is defined by its ID, model and components.
 * While ID and model cannot be change once created, the Jukebox's components can be upgraded or compromised.
 * <br>It has the following methods:
 * <br><br>{@link Jukebox#Jukebox(String, String, ArrayList)}
 * <br>{@link Jukebox#getId()}
 * <br>{@link Jukebox#getModel()}
 * <br>{@link Jukebox#getComponents()}
 * <br>{@link Jukebox#getHashedComponents()}
 * <br>{@link Jukebox#getComponent(String)}
 * <br>{@link Jukebox#addComponent(String)}*
 * <br>{@link Jukebox#removeComponent(String)}*
 * <br>{@link Jukebox#componentToJSON()}
 * <br>{@link Jukebox#equals(Object)}
 * <br>{@link Jukebox#toString()}
 * <br><br>* not used by this project, added for project overall completeness
 * @author Shuzhao Feng
 */
public class Jukebox {
    private final String id, model; // Once the Jukebox is created, its ID and model will likely never change again
    private ArrayList<String> components; // components do not need to be final assuming a Jukebox can be upgraded or compromised
    /**
     * This is the object constructor for {@link Jukebox}.
     * It takes three parameters: ID, model and components.
     * @param id An unique ID for each Jukebox.
     * @param model Defines the Jukebox's model.
     * @param components Components available on the Jukebox, e.g. LED lights, money receivers, etc.
     * @author Shuzhao Feng
     */
    public Jukebox(String id, String model, ArrayList<String> components){
        this.id = id;
        this.model = model;
        this.components = components;
    }
    /**
     * A simple ID getter method.
     * @return The Jukebox's ID.
     * @author Shuzhao Feng
     */
    public String getId(){
        return this.id;
    }
    /**
     * A simple model getter method.
     * @return The Jukebox's model.
     * @author Shuzhao Feng
     */
    public String getModel(){
        return this.model;
    }
    /**
     * A simple component getter method.
     * @return An arraylist of components.
     * @author Shuzhao Feng
     */
    public ArrayList<String> getComponents(){
        return this.components;
    }
    /**
     * This method return the Jukebox's components in form of a key/value pair to ease manipulation.
     * @return A hashmap of key/value pair where the key is the String name of the components,
     * and the value is the number of occurrences of this component in the Jukebox.
     * @author Shuzhao Feng
     */
    public HashMap<String, Integer> getHashedComponents(){
        HashMap<String, Integer> out = new HashMap<>(); // create empty hashmap to store the result
        for (String s : getComponents()){ // iterate over all components
            out.computeIfAbsent(s, s1 -> Collections.frequency(getComponents(), s1)); // add key/value pair if not yet added
        }
        return out;
    }
    /**
     * This method checks whether a component exist for the Jukebox.
     * <br> (I could also implement it to return true or false, I chose to return String instead because I like it better)
     * @param component the component to check.
     * @return the component itself if it is found, null otherwise.
     * @author Shuzhao Feng
     */
    public String getComponent(String component){
        for (String s : components){ // iterate through all components
            if (component.trim().equalsIgnoreCase(s)){ // compare the two strings, ignore cases
                return s; // component found, return and break method
            }
        } // component not found
        return null;
    }
    /**
     * This method adds a component to an existing Jukebox.
     * @param component The component to be added.
     * @return The component itself if it is successfully added, null if the input does not contain any valid character.
     * @author Shuzhao Feng
     */
    public String addComponent(String component){
        if (component == null || component.trim().isEmpty()) return null; // invalid input, unsuccessful exit
        this.components.add(component.trim().toLowerCase()); // add component to the list
        return getComponent(component); // check for successful exit
    }
    /**
     * This method removes the first component with the input component name to an existing Jukebox.
     * @param component The component to be removed.
     * @return The component itself if it is successfully removed, null if:
     * <br> - the input does not contain any valid character.
     * <br> - the component doesn't exist.
     * @author Shuzhao Feng
     */
    public String removeComponent(String component){
        if (component == null || component.length() < 1) return null; // invalid input, unsuccessful exit
        if (getComponent(component) == null){ // component does not exist
            return null; // unsuccessful exit
        } // component already exists
        this.components.remove(component); // remove component from the list
        return component; // successful exit
    }
    /**
     * This method convert the Jukebox's components into a Json formatted String.
     * @return A list of all components in Json format, converted to String.
     * @author Shuzhao Feng
     */
    public String componentToJSON() {
        StringBuilder str = new StringBuilder("[");
        for (String comp : getComponents()) {
            str.append("\n      {").append("\n        \"name\": \"").append(comp).append("\"").append("\n      }");
            if (comp != this.components.get(this.components.size()-1)) str.append(","); // if not last item, append comma
        }
        str.append("\n    ]");
        return str.toString();
    }
    /**
     * This method checks whether the Jukebox is equivalent to another object.
     * @param obj The object to compare with.
     * @return - true, if they either point to the same location, or they have identical ID, model and components.
     * <br> - false, if the input is not a Jukebox object, or the two Jukebox objects either differ in ID, model, components or multiple of these 3.
     * @author Shuzhao Feng
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // true if they point to the same location
        if (!(obj instanceof Jukebox box)) return false; // false if the object is not a Jukebox object
        return this.getId().equals(box.getId()) && this.getModel().equals(box.getModel()) && this.getComponents().equals(box.getComponents());
        // compare ID, model and components
    }
    /**
     * This method allows a Jukebox object to be printed out as an understandable message for a human.
     * @return a Json String of the Jukebox.
     * @author Shuzhao Feng
     */
    @Override
    public String toString() {
        return "\n  {" +
                "\n    \"id\": \"" + id + '\"' +
                ",\n    \"model\": \"" + model + '\"' +
                ",\n    \"components\": " + componentToJSON() +
                "\n  }";
    }
}
