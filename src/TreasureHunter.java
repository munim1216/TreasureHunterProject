import java.util.Scanner;

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;

    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    private void welcomePlayer() {
        System.out.println("Welcome to " + Colors.YELLOW + "TREASURE HUNTER!" + Colors.RESET);
        System.out.println("Going hunting for the " + Colors.YELLOW + "big treasure, eh?"+ Colors.RESET);
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        System.out.print("Hard mode? (y/n): ");
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("y")) {
            hardMode = true;
            hunter = new Hunter(name, 10);
        } else if (hard.equals("test")) {
            hunter = new Hunter(name);
        } else if (hard.equals("s")){
            hunter = new Hunter(name, 10);
            hunter.startSamurai();
        } else {
            hunter = new Hunter(name, 10);
        }
    }

    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            markdown = 0.25;
            toughness = 0.75;
        }
        Shop shop = new Shop(markdown);
        currentTown = new Town(shop, toughness);
        currentTown.hunterArrives(hunter);
    }
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x")&&!hunter.treasures()) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            currentTown.purgePrintMessage();
            System.out.println("***");
            System.out.println(hunter);
            //this one is important (prints your stuff)
            if (hunter.loseCond()) {
                System.out.println(currentTown);
                if (hunter.gold()) {
                    System.out.println(Colors.YELLOW + "(B)uy something at the shop.");
                }
                if (!hunter.kitIsEmpty()) {
                    System.out.println(Colors.PURPLE + "(S)ell something at the shop.");
                }
                System.out.println(Colors.CYAN + "(M)ove on to a different town.");
                System.out.println(Colors.GREEN + "(H)unt for treasure!");
                System.out.println(Colors.RED + "(L)ook for trouble!");
                System.out.println(Colors.YELLOW + "(D)ig for gold!");
            }
            System.out.println(Colors.RESET + "Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
        if (hunter.treasures()){
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println("Wait, holy shit.");
            System.out.println("You found" + hunter.getTreasures() + ".");
            System.out.println(Colors.YELLOW + "You did it. You actually did it.");
            System.out.println(Colors.GREEN + "You win. You're done. " + Colors.RED + "The hunt is over." + Colors.RESET);
            System.out.println("Great job, " + hunter.getHunterName() + ".");
        }
    }
    private void processChoice(String choice) {
        if ((choice.equals("b") || (choice.equals("s")&&!hunter.kitIsEmpty()))&&hunter.loseCond()) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown() && hunter.loseCond()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l") && hunter.loseCond()) {
            currentTown.lookForTrouble();
        }else if (choice.equals("h")&&hunter.loseCond()){
            currentTown.searchGold();
        } else if (choice.equals("x")) {
            System.out.println("You forfeit, " + hunter.getHunterName() + ". Goodbye.");
        } else if (choice.equals("d")){
            System.out.println(currentTown.dig());
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}