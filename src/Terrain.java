/**
 * The Terrain class is designed to represent the zones between the towns in the Treasure Hunter game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Terrain {
    // instance variables
    private String terrainName;
    private String neededItem;

    /**
     * Sets the class member variables
     *
     * @param name The name of the zone.
     * @param item The item needed in order to cross the zone.
     */
    public Terrain(String name, String item) {
        terrainName = name;
        neededItem = item.toLowerCase();
    }

    // accessors
    public String getTerrainName() {
        return terrainName;
    }

    public String getNeededItem() {
        return neededItem;
    }

    /**
     * Guards against a hunter crossing the zone without the proper item.
     * Searches the hunter's inventory for the proper item and determines whether the hunter can cross.
     *
     * @param hunter The Hunter object trying to cross the terrain.
     * @return true if the Hunter has the proper item.
     */
    public boolean canCrossTerrain(Hunter hunter) {
        if (hunter.hasItemInKit(neededItem)) {
            return true;
        }
        return false;
    }

    /**
     * @return A string representation of the terrain and item to cross it.
     */
    public String toString() {
        return terrainName + " needs a(n) " + neededItem + " to cross.";
    }
}