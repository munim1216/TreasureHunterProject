public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private String treasure;
    private boolean searched;
    private boolean toughTown;
    private boolean hasDug;

    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        hasDug = false;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        if (Math.random()<0.25){
            treasure = Colors.GREEN + "crown" + Colors.RESET;
        } else if (Math.random()<0.5){
            treasure = Colors.YELLOW + "trophy" + Colors.RESET;
        } else if (Math.random()<0.75){
            treasure = Colors.BLUE + "gem" + Colors.RESET;
        } else {
            treasure = "dust";
        }
        searched = false;
    }

    public String getLatestNews() {
        return printMessage;
    }
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + Colors.PURPLE + item + Colors.RESET + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += breakMessage(item);
            }
            return true;
        }
        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have the " + terrain.getNeededItem() + ".";
        return false;
    }

    public String dig() {
        if (!hunter.hasItemInKit("shovel")) {
            return "You can't dig for gold without a shovel!";
        }
        if (hasDug) {
            return "You already dug for gold in this town.";
        }
        if ((int)(Math.random() * 2) == 0) {
            return "You dug but only found dirt.";
        }
        int goldDug = (int) (Math.random() * 20) + 1;

        hunter.changeGold(goldDug);
        hasDug = true;
        return "You dug up " + goldDug + " gold!";
    }

    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += "Oof! Umph! Ow!\n" + Colors.RESET + "Okay, stranger! You proved yer mettle. Here, " + Colors.YELLOW + "take my gold." + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RESET + "Oof! Umph! Ow!\n" + Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! "+ Colors.YELLOW + "Now pay up!" + Colors.RESET;
                if (hunter.gold()) {
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                    hunter.changeGold(-goldDiff);
                } else {
                    printMessage += Colors.RED + "\nNot even scraps on ye. You ain't worth my time, get out." + Colors.RESET;
                    hunter.lose();
                }
            }
        }
    }
    public void purgePrintMessage() {
        printMessage = "";
    }
    public String toString() {
        return "This nice little town is surrounded by "+ terrain.getTerrainName() + ".";
    }

    public void searchGold() {
        if (searched) {
            printMessage = "You find nothing else of note.";
        } else {
            printMessage = "Oh wow. You found the " + treasure + ".\n";
            if (!hunter.hasTreasure(treasure) && !treasure.equals("dust")) {
                printMessage += "Well, you're hanging onto that.";
                hunter.addTreasure(treasure);
            } else if (treasure.equals("dust")) {
                printMessage += "...You don't want this.";
            } else {
                printMessage += "Wait, you already have this. Never mind.";
            }
        }
        searched = true;
    }
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .16) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .33) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .5) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .66) {
            return new Terrain("Desert", "Water");
        } else if (rnd < .83) {
            return new Terrain("Marsh", "Boots");
        } else {
            return new Terrain("Jungle", "Machete");
        }
    }
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    private String breakMessage(String item) {
        return switch (item) {
            case "water" -> "\nSadly, you ran out of water.";
            case "rope" -> "\nUnfortunately, your rope has broke.";
            case "machete" -> "\nUnfortunately, your machete has shattered into a million pieces.";
            case "horse" -> "\nSadly, your horse has decided to leave you.";
            case "boots" -> "\nUnfortunately, your boots have worn out and are no longer fit for wearing";
            case "boat" -> "\nSadly, your boat was stolen as you were sleeping";
            default -> throw new UnsupportedOperationException("Not a possible item");
        };
    }
}