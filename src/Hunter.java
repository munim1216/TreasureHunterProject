public class Hunter {
    //instance variables
    private String hunterName;
    private final String[] kit = new String[6];
    private final String[] treasure = new String[3];
    private int gold;
    private boolean lose;

    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        // only 5 possible items can be stored in kit
        gold = startingGold;
        lose = true;
    }
    public Hunter(String hunterName){
        this.hunterName = hunterName;
        kit[0] = "water";
        kit[1] = "rope";
        kit[2] = "machete";
        kit[3] = "horse";
        kit[5] = "boat";
        kit[4] = "boots";
        gold = 100;
        lose = true;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            gold = 0;
        }
    }
    public boolean gold(){
        return gold > 0;
    }
    public void lose(){
        lose = false;
    }
    public boolean loseCond(){
        return lose;
    }
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem == 0 || gold < costOfItem || hasItemInKit(item)) {
            return false;
        }

        gold -= costOfItem;
        addItem(item);
        return true;
    }
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit(kit);
            kit[idx] = item;
            return true;
        }
        return false;
    }
    public boolean addTreasure(String item){
        if (!hasTreasure(item)) {
            int idx = emptyPositionInKit(treasure);
            treasure[idx] = item;
            return true;
        }
        return false;
    }
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }
    public boolean hasTreasure(String item){
        for (String tmpItem : treasure) {
            if (item.equals(tmpItem)) {
                return true;
            }
        }
        return false;
    }
    public String getInventory() {
        String whatever = Colors.PURPLE + getInv(kit) + Colors.RESET;
        whatever += getInv(treasure);
        return whatever;
    }
    private String getInv(String[] a){
        String printableKit = "";
        for (String item : a) {
            if (item != null) {
                printableKit += item + ", " ;
            }
        }
        if (!printableKit.isEmpty()){
            printableKit = printableKit.substring(0, printableKit.length()-2);
        }

        return printableKit;
    }
    public String toString() {
        String str = "You have " + Colors.YELLOW + gold + Colors.RESET + " gold";
        if (!lose) {
            str = "You have nothing";
        }
        if (!kitIsEmpty()&&lose) {
            str += " and " + getInventory();
        }
        str += ".";
        return str;
    }
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];
            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }
    public boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }
    private boolean treasures() {
        for (String string : treasure) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }
    private int emptyPositionInKit(String[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                return i;
            }
        }
        return -1;
    }

}