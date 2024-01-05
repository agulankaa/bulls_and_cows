package bullscows;

import java.util.*;

public class Main {
    char[] password;
    int length;
    int range;
    int turnNumber;

    public Main(int length, int range){
        if(length != 0 && range != 0) {
            this.length = length;
            this.range = range;
            this.password = generatePassword(length, range);
        }
        turnNumber = 1;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input the length of the secret code:");
        String lengthString = scanner.nextLine();
        int passLen = 0;
        int passRange = 0;
        try {
            passLen = Integer.parseInt(lengthString);
            if(passLen <= 0){
                System.out.println("Error: Length must be greater than 0");
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", lengthString);
        }

        if(passLen != 0) {
            System.out.println("Input the number of possible symbols in the code:");
            String rangeString = scanner.nextLine();
            try {
                passRange = Integer.parseInt(rangeString);
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.printf("Error: %s isn't a valid number.\n", rangeString);
            }
        }

        if (passLen != 0 && passRange != 0) {
            Main game = new Main(passLen, passRange);
            if(game.password != null){
                System.out.println("Okay, let's start a game!");
                boolean endgame = false;
                while (!endgame) {
                    System.out.printf("Turn %d:\n", game.turnNumber);
                    String guess = scanner.nextLine();
                    char[] inputNumbers = guess.toCharArray();
                    endgame = game.validateAnswer(inputNumbers);
                }
            }
        }

    }

    public char[] generatePassword(int len, int range){
        if(range > 36){
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return null;
        }
        else if(len > range){
            System.out.printf("Error: it's not possible to generate a code with a length of %d ", len);
            System.out.printf("with %d unique symbols.\n", range);
            return null;
        }
        Random rand = new Random();
        StringBuilder password = new StringBuilder(len);

        for(int i=0; i<len; i++){
            String number = drawSymbol(range, rand);
            while(password.indexOf(number) != -1){
                number = drawSymbol(range, rand);
            }
            password.append(number);
        }
        char[] charArray = new char[password.length()];
        password.getChars(0, password.length(), charArray, 0);

        String passStars = "*".repeat(len);
        System.out.printf("The secret is prepared: %s %s\n", passStars, displayRanges(range));
        //System.out.println(charArray);

        return charArray;
    }

    public boolean validateAnswer(char[] inputArray) {
        boolean endGame = false;
        int bulls = 0;
        int cows = 0;

        if(inputArray == null || inputArray.length != this.length){
            System.out.printf("Error: incorrect guess, it should be %d symbols long", this.length);
            return endGame;
        }

        String pass = new String(this.password);
        this.turnNumber++;

        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] == this.password[i]) {
                bulls++;
            } else if (pass.contains(String.valueOf(inputArray[i]))) {
                cows++;
            }
        }
        if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None.");
        } else if (bulls == this.length) {
            System.out.printf("Grade: %d bulls\n", bulls);
            System.out.println("Congratulations! You guessed the secret code.");
            endGame = true;
        }
        else{
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
        }
        return endGame;
    }

    public static String drawSymbol(int range, Random rand) {
        int min = 0;
        int num = rand.nextInt((range - min)) + min;
        String number;
        if (num >= 10) {
            number = String.valueOf((char) ('a' + (num - 10)));
        } else {
            number = "" + num;
        }
        return number;
    }

    public static String displayRanges(int range){
        if(range <=10){
            return String.format("(0-%d)", range-1);
        }
        else{
            char upperLetter = (char) ('a' + (range-11));
            return String.format("(0-9, a-%c)", upperLetter);
        }
    }
}


