import java.util.Scanner;

public class SafeInput {
    private static final Scanner pipe = new Scanner(System.in);

    public static String getNonZeroLenString(String prompt) {
        String retString = "";

        do {
            System.out.print("\n" + prompt + ": ");
            retString = pipe.nextLine();

            if (retString.length() == 0) {
                System.out.println("You must enter something.");
            }

        } while (retString.length() == 0);

        return retString;
    }

    public static int getRangedInt(String prompt, int low, int high) {
        int result = 0;
        boolean done = false;

        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: ");

            if (pipe.hasNextInt()) {
                result = pipe.nextInt();
                pipe.nextLine();

                if (result >= low && result <= high) {
                    done = true;
                } else {
                    System.out.println("Number must be between " + low + " and " + high + ".");
                }

            } else {
                System.out.println("Invalid input. Enter a whole number.");
                pipe.nextLine();
            }

        } while (!done);

        return result;
    }

    public static boolean getYNConfirm(String prompt) {
        String response = "";
        boolean done = false;
        boolean result = false;

        do {
            System.out.print("\n" + prompt + " [Y/N]: ");
            response = pipe.nextLine();

            if (response.equalsIgnoreCase("Y")) {
                result = true;
                done = true;
            } else if (response.equalsIgnoreCase("N")) {
                result = false;
                done = true;
            } else {
                System.out.println("Enter Y or N.");
            }

        } while (!done);

        return result;
    }

    public static String getRegExString(String prompt, String regEx) {
        String response = "";
        boolean done = false;

        do {
            System.out.print("\n" + prompt + ": ");
            response = pipe.nextLine();

            if (response.matches(regEx)) {
                done = true;
            } else {
                System.out.println("Invalid input.");
            }

        } while (!done);

        return response;
    }
}