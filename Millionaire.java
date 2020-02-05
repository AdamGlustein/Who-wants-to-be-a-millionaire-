import java.util.*;
/*
 * Who Wants to Be a Millionaire: By Adam Glustein 
 * Due May 30th, 2018 for ICS3UP-D1
 * Purpose: to synthesize our understanding of programming structures learned throughout Grade 11 Computer Science such as decisions, loops, arrays, 
 * and randomization by designing a functional video game for our culminating activity.
 */
public class Millionaire
{
    private static int round = 1;
    private static boolean is5050Available = true; // these 3 variables track if lifelines have been used 
    private static boolean isPAFAvailable = true;
    private static boolean isATAAvailable = true;
    private static boolean hasWon = false; // these 3 variables track if the game is over 
    private static boolean hasLost = false;
    private static boolean hasWalked = false;
    private static boolean aEliminated = false; // these 4 variables are used for 50:50
    private static boolean bEliminated = false;
    private static boolean cEliminated = false;
    private static boolean dEliminated = false;
    private static int round5050WasUsed; 
    private static char correctAnswer;
    private static String question;
    private static String optionA = ""; // these 4 contain each option for the question being answered
    private static String optionB = "";
    private static String optionC = "";
    private static String optionD = "";
    private static String balance = "$0"; // their winnings up to that point

    public static void main(String[] args) throws InterruptedException // runs the game 
    {
        Scanner sc = new Scanner(System.in);
        setupScreen(); // asks the user to resize their window

        while(true) // the game itself; when the while loop reaches the end, the game returns to the title screen
        {
            int mode = titleScreen();            
            if(mode == 1) // the player chooses to play
            {
                printIntro();
                while(!hasWon && !hasLost && !hasWalked) // loops once per question
                {
                    printRoundDisplay();
                    String[] questArray = questionGenerator(round);
                    questionSetup(questArray);
                    printQuestionLayout();
                    int action = playerAction(); // returns an int based on what the player chose to do 
                    while(action == 3 || action == 4 || action == 5) // lifeline used
                    {
                        if(action == 3) // 50:50
                        {
                            is5050Available = false; // they have used the lifeline so they cannot use it again
                            questArray = fiftyFifty(questArray); // deletes two options to create new question array
                            round5050WasUsed = round; // tracks the round that 50:50 was used for the Ask the Audience method
                            printQuestionLayout();
                            action = playerAction();
                        }
                        else if(action == 4) // phone a friend
                        {                            
                            isPAFAvailable = false;
                            phoneFriend(questArray); // runs phone a friend method, separate from main
                            printQuestionLayout();
                            action = playerAction();
                        }
                        else if(action == 5) // ask the audience
                        {                            
                            isATAAvailable = false;
                            askTheAudience(questArray); // prints the graph of results 
                            printQuestionLayout();
                            action = playerAction();
                        }
                    }

                    if(action == 0) // correct answer
                    {
                        printQuestionResult(true, questArray[2]);
                        round++;
                        updateBalance();
                    }
                    else if(action == 1) // incorrect answer
                    {
                        printQuestionResult(false, questArray[2]);
                        hasLost = true;
                        updateBalance();
                    }
                    else if(action == 2) // player walks
                    {
                        hasWalked = true;
                    }

                    if(round == 16) // the player has correctly answered question 15
                    {
                        hasWon = true;
                    }
                }
                printGameOverScreen();
                gameReset();
            }
            else // the player chooses to view the rulebook
            {
                printRules();
            }
        }
    }

    public static void setupScreen() // just the setup screen to enhance user experience

    {
        Scanner sc = new Scanner(System.in);
        clear();

        System.out.println("|-------------------------------------------------------------------------------------------------------------|");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                 Please resize your window to these dimensions.                              |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                             Press enter to continue.                                        |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                        By Adam Glustein and Erik Kreem                                      |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                    ICS3UP                                                   |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                                                                                             |");
        System.out.println("|-------------------------------------------------------------------------------------------------------------|");

        sc.nextLine(); // press enter to continue 
    }

    public static int titleScreen() // prints title screen, returns an int based on what the user choosed to do (Play, Rules, or Exit)
    {
        Scanner sc = new Scanner(System.in);
        clear();
        System.out.println();
        indent(); System.out.println("                                              _______");
        indent(); System.out.println("                                        _____/       \\_____");
        indent(); System.out.println("                                   ____/                   \\____");
        indent(); System.out.println("                               ___/                             \\___");
        indent(); System.out.println("                           ___/                  T                  \\___");
        indent(); System.out.println("                       ___/                N           S                \\___");
        indent(); System.out.println("                    __/              A                                      \\__");
        indent(); System.out.println("                 __/             W                               T             \\__");
        indent(); System.out.println("              __/                                                    O            \\__");
        indent(); System.out.println("           __/          O                                                            \\__");
        indent(); System.out.println("         _/                                                                B            \\_");
        indent(); System.out.println("       _/          H                                                          E           \\_");
        indent(); System.out.println("     _/                                                                                     \\_");
        indent(); System.out.println("   _/          W                                                                  A           \\_");
        indent(); System.out.println(" _/                                                                                             \\_");
        indent(); System.out.println("/         |\\        /| --|-- |      |      --|--  /-\\   |\\    |    /\\    --|-- |-\\  |-----        \\");
        indent(); System.out.println("|         | \\      / |   |   |      |        |   /   \\  | \\   |   /  \\     |   |  | |             |");
        indent(); System.out.println("|         |  \\    /  |   |   |      |        |  |     | |  \\  |  /----\\    |   |_/  |---          |");
        indent(); System.out.println("|         |   \\  /   |   |   |      |        |   \\   /  |   \\ | /      \\   |   | \\  |             |");
        indent(); System.out.println("\\_        |    \\/    | __|__ |_____ |_____ __|__  \\_/   |    \\|/        \\__|__ |  \\ |_____       _/"); 
        indent(); System.out.println("  \\_                                                                                           _/");
        indent(); System.out.println("    \\_         W                                                                  A          _/");
        indent(); System.out.println("      \\_                                                                                   _/");
        indent(); System.out.println("        \\_         H                                                          E          _/");
        indent(); System.out.println("          \\__                                                              B          __/");
        indent(); System.out.println("             \\__        O                                                          __/");
        indent(); System.out.println("                \\__                                                  O          __/");
        indent(); System.out.println("                   \\__           W                               T           __/");
        indent(); System.out.println("                      \\___           A                                   ___/");
        indent(); System.out.println("                          \\___             N           S             ___/");
        indent(); System.out.println("                              \\___               T               ___/");
        indent(); System.out.println("                                  \\____                     ____/");
        indent(); System.out.println("                                       \\_____         _____/");
        indent(); System.out.println("                                             \\_______/");
        indent(); System.out.println();
        indent(); System.out.println("                                          Enter 1 to play");
        indent(); System.out.println("                                       Enter 2 to view rules");
        indent(); System.out.println("                                          Enter 3 to exit");

        String input = sc.nextLine();
        while(!(input.equals("1") || input.equals("2") || input.equals("3"))) // invalid input loop
        {
            clear();
            System.out.println();
            indent(); System.out.println("                                              _______");
            indent(); System.out.println("                                        _____/       \\_____");
            indent(); System.out.println("                                   ____/                   \\____");
            indent(); System.out.println("                               ___/                             \\___");
            indent(); System.out.println("                           ___/                  T                  \\___");
            indent(); System.out.println("                       ___/                N           S                \\___");
            indent(); System.out.println("                    __/              A                                      \\__");
            indent(); System.out.println("                 __/             W                               T             \\__");
            indent(); System.out.println("              __/                                                    O            \\__");
            indent(); System.out.println("           __/          O                                                            \\__");
            indent(); System.out.println("         _/                                                                B            \\_");
            indent(); System.out.println("       _/          H                                                          E           \\_");
            indent(); System.out.println("     _/                                                                                     \\_");
            indent(); System.out.println("   _/          W                                                                  A           \\_");
            indent(); System.out.println(" _/                                                                                             \\_");
            indent(); System.out.println("/         |\\        /| --|-- |      |      --|--  /-\\   |\\    |    /\\    --|-- |-\\  |-----        \\");
            indent(); System.out.println("|         | \\      / |   |   |      |        |   /   \\  | \\   |   /  \\     |   |  | |             |");
            indent(); System.out.println("|         |  \\    /  |   |   |      |        |  |     | |  \\  |  /----\\    |   |_/  |---          |");
            indent(); System.out.println("|         |   \\  /   |   |   |      |        |   \\   /  |   \\ | /      \\   |   | \\  |             |");
            indent(); System.out.println("\\_        |    \\/    | __|__ |_____ |_____ __|__  \\_/   |    \\|/        \\__|__ |  \\ |_____       _/"); 
            indent(); System.out.println("  \\_                                                                                           _/");
            indent(); System.out.println("    \\_         W                                                                  A          _/");
            indent(); System.out.println("      \\_                                                                                   _/");
            indent(); System.out.println("        \\_         H                                                          E          _/");
            indent(); System.out.println("          \\__                                                              B          __/");
            indent(); System.out.println("             \\__        O                                                          __/");
            indent(); System.out.println("                \\__                                                  O          __/");
            indent(); System.out.println("                   \\__           W                               T           __/");
            indent(); System.out.println("                      \\___           A                                   ___/");
            indent(); System.out.println("                          \\___             N           S             ___/");
            indent(); System.out.println("                              \\___               T               ___/");
            indent(); System.out.println("                                  \\____                     ____/");
            indent(); System.out.println("                                       \\_____         _____/");
            indent(); System.out.println("                                             \\_______/");
            indent(); System.out.println();
            indent(); System.out.println("                                   Please enter either 1 to play,");
            indent(); System.out.println("                                   2 to view rules, or 3 to exit");
            input = sc.nextLine();
        }
        if(input.equals("1"))
            return 1;
        else if(input.equals("2"))
            return 2;
        else
        {
            clear();
            System.exit(0);
        }
        return -1; // to avoid a syntax error; will never be returned
    }

    public static void printRules() // displays the rulebook
    {
        Scanner sc = new Scanner(System.in);
        clear();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        indent();indent();
        System.out.println("This is \"Who Wants to be a Millionaire\", the greatest game show in history, where");
        indent();indent();
        System.out.println("contestants play for the kind of life-changing money most people can only dream of!");
        System.out.println();
        indent();indent();
        System.out.println("On the show, you'll be faced with a series of 15 multiple-choice trivia questions.");
        indent();indent();
        System.out.println("Answering each one correctly wins you money and takes you one step closer to the");
        indent();indent();
        System.out.println("elusive million-dollar prize.");
        System.out.println();
        indent();indent();
        System.out.println("However, if you answer even a single question wrong, you will forfeit your winnings");
        indent();indent();
        System.out.println("up to that point, and you'll be off the show. If you ever wish to walk away with the");
        indent();indent();
        System.out.println("money you've earned so far, you're always able to, even after a question is given to");
        indent();indent();
        System.out.println("you. But walk away, and your shot at the million is gone.");
        System.out.println();
        indent();indent();
        System.out.println("You have three lifelines at your disposal, each of which can be utilized a single time:");
        System.out.println();
        indent();indent();indent();
        System.out.println("50:50 allows you to eliminate two wrong answers, leaving only");
        indent();indent();indent();
        System.out.println("the correct answer and one other possibility.");
        System.out.println();
        indent();indent();indent();
        System.out.println("Phone a Friend allows you to call on one of your friends for help.");
        indent();indent();indent();
        System.out.println("They may know the answer, they may not.");
        System.out.println();
        indent();indent();indent();
        System.out.println("Ask the Audience allows you to poll the members of tonight's audience.");
        indent();indent();indent();
        System.out.println("Do you trust them?");
        System.out.println();
        indent();indent();
        System.out.println("Reach either of the two thresholds - $1,000 at question 5, and $32,000 at question 10 - ");
        indent();indent();
        System.out.println("and that money is yours to keep. Answer a future question wrong, and you'll fall back");
        indent();indent();
        System.out.println("to that amount.");
        System.out.println();
        indent();indent();
        System.out.println("Good luck - are you ready to play for the million?");
        System.out.println();
        indent();indent();
        System.out.println("Press enter to return to the title screen...");

        sc.nextLine();
    }

    public static void printIntro() throws InterruptedException // prints the intro in a timed manner
    {
        Scanner sc = new Scanner(System.in);

        clear();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        indent();indent();
        System.out.println("Enter 1 to view the Intro. Enter 2 to skip."); // gives user the option to skip the intro, as it can get quite annoying
        String input = sc.nextLine();
        while(!(input.equals("1") || input.equals("2")))
        {
            clear();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Please enter either 1 to view intro sequence, or 2 to skip.");
            input = sc.nextLine();
        }

        if(input.equals("1")) // they want to see the intro
        {
            clear();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            Thread.sleep(2000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            Thread.sleep(1000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|");
            System.out.println("                                                          |  50:50  |");
            System.out.println("                                                          |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50,");
            Thread.sleep(1000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |");
            System.out.println("                                                          |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend,");
            Thread.sleep(1000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.print("50:50, phone a friend, ask the audience,");
            Thread.sleep(1000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.print("50:50, phone a friend, ask the audience,");
            System.out.println(" it's all there for you...");
            Thread.sleep(2000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            System.out.println();
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(500);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            System.out.println();
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            System.out.println();
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            System.out.println();
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            System.out.println();
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|                               |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(300);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            Thread.sleep(1000);

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            System.out.println();
            indent();indent();
            System.out.println("Are you ready? Let's play \"Who Wants to be a Millionaire!\"");

            clear();
            System.out.println();
            System.out.println();
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Welcome to \"Who Wants to be a Millionaire\"!");
            System.out.println();
            indent();indent();
            System.out.println("You know about the rules, you know about the lifelines,");
            indent();indent();
            System.out.println("50:50, phone a friend, ask the audience, it's all there for you...");
            System.out.println();
            indent();indent();
            System.out.println("Fifteen questions gets you to the million...");
            indent();indent();
            System.out.println("Checkpoints at $1,000 and $32,000... you reach those, you're not going home with less...");
            System.out.println();
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|    1                $100      |");
            chartSpacing(); System.out.println("|-------------------------------|");
            System.out.println();
            indent();indent();
            System.out.println("Are you ready? Let's play \"Who Wants to be a Millionaire!\"");
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println("Press enter to continue...");

            sc.nextLine();
        }
    }    

    public static void printRoundDisplay() // prints the display screen before each question is asked
    {
        // uses the "round" instance variable
        Scanner sc = new Scanner(System.in);
        clear();
        System.out.println();
        System.out.println();
        printLifelines();
        System.out.println();
        System.out.println();
        if(round == 1) // doesn't print the bank balance in round 1
        {
            indent();indent();
            System.out.println("This is your round 1 question. Answer incorrectly, and you walk away with nothing.");
            System.out.println();
            System.out.println();
        }
        else if(round == 15) // million-dollar question
        {
            indent();indent();
            System.out.println("Congratulations - you have made it to the final round!");
            indent();indent();
            System.out.println("You have much to gain, but everything to lose.");
            System.out.println();
            indent();indent();
            System.out.println("Answer correctly, and you win it all, but answer incorrectly");
            indent();indent();
            System.out.println("and you fall all the way back down to $32,000.");
            System.out.println();
            indent();indent();
            System.out.println("Here is your one-million-dollar question.");
        }
        else if(round == 6 || round == 11) // player just passed a checkpoint question
        {
            indent();indent();
            System.out.println("Congratulations - you have made it to round " + round + ".");
            System.out.println();
            indent();indent();
            System.out.println("Answer incorrectly, and you still leave with your " + balance + ",");
            indent();indent();
            System.out.println("so there's no reason not to go for it on this one.");
            round += 1; // this allows for the retrieval of the value of the next question, by calling updateBalance on a round one greater and then later returning the balance to
            updateBalance(); // the correct current bank balance
            round -= 1;
            System.out.println();
            indent();indent();
            System.out.println("Here is your " + balance + " question.");
            updateBalance();
        }
        else // rounds 2 to 14
        {
            indent();indent();
            System.out.println("Congratulations - you have made it to round " + round + ".");
            System.out.println();
            indent();indent();
            System.out.println("Walk away now, and you leave with " + balance + ".");
            indent();indent();
            if(round < 6)
            {
                System.out.println("Answer incorrectly, and you walk away with nothing.");
            }
            else if(round < 11) // $1,000 checkpoint
            {
                System.out.println("Answer incorrectly, and you fall back down to $1,000.");
            }
            else // $32,000 checkpoint
            {
                System.out.println("Answer incorrectly, and you fall back down to $32,000.");
            }
            round += 1; // this allows for the retrieval of the value of the next question, by calling updateBalance on a round one greater and then later returning the balance to
            updateBalance(); // the correct current bank balance
            round -= 1;
            System.out.println();
            indent();indent();
            System.out.println("Here is your " + balance + " question.");
            updateBalance();
        }
        System.out.println();
        printQuestionChart();
        System.out.println();
        System.out.println();
        indent();indent();
        System.out.println("Press enter to continue...");
        sc.nextLine();
    }

    public static void printLifelines() // prints lifelines in upper right corner
    {
        // is5050Available, isPAFAvailable, isATAAvailable
        if(is5050Available && isPAFAvailable && isATAAvailable) // all lifelines left
        {
            System.out.println("                                                          |---------|  |---------|  |---------|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---------|  |---------|");
        }
        else if(is5050Available && isPAFAvailable) // ask the audience used
        {
            System.out.println("                                                          |---------|  |---------|  |---\\-/---|");
            System.out.println("                                                          |  50:50  |  |  P-a-F  |  |--- X ---|");
            System.out.println("                                                          |---------|  |---------|  |---/-\\---|");
        }
        else if(is5050Available && isATAAvailable) // phone a friend used
        {
            System.out.println("                                                          |---------|  |---\\-/---|  |---------|");
            System.out.println("                                                          |  50:50  |  |--- X ---|  |  A-t-A  |");
            System.out.println("                                                          |---------|  |---/-\\---|  |---------|");
        }
        else if(is5050Available) // phone a friend and ask the audience used
        {
            System.out.println("                                                          |---------|  |---\\-/---|  |---\\-/---|");
            System.out.println("                                                          |  50:50  |  |--- X ---|  |--- X ---|");
            System.out.println("                                                          |---------|  |---/-\\---|  |---/-\\---|");
        }
        else if(isPAFAvailable && isATAAvailable) // 50:50 used
        {
            System.out.println("                                                          |---\\-/---|  |---------|  |---------|");
            System.out.println("                                                          |--- X ---|  |  P-a-F  |  |  A-t-A  |");
            System.out.println("                                                          |---/-\\---|  |---------|  |---------|");
        }
        else if(isPAFAvailable) // 50:50 and ask the audience used
        {
            System.out.println("                                                          |---\\-/---|  |---------|  |---\\-/---|");
            System.out.println("                                                          |--- X ---|  |  P-a-F  |  |--- X ---|");
            System.out.println("                                                          |---/-\\---|  |---------|  |---/-\\---|");
        }
        else if(isATAAvailable) // 50:50 and phone a friend used
        {
            System.out.println("                                                          |---\\-/---|  |---\\-/---|  |---------|");
            System.out.println("                                                          |--- X ---|  |--- X ---|  |  A-t-A  |");
            System.out.println("                                                          |---/-\\---|  |---/-\\---|  |---------|");
        }
        else // all lifelines used
        {
            System.out.println("                                                          |---\\-/---|  |---\\-/---|  |---\\-/---|");
            System.out.println("                                                          |--- X ---|  |--- X ---|  |--- X ---|");
            System.out.println("                                                          |---/-\\---|  |---/-\\---|  |---/-\\---|");
        }
    }

    public static void printQuestionChart() // prints the chart showing the player's progress through the game; uses the "round" instance variable
    {
        if(round == 1)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|    2                $200      |");
            chartSpacing(); System.out.println("|--> 1 <--        --> $100 <--  |");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 2)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|    3                $300      |");
            chartSpacing(); System.out.println("|--> 2 <--        --> $200 <--  |");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 3)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|    4                $500      |");
            chartSpacing(); System.out.println("|--> 3 <--        --> $300 <--  |");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 4)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|    5           ---$1,000---   |");
            chartSpacing(); System.out.println("|--> 4 <--        --> $500 <--  |");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 5)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|    6              $2,000      |");
            chartSpacing(); System.out.println("|--> 5 <--    --> --$1,000-- <--|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 6)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|    7              $4,000      |");
            chartSpacing(); System.out.println("|--> 6 <--      --> $2,000 <--  |");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 7)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|    8              $8,000      |");
            chartSpacing(); System.out.println("|--> 7 <--      --> $4,000 <--  |");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 8)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|    9             $16,000      |");
            chartSpacing(); System.out.println("|--> 8 <--      --> $8,000 <--  |");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 9)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|   10          ---$32,000---   |");
            chartSpacing(); System.out.println("|--> 9 <--      -->$16,000 <--  |");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 10)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|   11             $64,000      |");
            chartSpacing(); System.out.println("|-->10 <--   --> --$32,000-- <--|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 11)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|   12            $125,000      |");
            chartSpacing(); System.out.println("|-->11 <--     --> $64,000 <--  |");
            chartSpacing(); System.out.println("|---10-------------$32,000------|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 12)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|   13            $250,000      |");
            chartSpacing(); System.out.println("|-->12 <--    --> $125,000 <--  |");
            chartSpacing(); System.out.println("|---11-------------$64,000------|");
            chartSpacing(); System.out.println("|---10-------------$32,000------|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 13)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|   14            $500,000      |");
            chartSpacing(); System.out.println("|-->13 <--    --> $250,000 <--  |");
            chartSpacing(); System.out.println("|---12------------$125,000------|");
            chartSpacing(); System.out.println("|---11-------------$64,000------|");
            chartSpacing(); System.out.println("|---10-------------$32,000------|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 14)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|   15          $1 Million      |");
            chartSpacing(); System.out.println("|-->14 <--    --> $500,000 <--  |");
            chartSpacing(); System.out.println("|---13------------$250,000------|");
            chartSpacing(); System.out.println("|---12------------$125,000------|");
            chartSpacing(); System.out.println("|---11-------------$64,000------|");
            chartSpacing(); System.out.println("|---10-------------$32,000------|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
        else if(round == 15)
        {
            chartSpacing(); System.out.println("|-------------------------------|");
            chartSpacing(); System.out.println("|-->15 <--  --> $1 Million <--  |");
            chartSpacing(); System.out.println("|---14------------$500,000------|");
            chartSpacing(); System.out.println("|---13------------$250,000------|");
            chartSpacing(); System.out.println("|---12------------$125,000------|");
            chartSpacing(); System.out.println("|---11-------------$64,000------|");
            chartSpacing(); System.out.println("|---10-------------$32,000------|");
            chartSpacing(); System.out.println("|----9-------------$16,000------|");
            chartSpacing(); System.out.println("|----8--------------$8,000------|");
            chartSpacing(); System.out.println("|----7--------------$4,000------|");
            chartSpacing(); System.out.println("|----6--------------$2,000------|");
            chartSpacing(); System.out.println("|----5--------------$1,000------|");
            chartSpacing(); System.out.println("|----4----------------$500------|");
            chartSpacing(); System.out.println("|----3----------------$300------|");
            chartSpacing(); System.out.println("|----2----------------$200------|");
            chartSpacing(); System.out.println("|----1----------------$100------|");
            chartSpacing(); System.out.println("|-------------------------------|");
        }
    }

    public static void gameOverIndent()
    {
        indent();
        indent();
        indent();
        indent();
        System.out.print("   ");
    }

    public static void printGameOverScreen()
    {
        Scanner sc = new Scanner(System.in);
        clear();
        spacing();
        spacing();
        spacing();
        spacing();
        spacing();
        spacing();
        spacing();
        gameOverIndent();
        System.out.println("--------------------------------------------------------"); // 56
        gameOverIndent();
        System.out.println("|                                                      |");
        gameOverIndent();
        System.out.print("| "); // 2
        if (hasWalked)
        {
            System.out.print("You have walked away with " + balance + "!");
            for (int x = balance.length(); x < 26; x++)
                System.out.print(" ");
            System.out.println("|");
        }
        else if (hasWon)
            System.out.println("     Congratulations! You have won $1,000,000!       |");
        else // hasLost
        {
            System.out.print("You have won " + balance + "."); // 15 + bal
            for (int x = 0; x < 39-balance.length(); x++)
                System.out.print(" ");
            System.out.println("|");           
        }   
        gameOverIndent();
        System.out.println("| Thank you for playing Who Wants to Be a Millionaire! |");
        gameOverIndent();
        System.out.println("|                                                      |");
        gameOverIndent();
        System.out.println("--------------------------------------------------------");
        System.out.println();
        gameOverIndent();
        System.out.println("Press enter to return to title screen"); 
        sc.nextLine();
    }

    public static void gameReset() // resets all instance variables when a new game begins 
    {
        round = 1;
        is5050Available = true;
        isPAFAvailable = true;
        isATAAvailable = true;
        hasWon = false;
        hasLost = false;
        hasWalked = false;
        aEliminated = false;
        bEliminated = false;
        cEliminated = false;
        dEliminated = false;
        balance = "$0";
    }

    public static void updateBalance() // updates the player's earned money; if they have lost the game, reverts their bank balance to the highest checkpoint they have passed
    {
        if(hasLost)
        {
            if(round < 6) // lost before first checkpoint
                balance = "$0";
            else if(round < 11) // lost after first checkpoint
                balance = "$1000";
            else // lost after second checkpoint
                balance = "$32000";
        }
        else
        {
            if(round == 2)
                balance = "$100";
            if(round == 3)
                balance = "$200";
            if(round == 4)
                balance = "$300";
            if(round == 5)
                balance = "$500";
            if(round == 6)
                balance = "$1,000";
            if(round == 7)
                balance = "$2,000";
            if(round == 8)
                balance = "$4,000";
            if(round == 9)
                balance = "$8,000";
            if(round == 10)
                balance = "$16,000";
            if(round == 11)
                balance = "$32,000";
            if(round == 12)
                balance = "$64,000";
            if(round == 13)
                balance = "$125,000";
            if(round == 14)
                balance = "$250,000";
            if(round == 15)
                balance = "$500,000";
            if(round == 16)
                balance = "$1,000,000";
        }
    }

    public static void indent() // prints six spaces
    {
        System.out.print("      "); 
    }

    public static void chartSpacing() // prints 33 spaces
    {
        indent();
        indent();
        indent();
        indent();
        indent();
        indent();
        System.out.print("   ");
    }

    public static void spacing() // prints two spaces from the top
    {
        System.out.println();
        System.out.println();
    }

    public static int toInt(String input) // converts String to int
    {
        return Integer.parseInt(input);
    }

    public static void clear() // clears screen 
    {
        System.out.print('\u000C');
    }

    public static String[] fiftyFifty(String[] questData) // eliminates two wrong options
    {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        clear();
        System.out.println();
        System.out.println();
        indent();
        System.out.println("Are you sure you want to use your 50:50?");
        indent();
        System.out.println("Press 1 to confirm. Press 2 to return to the question");
        String input = sc.nextLine();
        while (!(input.equals("1") || input.equals("2"))) // input checker 
        {
            clear(); 
            System.out.println();
            System.out.println();
            indent();
            System.out.println("Please select 1 to confirm, or 2 to return to the question"); 
            input = sc.nextLine();
        }
        if (toInt(input) == 2) // they are choosing not to use their lifeline
        {
            is5050Available = true;
            return questData; // returns the original question array
        }
        else // they are using the lifeline
        {
            clear();
            String[] newQuestData = questData;
            int questionToKeep = rand.nextInt(3); // randomizes which wrong answer is kept
            String elim1 = "";
            String elim2 = "";
            if(questionToKeep == 0) // keeps the first wrong answer in order from a to d
            {
                if(correctAnswer == 'a')
                {
                    elim1 = optionC;
                    elim2 = optionD;
                    optionC = "";
                    optionD = "";
                    cEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'b')
                {
                    elim1 = optionC;
                    elim2 = optionD;
                    optionC = "";
                    optionD = "";
                    cEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'c')
                {
                    elim1 = optionB;
                    elim2 = optionD;
                    optionB = "";
                    optionD = "";
                    bEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'd')
                {
                    elim1 = optionB;
                    elim2 = optionC;
                    optionB = "";
                    optionC = "";
                    bEliminated = true;
                    cEliminated = true;
                }
            }
            else if(questionToKeep == 1) // keeps the second wrong answer
            {
                if(correctAnswer == 'a')
                {
                    elim1 = optionC;
                    elim2 = optionD;
                    optionC = "";
                    optionD = "";
                    cEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'b')
                {
                    elim1 = optionA;
                    elim2 = optionD;
                    optionA = "";
                    optionD = "";
                    aEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'c')
                {
                    elim1 = optionA;
                    elim2 = optionD;
                    optionA = "";
                    optionD = "";
                    aEliminated = true;
                    dEliminated = true;
                }
                if(correctAnswer == 'd')
                {
                    elim1 = optionA;
                    elim2 = optionC;
                    optionA = "";
                    optionC = "";
                    aEliminated = true;
                    cEliminated = true;
                }
            }
            else if(questionToKeep == 2) // keeps the third wrong answer
            {
                if(correctAnswer == 'a')
                {
                    elim1 = optionB;
                    elim2 = optionC;
                    optionB = "";
                    optionC = "";
                    bEliminated = true;
                    cEliminated = true;
                }
                if(correctAnswer == 'b')
                {
                    elim1 = optionA;
                    elim2 = optionC;
                    optionA = "";
                    optionC = "";
                    aEliminated = true;
                    cEliminated = true;
                }
                if(correctAnswer == 'c')
                {
                    elim1 = optionA;
                    elim2 = optionB;
                    optionA = "";
                    optionB = "";
                    aEliminated = true;
                    bEliminated = true;
                }
                if(correctAnswer == 'd')
                {
                    elim1 = optionA;
                    elim2 = optionB;
                    optionA = "";
                    optionB = "";
                    aEliminated = true;
                    bEliminated = true;
                }
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            indent();indent();
            System.out.println(elim1.trim() + " and " + elim2.trim() + " have been eliminated."); // removes unnecessary spaces from the words before printing them
            indent();indent();
            System.out.println("Press enter to return to the question.");
            sc.nextLine();
            return newQuestData; // returns the new options, with two eliminated 
        }
    }

    public static void phoneFriend(String[] questData) // phone a friend method, runs "conversation" between player and friend 
    {
        Scanner sc = new Scanner(System.in);
        clear();
        spacing();
        indent(); 
        System.out.println("Which friend do you want to call?"); 
        System.out.println();
        indent();
        System.out.println("A: Tim, a history professor who prides himself on the merits of academia");
        indent();
        System.out.println("B: Danielle, a high school chemistry teacher with wide knowledge of the sciences");
        indent();
        System.out.println("C: Darren, a stay-at-home dad who might surprise you with his trivial knowledge");
        System.out.println();
        indent();
        System.out.println("To return to the question and save your lifeline, press D");
        indent();
        String friend = sc.nextLine(); 
        String probability = questData[7]; // the probability value for Phone a Friend is stored in the question array at index 7 
        String type = questData[1];
        int friendNum = 0; // number corresponds to friend chosen 
        while (!(friend.toUpperCase().equals("A") || friend.toUpperCase().equals("B") || friend.toUpperCase().equals("C") || friend.toUpperCase().equals("D"))) // invalid input checker 
        {
            clear();
            spacing();
            indent();
            System.out.println("Please try again, and enter a valid character");
            System.out.println();
            indent();
            System.out.println("Which friend do you want to call?"); 
            System.out.println();
            indent();
            System.out.println("A: Tim, a history professor who prides himself on the merits of academia");
            indent();
            System.out.println("B: Danielle, a high school chemistry teacher with wide knowledge of the sciences");
            indent();
            System.out.println("C: Darren, a stay-at-home dad who might surprise you with his trivial knowledge");
            System.out.println();
            indent();
            System.out.println("To return to the question and save your lifeline, press D");
            indent();
            friend = sc.nextLine();
        }
        if (friend.toUpperCase().equals("D")) // user wants to return to question
        {
            isPAFAvailable = true;
        }
        else { // user wants to phone a friend
            clear();
            if (friend.toUpperCase().equals("A")) // Tim
            {
                friendNum = 3;
            }
            else if (friend.toUpperCase().equals("B")) // Danielle
            {
                friendNum = 1;
            }
            else  if (friend.toUpperCase().equals("C")) // Darren
            {
                friendNum = 2;
            }
            boolean isCorrect = isFriendCorrect(friendNum, toInt(probability), toInt(type)); // determines if the friend knows the answer or not, see below 
            spacing();
            indent();
            if (friendNum == 1)
                friend = "Danielle";
            else if (friendNum == 3)
                friend = "Tim";
            else
                friend = "Darren";
            System.out.println(friend + " says:");
            indent();
            if (isCorrect) // if the friend knows the answer
            {
                System.out.println("\"I know that one, it's easy...the answer is '"+ questData[2] + "'.\"");
            }
            else // if they don't know it
            {
                System.out.println("\"Sorry boss, can't help you on that one. I got no idea.\"");
            }
            System.out.println();
            indent();
            System.out.println("Press enter to return to the question");
            sc.nextLine();
            clear();
        }
    }

    public static boolean isFriendCorrect(int friend, int probability, int type) // uses the probability factor to determine if the friend is correct 
    {
        Random rand = new Random();
        int totalProb = probability; // the average probability, before advantages/disadvantages are added based on which friend was chosen 
        if (type == 1 && friend == 1) // if Danielle was chosen for a science question, 20% boost in probability
        {
            probability+=20;
        }
        else if (type == 1 && friend == 2) // if Danielle was chosen for a trivial knowledge question, 20% drop in probability 
        {
            probability-=20;
        }        
        else if (type == 2 && friend == 2) // if Darren is chosen for a trivial knowledge question, 20% boost
        {
            probability+=20;
        }
        else if (type == 2 && friend == 3) // if Darren is chosen for a history/geo question, 20% drop
        {
            probability-=20;
        }  
        else if (type == 3 && friend == 3) // if Tim is chosen for a history/geo question, 20% boost
        {
            probability+=20;
        }
        else if (type == 3 && friend == 1) // if Tim is chosen for a science question, 20% drop
        {
            probability-=20;
        }  

        if (probability > 100) // sometimes, because of boosts, probability can become above 100%
            probability = 100;
        else if (probability < 0) // sometimes, because of drops, probability can become less than 0%
            probability = 0; 

        int toPick = rand.nextInt(100); // picks a random integer between 0 and 99
        while (probability > 0) // the actual randomization: if probability value is higher than the random integer, then the friend knows the answer 
        {
            probability--; // brings value down to shift from "1 to 100" system to "0 to 99" initially; then it gradually brings probability value to 0
            if (probability == toPick) 
            {
                return true;
            }
        }
        return false; // friend doesn't know the answer 
    }

    public static void askTheAudience(String[] questArray) // Ask the Audience user interaction portal
    {
        Scanner sc = new Scanner(System.in);
        clear();
        spacing();
        indent();
        System.out.println("Are you sure you want to 'Ask the Audience'?");
        indent();
        System.out.println("Press 1 to confirm. Press 2 to return to the question");
        String inp = sc.nextLine();
        while (!(inp.equals("1") || inp.equals("2"))) // input checker
        {
            clear(); 
            spacing();
            indent();
            System.out.println("Please select 1 to confirm, or 2 to return to the question"); 
            inp = sc.nextLine();
        }
        if (toInt(inp) == 2) // they are choosing not to use lifeline
        {
            isATAAvailable = true;
        }
        else // they are using lifeline
        {
            clear();
            int[] responses = audienceDistribution(questArray); // audienceDistribution returns the percentages for each answer
            int probA = responses[0];          
            int probAInitial = probA;
            int probB = responses[1];
            int probBInitial = probB;
            int probC = responses[2];
            int probCInitial = probC;
            int probD = responses[3];
            int probDInitial = probD;
            int counterOfLines = 0; // counts how many lines have been printed for the bar graph 
            // now the time to make our graph
            spacing();
            indent(); System.out.println("________________________________________________________________________"); // 72 lines
            indent(); System.out.println("|                                                                      |");

            indent();   System.out.print("|  A:  "); // printing the A bar for the graph
            while (probA > 0)
            {
                System.out.print("-");
                probA-=2;
                counterOfLines++;
            }
            System.out.print("  "); // 3 lines
            System.out.print(probAInitial + "%"); // excess characters accounted for below
            if (probAInitial > 9) // based on length of probability, different number of spaces will be printed to fill at the end 
                counterOfLines+=3;
            else  
                counterOfLines+=2;
            int leftOver = 62-counterOfLines;
            for (int x = 0; x < leftOver; x++) // prints spaces to fill the line 
            {
                System.out.print(" ");
            }
            System.out.println("|");
            indent(); System.out.println("|                                                                      |");
            leftOver = 0; // resets leftOver and counterOfLines for the next lines 
            counterOfLines = 0; 

            indent();   System.out.print("|  B:  "); // printing the B bar for the graph: exact same code as above
            while (probB > 0)
            {
                System.out.print("-");
                probB-=2;
                counterOfLines++;
            }
            System.out.print("  "); 
            System.out.print(probBInitial + "%"); 
            if (probBInitial > 9)
                counterOfLines+=3;
            else  
                counterOfLines+=2;
            leftOver = 62-counterOfLines;
            for (int x = 0; x < leftOver; x++)
            {
                System.out.print(" ");
            }
            System.out.println("|");                              
            indent(); System.out.println("|                                                                      |");
            leftOver = 0; 
            counterOfLines = 0; 

            indent();   System.out.print("|  C:  "); // printing the C bar for the graph
            while (probC > 0)
            {
                System.out.print("-");
                probC-=2;
                counterOfLines++;
            }
            System.out.print("  "); 
            System.out.print(probCInitial + "%"); 
            if (probCInitial > 9)
                counterOfLines+=3;
            else  
                counterOfLines+=2;
            leftOver = 62-counterOfLines;
            for (int x = 0; x < leftOver; x++)
            {
                System.out.print(" ");
            }
            System.out.println("|");
            indent(); System.out.println("|                                                                      |");
            leftOver = 0; 
            counterOfLines = 0; 

            indent();   System.out.print("|  D:  "); // printing the D bar for the graph
            while (probD > 0)
            {
                System.out.print("-");
                probD-=2;
                counterOfLines++;
            }
            System.out.print("  "); 
            System.out.print(probDInitial + "%"); 
            if (probDInitial > 9)
                counterOfLines+=3;
            else  
                counterOfLines+=2;
            leftOver = 62-counterOfLines;
            for (int x = 0; x < leftOver; x++)
            {
                System.out.print(" ");
            }
            System.out.println("|");
            indent(); System.out.println("|                                                                      |");
            indent(); System.out.println("________________________________________________________________________");

            System.out.println();
            indent();
            System.out.println("Press enter to return to the question");
            sc.nextLine();
            clear();
        }
    }

    public static int[] audienceDistribution(String[] questArray) // returns the audience percentages for each question during Ask the Audience
    {
        int prob = toInt(questArray[6])-1; // probability for ATA is stored with question 
        // each audience member acts separately
        Random rand = new Random();        
        int counterA = 0;
        int counterB = 0;
        int counterC = 0;
        int counterD = 0;

        for (int x = 0; x < 100; x++) // runs 100 times for 100 distinct audience members 
        {
            char ans = audienceAnswer(prob); // the choice of the audience member
            if (ans == 'a')
                counterA++;
            else if (ans == 'b')
                counterB++;
            else if (ans == 'c')
                counterC++;
            else
                counterD++;
        }

        int[] responses = {counterA, counterB, counterC, counterD};
        return responses; // returns number of audience members who selected each
    }

    public static char audienceAnswer(int prob) // returns what each of the 100 audience members chooses
    {
        if(round5050WasUsed == round) // if the 50:50 was used on this question, half of the people who would have gotten it wrong now guess correctly and half choose the remaining wrong answer
        {
            prob += (100 - prob) / 2;
        }
        if (correctAnswer == 'a') // if the correct answer is a then it is special, as it has the probability value (determined earlier) of being chosen
        {
            Random rand = new Random();
            for (int x = 0; x < 100; x++)
            {
                int toPick = rand.nextInt(100); // random number between 0-99
                while (prob > 0)
                {
                    prob--; 
                    if (prob == toPick) // same probability system as Phone a Friend, if the random number is lower than the probability value then the correct answer is returned 
                    {
                        return 'a';
                    }                    
                }
                int notChosen = rand.nextInt(3); // complete randomization for the other three wrong answers 
                if (round5050WasUsed == round) // if 50:50 is used then the audience only chooses between two options
                {
                    if (notChosen == 0 && !bEliminated || cEliminated && dEliminated)
                        return 'b';
                    if (notChosen == 1 && !cEliminated || bEliminated && dEliminated)
                        return 'c';
                }
                else
                {
                    if (notChosen == 0)
                        return 'b';
                    if (notChosen == 1)
                        return 'c';
                }
                return 'd';
            }
        }
        else if (correctAnswer == 'b') // same code as above, but b is the correct answer in this situation 
        {
            Random rand = new Random();
            for (int x = 0; x < 100; x++)
            {
                int toPick = rand.nextInt(100);
                while (prob >= 0)
                {
                    if (prob == toPick)
                    {
                        return 'b';
                    }
                    prob--; 
                }
                int notChosen = rand.nextInt(3);
                if (round5050WasUsed == round) // if 50:50 is used then the audience only chooses between two options
                {
                    if (notChosen == 0 && !aEliminated || cEliminated && dEliminated)
                        return 'a';
                    if (notChosen == 1 && !cEliminated || aEliminated && dEliminated)
                        return 'c';
                }
                else
                {
                    if (notChosen == 0)
                        return 'a';
                    if (notChosen == 1)
                        return 'c';
                }
                return 'd';                     
            }
        }
        else if (correctAnswer == 'c') // same code as above for c as the correct answer 
        {
            Random rand = new Random();
            for (int x = 0; x < 100; x++)
            {
                int toPick = rand.nextInt(100);
                while (prob >= 0)
                {
                    if (prob == toPick)
                    {
                        return 'c';
                    }
                    prob--; 
                }
                int notChosen = rand.nextInt(3);
                if (round5050WasUsed == round) // if 50:50 is used then the audience only chooses between two options
                {
                    if (notChosen == 0 && !aEliminated || bEliminated && dEliminated)
                        return 'a';
                    if (notChosen == 1 && !bEliminated || aEliminated && dEliminated)
                        return 'b';
                }
                else
                {
                    if (notChosen == 0)
                        return 'a';
                    if (notChosen == 1)
                        return 'b';
                }
                return 'd';                  
            }
        }
        else // once again, same code, but for d as the correct answer
        {
            Random rand = new Random();
            for (int x = 0; x < 100; x++)
            {
                int toPick = rand.nextInt(100);
                while (prob >= 0)
                {
                    if (prob == toPick)
                    {
                        return 'd';
                    }
                    prob--; 
                }
                int notChosen = rand.nextInt(3);
                if (round5050WasUsed == round) // if 50:50 is used then the audience only chooses between two options
                {
                    if (notChosen == 0 && !aEliminated || bEliminated && cEliminated)
                        return 'a';
                    if (notChosen == 1 && !bEliminated || aEliminated && cEliminated)
                        return 'b';
                }
                else
                {
                    if (notChosen == 0)
                        return 'a';
                    if (notChosen == 1)
                        return 'b';
                }
                return 'c';                    
            }
        }
        return 'a'; // this never gets returned, but makes the code compile
    }

    public static void questionSetup(String[] questData) // gives the order of the answers to determine how they will appear on the screen
    {
        // first step is to find the order the questions will appear and track the right answer
        question = questData[0];
        Random rand = new Random();
        int op1 = rand.nextInt(4); // the number this Random spits out corresponds to the letter of the correct answer 
        if (op1 == 0)
        {
            optionA = questData[2];
            correctAnswer = 'a';
        }
        else if (op1 == 1)
        {
            optionB = questData[2];
            correctAnswer = 'b';
        }
        else if (op1 == 2)
        {
            optionC = questData[2];
            correctAnswer = 'c';
        }
        else 
        {
            optionD = questData[2];
            correctAnswer = 'd';
        }

        int op2 = rand.nextInt(4); // this determines the letter corresponding to option B, the first wrong answer
        while (op2 == op1) // this fixes a possible error, if op2 corresponds to the same letter as op1
            op2 = rand.nextInt(4);   

        if (op2 == 0)
            optionA = questData[3];
        else if (op2 == 1)
            optionB = questData[3];
        else if (op2 == 2)
            optionC = questData[3];
        else 
            optionD = questData[3];

        int op3 = rand.nextInt(4);  // this determines corresponding letter to second wrong answer
        while (op3 == op1 || op3 == op2) // this fixes a possible error 
            op3 = rand.nextInt(4);            
        if (op3 == 0)
            optionA = questData[4];
        else if (op3 == 1)
            optionB = questData[4];
        else if (op3 == 2)
            optionC = questData[4];
        else 
            optionD = questData[4];

        int op4 = rand.nextInt(4); // determines corresponding letter to third wrong answer
        while (op4 == op1 || op4 == op2 || op4 == op3) // this fixes a possible error 
            op4 = rand.nextInt(4);            
        if (op4 == 0)
            optionA = questData[5];
        else if (op4 == 1)
            optionB = questData[5];
        else if (op4 == 2)
            optionC = questData[5];
        else 
            optionD = questData[5];

    }

    public static int lengthOfLongest(String[] strings) // returns the length of the longest string in an array of strings
    {
        int length = 0;

        for (int i = 0; i < strings.length; i++) // if a String is longer than the longest String already checked, it becomes the new length value
        {
            if(strings[i].length() > length)
            {
                length = strings[i].length();
            }
        }

        return length;
    }

    public static String[] cutString(String str) // takes a String and, if it is multiple words, separates it into two Strings of the most equal length possible;
    // also adds spaces to the end of the shorter half to make the two equal in length
    {
        String[] words = str.split(" ");
        int letterCount1 = 0; // the tally of letters of words up to or just exceeding half of the original string's length, beginning at the start of the string
        int letterCount2 = 0; // the tally of letters of words up to or just exceeding half of the original string's length, beginning at the end of the string
        int index1 = 0; // the index of the word that pushed the letter tally up to or past half of the string's length, beginning at the start of the string
        int index2 = 0; // the index of the word that pushed the letter tally up to or past half of the string's length, beginning at the end of the string

        if(words.length < 2) // the original string is either one word long or empty
        {
            return words;
        }

        if(words.length == 2) // the original string is two words long
        {
            if(words[0].length() == words[1].length())
            {
                return words;
            }
            String newStr1 = words[0];
            String newStr2 = words[1];
            if(newStr1.length() > newStr2.length())
            {
                while(newStr1.length() > newStr2.length())
                {
                    newStr2 += " ";
                }
            }
            else
            {
                while(newStr1.length() < newStr2.length())
                {
                    newStr1 += " ";
                }
            }
        }

        String newStr1 = words[0]; // prevents a fencepost error
        String newStr2;

        for(int i = 0; letterCount1 < str.length() / 2; i++) // counts the letters in the words until letterCount reaches half length of the original string
        {
            letterCount1 += words[i].length() + 1; // plus 1 accounts for the space between the words 
            index1 = i;
        }
        for(int i = words.length - 1; letterCount2 < str.length() / 2; i--) // same thing but starting from the end of the array
        {
            letterCount2 += words[i].length() + 1; // plus 1 accounts for the space between the words 
            index2 = i;
        }

        if(letterCount1 - str.length() / 2 <= letterCount2 - str.length() / 2) // if the letter tally starting from the first word is closer to the midpoint than the letter tally starting
        // from the last word, or if the tallies are the same
        {
            for(int i = 1; i <= index1; i++) // adds words to the first string up to and including the index that pushed the letter tally past the midpoint when beginning from the first word
            {
                newStr1 += " " + words[i];
            }
            newStr2 = words[index1 + 1]; // prevents a fencepost error
            for(int i = index1 + 2; i < words.length; i++) // adds the rest of the words to the second string
            {
                newStr2 += " " + words[i];
            }
        }
        else // if the letter tally starting from the last word is closer to the midpoint than the letter tally starting from the first word
        {
            for(int i = 1; i < index2; i++) // adds words to the first string up to but not including the index that pushed the letter tally past the midpoint when beginning from the last word
            {
                newStr1 += " " + words[i];
            }
            newStr2 = words[index2]; // prevents a fencepost error
            for(int i = index2 + 1; i < words.length; i++) // adds the rest of the words to the second string
            {
                newStr2 += " " + words[i];
            }
        }

        while(newStr1.length() > newStr2.length()) // adds spaces to the second string until it is not shorter than the first
        {
            newStr2 += " ";
        }

        while(newStr1.length() < newStr2.length()) // adds spaces to the first string until it is not shorter than the second
        {
            newStr1 += " ";
        }

        String[] stringCut = new String[] {newStr1, newStr2};
        return stringCut;
    }

    public static void printQuestionLayout() // prints the typical "Millionaire" layout shown for each question
    {
        clear();

        int questionLength = question.length();
        int optionLength = lengthOfLongest(new String[] {optionA, optionB, optionC, optionD}); // length of longest option to answer
        String prefixA = "A: ";
        String prefixB = "B: ";
        String prefixC = "C: ";
        String prefixD = "D: ";

        System.out.println();
        System.out.println();
        printLifelines(); // prints lifelines in upper right corner 
        System.out.println();
        System.out.println();
        printQuestionChart(); // prints the rounds chart above the question, true to "Millionaire" style 
        System.out.println();

        if(question.length() < 80) // prints the question on a single line: this whole section of code is ASCII art to print the four questions true to "Millionaire" style
        {
            for(int x = 0; x < (111 - question.length() - 4) / 2; x++)
            {
                System.out.print(" ");
            }
            System.out.print(" /");
            for(int x = 0; x < question.length(); x++)
            {
                System.out.print("-");
            }
            System.out.println("\\");

            for(int x = 0; x < (111 - question.length() - 4) / 2; x++)
            {
                System.out.print("-");
            }
            System.out.print("{ " + question + " }");
            for(int x = 0; x < (111 - question.length() - 4) / 2; x++)
            {
                System.out.print("-");
            }
            System.out.println();

            for(int x = 0; x < (111 - question.length() - 4) / 2; x++)
            {
                System.out.print(" ");
            }
            System.out.print(" \\");
            for(int x = 0; x < question.length(); x++)
            {
                System.out.print("-");
            }
            System.out.println("/");
        }
        else // prints the question on two lines, if they are too long for one line
        {
            String[] questionLines = cutString(question);
            String line1 = questionLines[0];
            String line2 = questionLines[1];
            int length = line1.length();

            for(int x = 0; x < (111 - length - 4) / 2; x++)
            {
                System.out.print(" ");
            }
            System.out.print(" /");
            for(int x = 0; x < length; x++)
            {
                System.out.print("-");
            }
            System.out.println("\\");

            for(int x = 0; x < (111 - length - 4) / 2; x++)
            {
                System.out.print("_");
            }
            System.out.print("/ " + line1 + " \\");
            for(int x = 0; x < (111 - length - 4) / 2; x++)
            {
                System.out.print("_");
            }
            System.out.println();

            for(int x = 0; x < (111 - length - 4) / 2; x++)
            {
                System.out.print(" ");
            }
            System.out.println("\\ " + line2 + " /");

            for(int x = 0; x < (111 - length - 4) / 2; x++)
            {
                System.out.print(" ");
            }
            System.out.print(" \\");
            for(int x = 0; x < length; x++)
            {
                System.out.print("-");
            }
            System.out.println("/");
        }

        while(optionA.length() < optionLength) // adds spaces to option A until it is not shorter than the longest option
        {
            optionA += " ";
        }
        while(optionB.length() < optionLength) // adds spaces to option B until it is not shorter than the longest option
        {
            optionB += " ";
        }
        while(optionC.length() < optionLength) // adds spaces to option C until it is not shorter than the longest option
        {
            optionC += " ";
        }
        while(optionD.length() < optionLength) // adds spaces to option D until it is not shorter than the longest option
        {
            optionD += " ";
        }

        if(optionA.trim().length() == 0) // if the option is blank, due to 50:50 elimination, then its prefix is printed  
        {
            prefixA = "   ";
        }
        if(optionB.trim().length() == 0)
        {
            prefixB = "   ";
        }
        if(optionC.trim().length() == 0)
        {
            prefixC = "   ";
        }
        if(optionD.trim().length() == 0)
        {
            prefixD = "   ";
        }

        System.out.println();

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print(" ");
        }
        System.out.print(" /");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.print("\\     /");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.println("\\");

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print("-");
        }
        System.out.print("{ " + prefixA + optionA + " }---{ " + prefixB + optionB + " }");
        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print("-");
        }
        System.out.println();

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print(" ");
        }
        System.out.print(" \\");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.print("/     \\");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.println("/");

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print(" ");
        }
        System.out.print(" /");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.print("\\     /");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.println("\\");

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print("-");
        }
        System.out.print("{ " + prefixC + optionC + " }---{ " + prefixD + optionD + " }");
        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print("-");
        }
        System.out.println();

        for(int x = 0; x < (111 - 2 * (optionLength + 7) - 3) / 2; x++)
        {
            System.out.print(" ");
        }
        System.out.print(" \\");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.print("/     \\");
        for(int x = 0; x < optionLength + 3; x++)
        {
            System.out.print("-");
        }
        System.out.println("/");
        System.out.println();
        indent();indent();
        System.out.println("Enter A, B, C or D to answer the question.");
        indent();indent();
        System.out.println("Enter 1 to use 50:50, 2 to use Phone a Friend, 3 to use Ask the Audience, or 4 to walk away.");
    }

    public static int playerAction() // returns 0 if the question is answered correctly; 1 if answered incorrectly; 2 if the player chooses to walk away; 3 if the player wishes to use their
    // 50:50; 4 if they wish to use phone a friend; 5 if they wish to use ask the audience
    // NOTE: Press 1 for 50:50, press 2 to use Phone a Friend, press 3 for Ask the Audience, press 4 to walk away
    {
        Scanner sc = new Scanner(System.in);

        String answer = sc.nextLine().toLowerCase();
        while(!(answer.equals("a") || answer.equals("b") || answer.equals("c") || answer.equals("d") || answer.equals("1") || answer.equals("2") || answer.equals("3") || answer.equals("4")))
        {
            clear();
            printQuestionLayout();
            indent();indent();
            System.out.println("Enter a valid response");
            answer = sc.nextLine().toLowerCase();
        }
        while (answer.equals("1") && !is5050Available || answer.equals("2") && !isPAFAvailable || answer.equals("3") && !isATAAvailable) // if lifeline is selected but unavailable 
        {
            clear();
            printQuestionLayout();
            indent();indent();
            System.out.println("You have already used that lifeline.");
            answer = sc.nextLine().toLowerCase();
        }
        if(answer.toLowerCase().equals("a") || answer.toLowerCase().equals("b") || answer.toLowerCase().equals("c") || answer.toLowerCase().equals("d")) // if the player is answering the question
        {
            if(answer.indexOf(correctAnswer) != -1) // answer is correct
            {                
                return 0;
            }
            else // answer is incorrect
            {
                return 1;
            }
        }
        if(answer.equals("4")) // Walk Away selected 
        {
            return 2;
        }
        if(answer.equals("1")) // 50:50 selected
        {
            return 3;
        }
        if(answer.equals("2")) // Phone a Friend selected
        {
            return 4;
        }
        else // Ask the Audience selected 
        {
            return 5;
        }
    }

    public static void printQuestionResult(boolean correct, String answer) throws InterruptedException // prints whether the question was answeredcorrectly...suspensefully!
    {
        Scanner sc = new Scanner(System.in);
        clear();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.print("                                                That is... ");
        Thread.sleep(300 + 100 * round); // the result becomes more suspenseful in later rounds 

        clear();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.print("                                                That is... ");
        if(correct)
        {
            System.out.print("correct!");
        }
        else
        {
            System.out.println("incorrect...");
            Thread.sleep(1000);
            for(int x = 0; x < (78 - answer.length()) / 2; x++) // centres the next line on the screen 
            {
                System.out.print(" ");
            }
            System.out.println("Unfortunately, the answer was " + Character.toUpperCase(correctAnswer) + ". " + answer + ".");
        }
        Thread.sleep(1000);
    }

    public static String[] questionGenerator(int round) // returns the data for each question used in the game, randomly choosing the question to use 
    {
        // questions are HARD beginning in round 5, because we take trivia seriously here 
        // info stored in each question: [0] question itself, [1] topic, [2] correct answer, [3-5] incorrect answers, [6] ATA probability, [7] Phone a Friend probability (before boosts)
        // topics: 1 = science, 2 = media, 3 = history/geography/literature, 4 = random knowledge

        String[][] round1 = new String[16][8]; // round 1 questions: so easy some may make you laugh
        round1[0] = new String[] {"Which chemical element has the symbol O, and is normally found as a diatomic gas at room temperature?",
            "1", "Oxygen", "Omnigen", "Orthogen", "Octagen", "98", "120"};
        round1[1] = new String[] {"Which of the following countries was named after a king of Spain?", "3", "The Phillipines", "Edwardor",
            "Kevinzuela", "Peterguay", "98", "100"};
        round1[2] = new String[] {"When I was released in 2009, I became the highest grossing film of all time. I was directed by James Cameron. What am I?", "2", 
            "Avatar", "Citizen Kane", "The Room", "Tropic Thunder", "98", "110"};
        round1[3] = new String[] {"Who was the first president of the United States?", "3", "George Washington", "Abraham Lincoln", "Yankee Doodle", "Donald Trump",
            "98", "120"}; 
        round1[4] = new String[] {"If you're having trouble speaking, who might have your tongue?", "4", "A cat", "A dog", "A rooster", "A marsupial", "98", "110"};
        round1[5] = new String[] {"The best way to quit a habit is to go cold...", "4", "Turkey", "Chicken", "Shoulder", "Steak", "98", "110"};
        round1[6] = new String[] {"The developers of Facebook were students at which American university?", "4", "Harvard University", "Cornell University",
            "NC State", "CalTech", "85", "90"}; 
        round1[7] = new String[] {"What is the annual championship game of the National Football League called?", "4", "Super Bowl", "World Cup", "World Series", "The Finals", 
            "98", "110"}; 
        round1[8] = new String[] {"What is the square root of the sum of 65 and 16?", "1", "9", "4", "12", "6561", "98", "110"};
        round1[9] = new String[] {"Which French expression refers to a meal in which each dish is paid for at a specified price?", "4", "A la carte", "Cul-de-sac", 
            "Nom-de-plume", "Coupe d'etat", "95", "110"}; 
        round1[10] = new String[] {"What country has jurisdiction over Puerto Rico?", "4", "The United States", "Spain", "Cuba", "The Netherlands", "95", "110"};
        round1[11] = new String[] {"Which playing piece do you try to capture to end the game of chess?", "4", "King", "Queen", "Captain", "Knight", "96", "110"};
        round1[12] = new String[] {"Paleontology refers to the study of what?", "1", "Fossils", "Life", "Lakes", "Ancient civilizations", "88", "100"}; 
        round1[13] = new String[] {"Who was the president of the United States between 2008 and 2016?", "3", "Barack Obama", "George W. Bush", "Bill Clinton", "Ronald Reagan", "95", "110"};
        round1[14] = new String[] {"In which sphere would one find life?", "1", "Biosphere", "Geosphere", "Lithosphere", "Thermosphere", "80", "90"};
        round1[15] = new String[] {"How many grams are in a kilogram??", "1", "A thousand", "A hundred", "A dozen", "A million", "98", "120"};       

        String[][] round2 = new String[15][8]; // little bit harder 
        round2[0] = new String[] {"Which major European city is divided into 20 'Arrondisements'?", "3", "Paris", "London", "Marseille", "Brussels", "80", "90"};
        round2[1] = new String[] {"What is the second largest state in the U.S. by area and population?", "3", "Texas", "Florida", "New York", "Califoria", "85", "90"};
        round2[2] = new String[] {"What famous rock and roll star made his last public performance in 1977?", "2", "Elvis Presley", "Freddie Mercury", "Jimi Hendrix", 
            "Buddy Holly", "75", "85"};
        round2[3] = new String[] {"The NCAA Men's Division I Basketball Tournament is better known by what name?", "4", "March Madness", 
            "Amazing April", "Fantastic February", "Magnificient May", "95", "100"};
        round2[4] = new String[] {"Uranus and which other planet are nicknamed the 'ice giants'?", "1", "Neptune", "Jupiter", "Pluto", "Saturn", "85", "95"};
        round2[5] = new String[] {"In what year was Taylor Swift's album \"1989\" released?", "2", "2014", "1989", "2012", "2016", "60", "60"};
        round2[6] = new String[] {"What city was evacuated in 1986 after a nuclear accident?", "4", "Chernobyl", "Fukushima", "New York", "Pittsburgh", "75", "85"};
        round2[7] = new String[] {"In golf, one stroke over par is called a what?", "4", "Bogey", "Eagle", "Birdie", "Albatross", "60", "75"};
        round2[8] = new String[] {"With what religion do you associate 'the five pillars'?", "3", "Islam", "Judaism", "Buddhism", "Taoism", "85", "95"};
        round2[9] = new String[] {"Bruce Banner is the real name of which superhero?", "2", "The Hulk", "Spiderman", "Batman", "Green Lantern", "70", "70"};
        round2[10] = new String[] {"What famous megalith stands near Salisbury, England?", "3", "Stonehenge", "Big Ben", "Dolmen", "Harhoog", "80", "100"};
        round2[11] = new String[] {"In hockey, how long is a minor penalty?", "4", "2 minutes", "5 minutes", "1 minute", "30 seconds", "80", "95"};
        round2[12] = new String[] {"Martedi in Italian, Dienstag in German and mardi in French, what's the day in English?", "4", "Tuesday", "Monday", "Wednesday", "Sunday", "90", "100"};
        round2[13] = new String[] {"The Benelux countries include The Netherlands, Luxembourg, and what?", "3", "Belgium", "Switzerland", "France", "Germany", "65", "80"};
        round2[14] = new String[] {"Who wrote the famous novels \"It\", \"The Shining\" and \"Carrie\"?", "2", "Stephen King", "Stanley Kubrick", "Edgar Allan Poe", "Sinclair Ross", "90", "100"};       

        String[][] round3 = new String[14][8]; // have fun from here on in
        round3[0] = new String[] {"What is the only American state capital that has three words in its name?", "3", "Salt Lake City", "New York City", "Lake Havasu City", 
            "West Palm Beach", "80", "90"};
        round3[1] = new String[] {"What type of eclipse occurs when the Moon is in the Earth's shadow?", "1", "Lunar eclipse", "Solar eclipse", "Total eclipse", 
            "Eccentric eclipse", "70", "70"};
        round3[2] = new String[] {"What is the smallest division of the animal kingdom that is reflected in an animal's two-word scientific name?", "1", "Species", "Genus", 
            "Family", "Phylum", "60", "60"};
        round3[3] = new String[] {"In 1934, what game was rejected by Parker Brothers as having 52 fundamental design errors?", "2", "Monopoly", "Connect Four", "Trivial Pursuit", "Clue", "70", "80"};
        round3[4] = new String[] {"Who released the 1994 album \"Smash\" and the 1998 album \"Americana\"?", "4", "The Offspring", "Marcy Playground", "Green Day", "Blink-182", "70", "70"};
        round3[5] = new String[] {"Who was the American president who began his second term in 1985?", "3", "Ronald Reagan", "Jimmy Carter", "Richard Nixon", "George H.W. Bush", "70", "85"};
        round3[6] = new String[] {"What country is located on the western side of the Iberian Peninsula?", "3", "Portugal", "Spain", "Andorra", "Malta", "65", "85"};
        round3[7] = new String[] {"After being shot, which U.S. president remarked, 'I forgot to duck!'?", "3", "Ronald Reagan", "Abraham Lincoln", "John F. Kennedy", "Franklin D. Roosevelt", "40", "50"};
        round3[8] = new String[] {"The name of which U.S. state comes from the Spanish word for \"dry region\"?", "3", "Arizona", "California", "Texas", "Florida", "60", "60"};
        round3[9] = new String[] {"Which American rock band includes lead vocalist Billie Joe Armstrong?", "4", "Green Day", "The Offspring", "Nirvana", "Weezer", "75", "70"};
        round3[10] = new String[] {"Siamese Dream was the second studio album released by which American rock band?", "4", "The Smashing Pumpkins", "Radiohead", "The Red Hot Chili Peppers", "The Offspring", "70", "70"};
        round3[11] = new String[] {"Which early 20th-century Bohemian Jewish author wrote \"The Metamorphosis\" and \"Before the Law\"?", "3", "Franz Kafka", "Gabriel Garica Marquez", "James Joyce", "William Faulkner", "70", "70"};
        round3[12] = new String[] {"Which Spanish conquistador conquered the Aztec empire in 1521 and secured Mexico for Spain?", "3", "Hernan Cortes", "Francisco Pizarro",
            "Jose de Anchieta", "Manuel da Nobrega", "60", "70"};
        round3[13] = new String[] {"Pretoria and Bloemfontein are two of the three capitals in which country?", "3", "South Africa", "Denmark", "Indonesia", 
            "The Netherlands", "80", "80"};

        String[][] round4 = new String[13][8];
        round4[0] = new String[] {"What is Canada's national motto?", "3", "From Sea to Sea", "True North Strong and Free", "In All of Us Command", 
            "Double-Double with Timbits", "75", "80"}; 
        round4[1] = new String[] {"What is the product of the number of U.S. states in 1860 times the number of U.S. states in 1776?", "3", "442", "2500", "169", "650", 
            "80", "85"}; 
        round4[2] = new String[] {"How many countries are in South America?", "3", "12", "23", "6", "41", "80", "85"};
        round4[3] = new String[] {"If you have suffered a myocardial infarction, you have had a...?", "1", "Heart attack", "Stroke", "Aneurysm", "Seizure", "75", "85"};
        round4[4] = new String[] {"The endocrine system controls body processes through special chemical messengers, called what?", "1", "Hormones", "Electrical impulses",
            "DNA", "Release factors", "70", "70"};
        round4[5] = new String[] {"Who ponders \"to be, or not to be: that is the question\"?", "3", "Hamlet", "Juliet", "Romeo", "Mercutio", "70", "70"};
        round4[6] = new String[] {"All of the following are official languages of Belgium, except what?", "1", "English", "French", "German", "Dutch", "40", "45"};   
        round4[7] = new String[] {"What is the largest island nation by total land area?", "3", "Australia", "Indonesia", "Madagascar", "United Kingdom", "80", "90"};
        round4[8] = new String[] {"What does the Latin word 'avis' mean?", "2", "Bird", "Car", "Caution", "Plane", "68", "70"};
        round4[9] = new String[] {"What large seabird did the ancient mariner kill in the poem \"The Rime of the Ancient Mariner\"?", "3", "Albatross", 
            "Condor", "Seagull", "Pelican", "50", "70"};
        round4[10] = new String[] {"How many points are awarded for a safety in American football?", "4", "2", "6", "7", "0", "75", "85"};
        round4[11] = new String[] {"In what city is the United Nations headquartered?", "3", "New York City", "Geneva", "Paris", "The Hague", "75", "75"};
        round4[12] = new String[] {"Natives of what country call their capital 'Wien'?", "3", "Austria", "Germany", "Czech Republic", "Hungary", "50", "60"};

        String[][] round5 = new String[12][8]; 
        round5[0] = new String[] {"Which Russian epic novel consists of four books, and was originally published in 1,225 pages?", "3", "War and Peace", "Anna Karenina",
            "The Brothers Karamazov", "The Old Man and the Sea", "80", "85"};
        round5[1] = new String[] {"What was the name of the first American space shuttle?", "1", "Enterprise", "Discovery", "Columbia", "Challenger", "65", "75"};
        round5[2] = new String[] {"What island lies directly south of the Italian boot?", "3", "Sicily", "Crete", "Amalfi", "Corsica", "70", "70"};
        round5[3] = new String[] {"An adult human has four canines. What are canines?", "1", "Teeth", "Bones", "Arteries", "Muscles", "85", "90"};
        round5[4] = new String[] {"What late pop music star led a group called 'The Wailers'?", "2", "Bob Marley", "Prince", "Freddie Mercury", "Kurt Cobain", "90", "90"};
        round5[5] = new String[] {"In which Asian country are dragons a sign of good luck?", "3", "China", "Japan", "Taiwan", "South Korea", "75", "85"};
        round5[6] = new String[] {"What is the largest prime number less than 40?", "1", "37", "39", "31", "41", "85", "90"};
        round5[7] = new String[] {"Windows XP and Windows 7 are examples of what?", "4", "Operating systems", "PCs", "User interfaces", "RAM discs", "70", "80"};
        round5[8] = new String[] {"In what city was the idea for the World Wide Web conceived?", "4", "Geneva", "New York", "Boston", "Beijing", "45", "55"};
        round5[9] = new String[] {"In which country did Isabel Peron become the first female president?", "3", "Argentina", "Israel", "Italy", "France", "70", "90"};
        round5[10] = new String[] {"What is the name of the cylindrical tube which extends from the lower portion of the human larynx?", "1", "Trachea", "Pharynx", 
            "Esophagus", "Peristalsis", "75", "85"};
        round5[11] = new String[] {"The human metabolic rhythm, usually coincident with the 24-hour day, is known by what name?", "1", "Circadian", "Melatonic", "Diurnal", 
            "Catabolic", "50", "60"};

        String[][] round6 = new String[11][8]; 
        round6[0] = new String[] {"In the \"Road Runner and Coyote\" cartoons, what famous sound does the roadunner make?", "2", "Beep! Beep!", "Vroom! Vroom!", 
            "Aooga! Aooga!", "Ping! Ping!", "60", "60"};
        round6[1] = new String[] {"If an American travels due south from Detroit, Michigan, which foreign country will he encounter first?", "3", "Canada", 
            "Mexico", "Cuba", "Venezuela", "50", "50"};        
        round6[2] = new String[] {"Which country contains the highest number of animal and plant species, making it the world's most biodiverse nation?", 
            "4", "Brazil", "Russia", "Indonesia", "Australia", "50", "50"};
        round6[3] = new String[] {"Which river, because it was both 'beautiful and blue', inspired Johann Strauss to compose one of his most famous waltzes?", "4", 
            "Danube", "Seine", "Volga", "Rhine", "50", "50"};
        round6[4] = new String[] {"In US politics, who possesses a veto power over legislation passed by Congress?", "3", "The president", "The Supreme Court", 
            "The people", "The Senate", "60", "60"};
        round6[5] = new String[] {"What ancient civilization was located in the Andes Mountains of South America?", "3", "Inca", "Maya", "Aztec", "Navajo", "65", "65"};      
        round6[6] = new String[] {"Which famous Canadian was drafted 15th overall in the 1996 NBA draft by the Phoenix Suns?", "4", "Steve Nash", "Jamaal Magloire", 
            "Samuel Dalembert", "Andrew Wiggins", "65", "70"};       
        round6[7] = new String[] {"What are the calcium carbonate deposits that form on the floors of caves called?", "2", "Stalagmites", "Stalactites", 
            "Pillars", "Cave spikes", "55", "60"};             
        round6[8] = new String[] {"Who was the first person to have been awarded the Nobel prize in two separate fields of science?", "2", "Marie Curie", 
            "Albert Einstein", "Richard Feynman", "Charles Darwin", "50", "55"};
        round6[9] = new String[] {"In major league baseball, who is the pitcher with the most career wins?", "4", "Cy Young", "Roy Halladay", "Pedro Rodriguez", 
            "Sandy Koufax", "60", "60"};
        round6[10] = new String[] {"Which river runs through Vientiane, the capital of Laos?", "3", "Mekong River", "Yangtze River", "Dong Nai River", "Golden River",
            "50", "60"};      

        String[][] round7 = new String[11][8]; 
        round7[0] = new String[] {"French president Charles de Gaulle caused controversy at what event by declaring \"Vive le Quebec Libre\"?", "3", "Expo 67", 
            "The Summit Series", "The Oka Protests", "The 1973 Quebec provincial election", "70", "70"};
        round7[1] = new String[] {"In baseball, how many runs does a grand slam score?", "4", "4", "3", "2", "1", "75", "75"};
        round7[2] = new String[] {"If you were \"zozzled\" in the 1920s, what are you now?", "3", "Drunk", "Happy", "Single", "Insane", "70", "60"};
        round7[3] = new String[] {"Which Western Conference NBA team plays their home games in the Oracle Arena?", "4", "Golden State Warriors", "Sacramento Kings",
            "Houston Rockets", "New York Knicks", "70", "70"};
        round7[4] = new String[] {"Which stock market index hit a huge peak of 5,132.52 on March 10, 2000?", "3", "NASDAQ", "Dow Jones", "NIKKEI", "TSX", "70", "65"};
        round7[5] = new String[] {"At 22km in length, the Vallee Blanche is the world's longest ski run. Which country is it located in?", "4", "France", "Switzerland",
            "Canada", "Austria", "60", "60"};
        round7[6] = new String[] {"How many American presidents have been assassinated while in office?", "3", "Four", "Three", "Two", "Five", "60", "65"};
        round7[7] = new String[] {"How many players define maximum capacity for a traditional game of \"Hungry Hungry Hippos\"?", "4", "Four", "Six", "Two", "Three", 
            "70", "65"};
        round7[8] = new String[] {"What is the more common name for a prairie wolf?", "2", "Coyote", "Prairie dog", "Meerkat", "Red fox", "65", "60"};
        round7[9] = new String[] {"Which U.S. state is nicknamed \"The Gopher State\"?", "3", "Minnesota", "North Dakota", "Michigan", "New Mexico", "65", "60"};
        round7[10] = new String[] {"America's first national park was established by Ulysses S. Grant in 1872. What was the park?", "3", "Yellowstone", "Grand Canyon", "Glacier", "Arches", "65", "60"};

        String[][] round8 = new String[11][8]; 
        round8[0] = new String[] {"The Battle of Antietam was a major battle in which war?", "3", "American Civil War", "World War One", "World War Two", 
            "War of 1812", "40", "50"};
        round8[1] = new String[] {"What was Stanley Kubrick's last film, released in 1999?", "2", "Eyes Wide Shut", "2001: A Space Odyssey", "A Clockwork Orange", 
            "Black Hawk Down", "55", "55"}; 
        round8[2] = new String[] {"Which World War Two battle saw an estimated two million casualties, and is believed to be the bloodiest battle in the history of mankind?", "3", "The Battle of Stalingrad", "The Battle of Midway", "The Battle of Iwo Jima", 
            "The Normandy Landings", "65", "65"};
        round8[3] = new String[] {"Which of the following authors is known for their witty, epigrammatic style, seen most famously in \"The Picture of Dorian Gray\"?",
            "2", "Oscar Wilde", "Mary Shelley", "Kurt Vonnegut", "Albert Camus", "70", "70"};
        round8[4] = new String[] {"On the U.S. adaptation of BBC's \"The Office\", who is analogous to David Brent?", "2", "Michael Scott", "Jim Halpert", "Dwight Schrute", 
            "Kelly Kapur", "55", "55"};
        round8[5] = new String[] {"What disease was referred to by the 17th century Huron people as the \"medicine of the black robes\"?", "3", "Smallpox", "Syphilis", "Scarlet fever", "Bubonic plague", "60", "60"};
        round8[6] = new String[] {"If the legs of a right-angled triangle are 15 centimeters and 8 centimeters, how long is the hypotenuse, in centimeters?", "1", "17", "23", "19", "120", "70", "60"};
        round8[7] = new String[] {"Which German state is famous for its Oktoberfest beer festival?", "4", "Bavaria", "Saxony", "Hesse", "Brandenburg", "40", "40"};
        round8[8] = new String[] {"What is the only marsupial native to North America?", "4", "Opossum", "Raccoon", "Potoroo", "Wallaby", "40", "40"};
        round8[9] = new String[] {"What Florentine painter, said to embody the spirit of the Renaissance, painted \"The Birth of Venus\"?", "3", "Sandro Botticelli", "Michelangelo", "Donatello", "Filippo Brunelleschi", "40", "40"};
        round8[10] = new String[] {"Located between Uzbekistan and Kazakhstan, which sea has drastically shrunk since the mid-20th century?", "3", "Aral Sea", "Caspian Sea", "Aydar Sea", "Shardara Sea", "40", "40"};

        String[][] round9 = new String[11][8]; 
        round9[0] = new String[] {"Which 90s-2000s alt-rock band released the Blue Album, the Green Album, and the Red Album?", "2", "Weezer", "Green Day", "Radiohead", 
            "Pearl Jam", "50", "50"};
        round9[1] = new String[] {"Lemurs are primates found only on which island?", "1", "Madagascar", "Mauritius", "Sri Lanka", "Tasmania", "60", "60"};
        round9[2] = new String[] {"The Great Emu War of 1932 was fought betweeen 20,000 wild emus and soldiers from which country?", "3", "Australia", "New Zealand", 
            "Chile", "South Africa", "50", "50"};   
        round9[3] = new String[] {"What was the name of the first atomic bomb detonated in active combat?", "3", "Little Boy", "Fat Man", "Enola Gay", 
            "Dear John", "55", "55"};   
        round9[4] = new String[] {"In the board game \"Clue\", what is connected by a secret door to the study?", "2", "Kitchen", "Library", "Lounge", 
            "Ballroom", "50", "50"};  
        round9[5] = new String[] {"Who won the Nobel Prize for Literature in 1957 and died in a car crash in 1960?", "3", "Albert Camus", "Jean-Paul Sartre", "Kurt Vonnegut", 
            "Thomas Pynchon", "40", "50"};
        round9[6] = new String[] {"From the 21st of February to the lst of December, 1916, the German army attacked French forces in what battle?", "3", "Battle of Verdun",
            "Battle of Passchendaele", "Battle of Beaumont-Hamel", "Battle of Champagne", "35", "45"};    
        round9[7] = new String[] {"In what borough of New York City would you find Coney Island?", "4", "Brooklyn", "The Bronx", "Manhattan", "Queens", "40", "50"};   
        round9[8] = new String[] {"In what new category was the Nobel Prize first awarded in 1969?", "3", "Economics", "Peace", "Medicine", "Literature", "35", "50"};   
        round9[9] = new String[] {"On which U.S. holiday is the Indianapolis 500 run?", "2", "Memorial Day", "Labour Day", "President's Day", "Thanksgiving", "35", "50"}; 
        round9[10] = new String[] {"Bill Tilden achieved fame in what sport?", "4", "Tennis", "Ice skating", "Baseball", "American football", "40", "50"}; 

        String[][] round10 = new String[10][8]; 
        round10[0] = new String[] {"Babe Ruth played his first major-league baseball game as a member of what team?", "4", "Boston Red Sox", "New York Yankees", "Chicago Cubs", "Brooklyn Dodgers", "35", "40"};
        round10[1] = new String[] {"In what play does Petruchio woo Katherina in order to allow her sister Bianca to be married?", "3", "The Taming of the Shrew", "A Midsummer Night's Dream", "Twelfth Night", "Much Ado About Nothing", "60", "60"};
        round10[2] = new String[] {"Who was the eccentric Russian philosopher and writer that learned to ride a bike at age 67?", "4", "Leo Tolstoy", "Fyodor Dostoyevsky", "Vladimir Nabokov", "Anton Chekhov", "40", "40"};
        round10[3] = new String[] {"What is the largest of the Japanese island?", "3", "Honshu", "Hokkaido", "Shikoku", "Kyushu", "25", "20"};
        round10[4] = new String[] {"What is the heaviest element that can be created in stellar fusion?", "1", "Iron", "Helium", "Neon", "Lead", "25", "10"};
        round10[5] = new String[] {"Which planet features large white ovals and a 300 year-old red spot?", "1", "Jupiter", "Saturn", "Mars", "Venus", "30", "20"};
        round10[6] = new String[] {"Which U.S. state borders British Columbia, Alberta and Saskatchewan?", "3", "Montana", "Idaho", "Washington", "North Dakota", "45", "50"};
        round10[7] = new String[] {"The centre of mass of a triangle lies at its what?", "1", "Centroid", "Circumcentre", "Orthocentre", "Incenter", "30", "30"};
        round10[8] = new String[] {"Which of these countries has no territory north of the Arctic Circle?", "3", "Denmark", "Sweden", "Finland", "Norway", "35", "40"};
        round10[9] = new String[] {"What body of water separates Sweden from Finland?", "3", "Gulf of Bothnia", "North Sea", "Baltic Sea", "Gulf of Finland", "30", "20"};        

        String[][] round11 = new String[9][8]; 
        round11[0] = new String[] {"Located south of Cairo, with a current namesake in the United States, what city was the capital of Ancient Egypt?", "3", "Memphis", "Chicago", "Sacramento", "Atlanta", "25", "10"};
        round11[1] = new String[] {"What country won the most medals in the 1976 Olympic Games in Montreal?", "4", "Soviet Union", "United States", "Spain", "China", "60", "60"};
        round11[2] = new String[] {"What class of rock covers about three quarters of the earth's surface?", "1", "Sedimentary", "Igneous", "Metamorphic", "Basaltic", "30", "40"};
        round11[3] = new String[] {"Of the four sections in a typical orchestra, which one usually includes the most musicians?", "4", "Strings", "Woodwinds", "Brass", "Percussion", "50", "50"};
        round11[4] = new String[] {"The word 'laser' is an acronym. What does the 'a' stand for?", "1", "Amplification", "Astronomical", "Amalgam", "Adenylation", "30", "20"};
        round11[5] = new String[] {"In the 20th century, four consecutive Summer Olympics were held in cities whose names start with what common letter?", "4", "M", "T", "A", "L", "50", "50"};
        round11[6] = new String[] {"The VS-300, built by Russian engineer Igor Sikorsky in the early days of the Second World War, was the first operational American version of what type of machine?",
            "3", "Helicopter", "Ballistic missile", "Submarine", "Automatic rifle", "40", "50"};
        round11[7] = new String[] {"Which country hosted the 2006 FIFA World Cup?", "4", "Germany", "Brazil", "South Africa", "Italy", "50", "50"};
        round11[8] = new String[] {"On which Shakespearean play is the plot of Disney's 'Lion King' loosely based?", "2", "Hamlet", "Richard III", "Othello", "The Tempest", "30", "30"};

        String[][] round12 = new String[9][8]; 
        round12[0] = new String[] {"Which of the following is not a bone of the middle ear?", "1", "Ethmoid", "Malleus", "Stapes", "Incus", "40", "50"};
        round12[1] = new String[] {"Which U.S. president has quotes including \"is our children learning?\" and \"they misunderestimated me\"?", "3",
            "George W. Bush", "Ronald Reagan", "Richard Nixon", "Bill Clinton", "50", "50"}; 
        round12[2] = new String[] {"Which of the following is not a bone of the middle ear?", "1", "Ethmoid", "Malleus", "Stapes", "Incus", "40", "50"};  
        round12[3] = new String[] {"Who was the general-in-chief of the Union Army from July 1862 to March 1864?", "3", "Henry Halleck", "George B. McLellan", 
            "Ulysses S. Grant", "William Tecumseh Sherman", "30", "50"};  
        round12[4] = new String[] {"What metallic nickname is given to the age of civilization that occurred between 1000 BCE and 400 BCE?", "3", "Iron Age", "Copper Age", 
            "Bronze Age", "Golden Age", "35", "30"};
        round12[5] = new String[] {"Which immigrant group came to Western Canada in the 1870s afier a volcanic eruption in their homeland?", "3", "Icelanders", "Japanese", 
            "Korean", "Ukrainian", "35", "30"};
        round12[6] = new String[] {"Inside which organ of the body are the fluids the most alkaline?", "2", "Pancreas", "Small intestine", "Brain", "Gallbladder", "30",
            "40"};
        round12[7] = new String[] {"What is the second most abundant element in the observable universe?", "2", "Helium", "Neon", "Carbon", "Lithium", "35", "40"};
        round12[8] = new String[] {"What was the name of Canada's first space satellite?", "2", "Alouette", "Caribou", "Sapphire","Pioneer", "30", "40"};

        String[][] round13 = new String[9][8]; 
        round13[0] = new String[] {"A common, ubiquitous benchmark may be referred to as a \"rule of thumb\"? What did the original, 17th-century Rule of Thumb govern?",
            "3", "Domestic abuse in England", "Butter prices", "Trade routes", "Hat sizes", "30", "30"};
        round13[1] = new String[] {"In physics, the SI unit for capacitance is the...", "1", "Farad", "Siemen", "Weber", "Henry", "25", "15"}; 
        round13[2] = new String[] {"If I were a philatelist, what would I collect?", "4", "Stamps", "Coins", "Sports cards", "Buttons", "30", "20"}; 
        round13[3] = new String[] {"What is the more common name for \"graminoid\" plants, such as bamboo?", "2", "Grasses", "Shrubs", "Mangroves", "Reeds", "25", "15"}; 
        round13[4] = new String[] {"The 1971 mission of the Canadian environmentalist group, Greenpeace, was to prevent nuclear testing in which remote location?", "2",
            "Amchitka, Alaska", "Resolute, Nunavut", "Baker, Nevada", "Bikini Atoll, Marshall Island", "25", "15"}; 
        round13[5] = new String[] {"Of the seven ancient wonders of the world, one was a lighthouse marking the harbour of what city?", "3", "Alexandria", "Constantinople", 
            "Rio de Janeiro", "Amsterdam", "30", "20"}; 
        round13[6] = new String[] {"The city of North Bay, Ontario is located on the shores of what lake?", "4", "Lake Nipissing", "Lake Superior", "Lake Huron", 
            "Lake Ontario", "25", "15"}; 
        round13[7] = new String[] {"Which country lost much of its territory to Brazil, Uruguay and Argentina in the 19th century War of the Triple Alliance?", 
            "3", "Paraguay", "Chile", "Guyana", "Peru", "30", "20"}; 
        round13[8] = new String[] {"Who is the only NHL goaltender to have scored a game-winning goal?", "2", "Martin Brodeur", "Patrick Roy", "Vesa Toskala", 
            "Tuukka Rask", "25", "15"};

        String[][] round14 = new String[8][8]; 
        round14[0] = new String[] {"The HMS Argus was the first of which type of ship developed for naval warfare?", "3", "Aircraft carrier", "Battleship", "Destroyer", "Man o' war", "30", "30"};
        round14[1] = new String[] {"With which empire can you associate the word \"quipu\"?", "3", "Incan", "Roman", "Aztec", "Mongol", "25", "30"};
        round14[2] = new String[] {"What 1915 film was the first American 12-reel motion picture?", "4", "The Birth of a Nation", "Intolerance", "Broken Blossoms", "In Old California",
            "30", "15"};
        round14[3] = new String[] {"Two books of the Old Testament have women's names as titles. One is Ruth. Name the other.", "4", "Esther", "Mary", "Sarah", "Miriam", "30", "30"};
        round14[4] = new String[] {"In 1973, who led a coup that overthrew the elected government of Salvador Allende?", "3", "Augusto Pinochet", "Jean-Claude Duvalier", "Manuel Noriega", "Juvenal Habyarimana", "30", "15"};
        round14[5] = new String[] {"In what year did the Anglo-Iraqi War begin and end?", "3", "1941", "1966", "1890", "1991", "25", "20"};
        round14[6] = new String[] {"What is an erubescent person doing?", "4", "Blushing", "Frowning", "Smiling", "Laughing", "30", "20"};
        round14[7] = new String[] {"Gram staining is an empirical method of differentiating between types of what?", "1", "Bacteria", "Alloys", "Wood", "Blood types", "35", "30"};

        String[][] round15 = new String[7][8]; 
        round15[0] = new String[] {"Which Winter Olympic Games saw a blizzard during the Opening Ceremony, immediately followed by temperatures as high as 25 C?",
            "4", "1928 St. Moritz", "1936 Garmisch", "1952 Oslo", "1972 Sapporo", "25", "10"};
        round15[1] = new String[] {"In curling, a stone cannot be released by a curler past the...", "4", "Hog line","Back line", "Tee line", "House line", "25", "10"};
        round15[2] = new String[] {"In 2011, Tennessee lawmakers made what common act illegal?", "4", "Sharing Netflix passwords", "Driving while dehydrated", "Selling plastic bags",
            "Music pirating", "25", "10"};
        round15[3] = new String[] {"Which African country's soccer team is known as 'The Squirrels'?", "4", "Benin","Burkina Faso", "Gambia", "Zaire", "25", "10"};
        round15[4] = new String[] {"Which chemical element was first isolated by Sir Humphry Davy in 1808, using electrolysis?", "1", "Barium","Calcium", "Strontium", "Fluorine", "25", "0"};
        round15[5] = new String[] {"What country's King Charles XIV had a tattoo that read 'Death to all Kings'?", "3", "Sweden","England", "France", "Denmark", "25", "0"};
        round15[6] = new String[] {"An Ontario farmer discovered new seedlings on his farm in 1811, eventually leading him to produce an abundance of what food?", "4", "McIntosh apples","Freestone peaches", "Highbush blueberries", "Purple potatoes", "25", "5"};

        String[][][] questionSet = new String[][][] {round1, round2, round3, round4, round5, round6, round7, round8, round9, round10, round11, round12, round13, round14, round15};

        // now comes the actual question choosing
        Random rand = new Random();
        int roundNum = round-1;
        int questionNum = rand.nextInt(questionSet[roundNum].length); // question number is randomized, so for each round you don't know what question you will get 
        return questionSet[roundNum][questionNum]; // returns all of the data stored for each question
    }
}