public class Terrain {
    // instance variables
    private String terrainName;
    private String neededItem;

    public Terrain(String name, String item) {
        terrainName = name;
        neededItem = item.toLowerCase();
    }

    // accessors
    public String getTerrainName() {
        return Colors.CYAN + terrainName + Colors.RESET;
    }

    public String getNeededItem() {
        return Colors.PURPLE + neededItem + Colors.RESET;
    }
    public boolean canCrossTerrain(Hunter hunter) {
        if (hunter.hasItemInKit(neededItem)) {
            return true;
        }
        return false;
    }
    public String toString() {
        return terrainName + " needs a(n) "+ neededItem + " to cross.";
    }
}