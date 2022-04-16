import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.object.Jukebox;
import project.service.JukeboxAPI;
import project.service.SettingsAPI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class. Running it will perform some JUnit test on the application.
 * @author Shuzhao Feng
 */
class JUnitTest {
    JukeboxAPI juke;
    SettingsAPI set;
    /**
     * Create a new database before each independent test.
     * @throws IOException might occur when reading files online
     * @author Shuzhao Feng
     */
    @BeforeEach
    void set() throws IOException{
        this.juke = new JukeboxAPI();
        this.set = new SettingsAPI();
    }
    /**
     * Test databases set up conditions.
     * @author Shuzhao Feng
     */
    @Test
    void apiSetUp(){
        assertNotNull(juke.getJukeboxes()); // database must exist
        assertFalse(juke.getJukeboxes().isEmpty()); // database must not be empty
        assertNotNull(set.getSettings());
        assertFalse(set.getSettings().isEmpty());
    }
    /**
     * Test set up condition for a Jukebox object.
     * @author Shuzhao Feng
     */
    @Test
    void objectSetUp(){
        Random random = new Random(); // randomizer
        String[] components = new String[]{"comp1","comp2","comp3","comp4","comp5"}; // set of components
        ArrayList<String> comps = new ArrayList<>(); // components to be added
        for (int i = 0; i < 3; i++){ // add 3 random components to the Jukebox
          comps.add(components[random.nextInt(components.length)]);
        }
        Jukebox box1 = new Jukebox("id","model", comps); // create object
        assertEquals(box1.getId(), "id");
        assertEquals(box1.getModel(), "model");
        assertEquals(box1.getComponents(), comps);
    }
    /**
     * Test most filters that will be used by the application.
     * @author Shuzhao Feng
     */
    @Test
    void filters(){
        assertNull(juke.filterById("Not a valid ID")); // invalid id
        ArrayList<String> ids = new ArrayList<>(List.of(new String[]{"we", "totally", "are", "valid", "IDs","!!!"}));
        assertNull(juke.filterById(ids)); // invalid ids
        assertNull(juke.filterByModel("Not a valid model")); // invalid model
        ArrayList<String> models = new ArrayList<>(List.of(new String[]{"how", "about", "models", "instead", "?"}));
        assertNull(juke.filterByModel(models)); // invalid models
        assertNull(juke.settingId("Not a valid setting ID")); // invalid setting id
        assertNull(juke.filterByComponents(new HashMap<>())); // input is empty
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("This is not a component", 1);
        assertNull(juke.filterByComponents(hash)); // input is not a valid component
        hash = new HashMap<>();
        hash.put("pcb",99);
        assertNull(juke.filterByComponents(hash)); // no jukebox has 99 PCBs
        assertTrue(JukeboxAPI.filter(juke.getJukeboxes(), new ArrayList<>()).isEmpty()); // filter a list with an empty list returns an empty list
        assertEquals(JukeboxAPI.filter(juke.getJukeboxes(), juke.getJukeboxes()), juke.getJukeboxes()); // filter 2 identical lists return the same list
    }
}
