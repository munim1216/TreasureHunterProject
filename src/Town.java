/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean hasDug;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
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
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
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

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
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
    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
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

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
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

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
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