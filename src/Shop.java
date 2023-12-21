import java.util.Scanner;
public class Shop {
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int SHOVEL_COST = 8;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;
    private static final int BOOTS_COST = 15;
    private static final int SWORD_COST = 0;

    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private final double markdown;
    private Hunter customer;
    public Shop(double markdown) {
        this.markdown = markdown;
        customer = null; // is set in the enter method
    }
    public void enter(Hunter hunter, String buyOrSell) {
        customer = hunter;

        if (buyOrSell.equals("b")) {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, true);
            if (cost == -1) {
                System.out.println("We ain't got none of those.");
            } else if (customer.samurai()){
                buyItem(item);
                customer.buyItem(item, 0);
            }
            else {
                System.out.print("It'll cost you " + Colors.YELLOW + cost + Colors.RESET + " gold. Buy it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y")) {
                    buyItem(item);
                }
            }
        } else {
            System.out.println("What're you lookin' to sell? ");
            System.out.println("You currently have the following items: " + customer.getInventory());
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == -1) {
                System.out.println("We don't want " + Colors.PURPLE + "none of those." + Colors.RESET);
            } else if (customer.samurai()&&item.equals("sword")){
                System.out.print(Colors.RED + "...No." + Colors.RESET);
            } else {
                System.out.print("It'll get you " + Colors.YELLOW + cost + Colors.RESET + " gold. Sell it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();

                if (option.equals("y")) {
                    sellItem(item);
                }
            }
        }
    }
    public String inventory() {
        String str = "Water: "+ Colors.YELLOW + WATER_COST + Colors.RESET + " gold\n";
        str += "Rope: " + Colors.YELLOW + ROPE_COST + Colors.RESET + " gold\n";
        str += "Machete: " + Colors.YELLOW + MACHETE_COST + Colors.RESET + " gold\n";
        str += "Shovel: " + Colors.YELLOW + SHOVEL_COST + Colors.RESET + " gold\n";
        str += "Horse: " + Colors.YELLOW + HORSE_COST + Colors.RESET + " gold\n";
        str += "Boots: " + Colors.YELLOW + BOOTS_COST + Colors.RESET + " gold\n";
        str += "Boat: " + Colors.YELLOW + BOAT_COST + Colors.RESET + " gold\n";
        if (!customer.samurai()&&customer.samuraiMode()){
            str += Colors.RED + "Sword." + Colors.RESET;
        }
        return str;
    }
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)&&!customer.samurai()) {
            System.out.print("Ye' got yerself a " + item + ". Come again soon.");
        } else if (costOfItem == 0&&item.equals("sword")){
            System.out.print("The " + Colors.RED + "damn thing's" + Colors.RESET + " yours.");
            customer.buyItem(item, costOfItem);
        } else if (!customer.buyItem(item, costOfItem)&&customer.samurai()){
            System.out.println("Hmm, you don't have enough-");
            System.out.print("Ah, well, " + Colors.RED + "I guess it's on the house, stranger." + Colors.RESET);
            customer.buyItem(item);
        } else {
            System.out.print("Hmm, either you don't have enough " + Colors.YELLOW + "gold" + Colors.RESET + " or you've already got one of those!");
        }
    }
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice)&&!customer.samurai()) {
            System.out.println("Pleasure doin' business with you.");
        } else if (customer.samurai()&&!item.equals("sword")) {
            System.out.println(Colors.RED + "...this is highway robbery..." + Colors.RESET);
            customer.sellItem(item, buyBackPrice);
        } else {
            System.out.println(Colors.RED + "Stop stringin' me along!" + Colors.RESET);
        }
    }
    public int checkMarketPrice(String item, boolean isBuying) {
        if (isBuying) {
            return getCostOfItem(item);
        } else {
            return getBuyBackCost(item);
        }
    }
    public int getCostOfItem(String item) {
        return switch (item) {
            case "water" -> WATER_COST;
            case "rope" -> ROPE_COST;
            case "machete" -> MACHETE_COST;
            case "shovel" -> SHOVEL_COST;
            case "horse" -> HORSE_COST;
            case "boat" -> BOAT_COST;
            case "boots" -> BOOTS_COST;
            case "sword" -> SWORD_COST;
            default -> -1;
        };
    }
    public int getBuyBackCost(String item) {
        return (int) (getCostOfItem(item) * markdown);
    }
}