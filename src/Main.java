import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hi Eyad!");
        // Loop forever performing list/act loop
        while (true) {
            System.out.println("Enter the number of the operation you'd like to perform.");
            System.out.println("\t[0]: Wrapping Addition Demo (add two numbers, but only within a given number of bits)");
            System.out.println("\t[1]: Generate Checksum");
            System.out.println("\t[2]: Check Checksum");
            System.out.println("\t[3]: Generate Fletcher Checksum");
            System.out.println("\t[4]: Check Fletcher Checksum");
            System.out.println("\t[5]: Generate CRC");
            System.out.println("\t[6]: Check CRC");
            System.out.println("\t[9]: Exit");
            System.out.print("Enter choice: ");

            // Accept a decision on what to do from the user
            Scanner s = new Scanner(System.in);
            int decision = s.nextInt();

            // Activate the correct action
            switch (decision) {
                case 0:
                    wrapInput();
                    break;
                case 1:
                    checkSumGenerate();
                    break;
                case 2:
                    checkSumCheck();
                    break;
                case 3:
                    fletcherSumGenerate();
                    break;
                case 4:
                    fletcherSumCheck();
                    break;
                case 5:
                    CRC.doCRC();
                    break;
                case 6:
                    CRC.checkCRC();
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("That's not one of the options.");
                    System.exit(1);
            }
        }
    }

    /**
     * Take user input and perform a wrapping add on it
     */
    public static void wrapInput() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter length of word (m):");
        int wordLen = s.nextInt();

        System.out.println("Enter value in decimal.");
        int value = s.nextInt();

        System.out.println("Input was " + value + ", output was " + BitWrapper.wrapTo(value, wordLen));
    }

    /**
     * Generate a checksum
     */
    public static void checkSumGenerate() {
        // Get some parameters from the user
        Scanner s = new Scanner(System.in);
        System.out.print("Enter length of word (m): ");
        int wordLen = s.nextInt();
        System.out.print("Enter number of words: ");
        int nWords = s.nextInt();

        // Get data from the user
        System.out.print("Enter bytes in decimal, separated by spaces: ");
        int[] words = getWords(s, wordLen, nWords);

        // Sum up and wrap input
        int sum = BitWrapper.wrapTo(IntStream.of(words).sum(), wordLen);
        // Invert sum
        int checkSum = BitWrapper.invertBits(sum, wordLen);

        // Report sum and checksum
        System.out.println("The sum is " + sum + " and the checksum is " + checkSum);
        // Report the words to send
        System.out.println("The words to send are as follows");
        for (int i = 0; i < nWords; i++) {
            System.out.print(words[i] + " ");
        }
        System.out.println(checkSum);
    }

    /**
     * Generate a Fletcher checksum
     */
    public static void fletcherSumGenerate() {
        // Get some parameters from the user
        Scanner s = new Scanner(System.in);
        System.out.print("Enter length of word (m): ");
        int wordLen = s.nextInt();
        System.out.print("Enter number of words: ");
        int nWords = s.nextInt();

        // Get data from the user
        System.out.print("Enter bytes in decimal, separated by spaces: ");
        int[] words = getWords(s, wordLen, nWords);

        // Perform the Fletcher algorithm. L is left, R is right
        int l = 0, r = 0;
        for (int i = 0; i < nWords; i++) {
            // Right = current right + next word, wrapped at 2 to the wordLen
            r = (r + words[i]) % (int) Math.pow(2, wordLen);
            // Left = current left plus current right, wrapped at 2 to the wordLen
            l = (l + r) % (int) Math.pow(2, wordLen);
        }

        // The sum is the left bits with the right bits appended.
        int checkSum = (l << wordLen) + r;

        System.out.println("The " + wordLen + "-bit left sum is " + l + ", the " + wordLen + "-bit right sum is " + r + ", and the " + (2 * wordLen) + "-bit checksum is " + checkSum);
        System.out.println("The words to send are as follows");
        for (int i = 0; i < nWords; i++) {
            System.out.print(words[i] + " ");
        }
        System.out.print(l + " ");
        System.out.println(r);

    }

    public static void fletcherSumCheck() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter length of word (m): ");
        int wordLen = s.nextInt();
        System.out.print("Enter number of words, including 2 for the check sum: ");
        int nWords = s.nextInt();

        System.out.print("Enter bytes in decimal, separated by spaces: ");
        int[] words = getWords(s, wordLen, nWords - 2);
        int lIn = s.nextInt();
        int rIn = s.nextInt();

        int l = 0, r = 0;
        for (int i = 0; i < nWords - 2; i++) {
            r = (r + words[i]) % (int) Math.pow(2, wordLen);
            l = (l + r) % (int) Math.pow(2, wordLen);
        }

        int checkSum = (l << wordLen) + r;
        int checkSumIn = (lIn << wordLen) + rIn;

        System.out.println("The computed " + wordLen + "-bit left sum is " + l + ", the computed " + wordLen + "-bit right sum is " + r + ", and the computed " + (2 * wordLen) + "-bit checksum is " + checkSum);
        System.out.println("The received " + wordLen + "-bit left sum is " + lIn + ", the received " + wordLen + "-bit right sum is " + rIn + ", and the received " + (2 * wordLen) + "-bit checksum is " + checkSumIn);

        if (checkSum == checkSumIn) {
            System.out.println("Checksum matches!");
        } else {
            System.out.println("Checksum does not match.");
        }

    }

    public static void checkSumCheck() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter length of word (m): ");
        int wordLen = s.nextInt();
        System.out.print("Enter number of words, including checksum: ");
        int nWords = s.nextInt();

        System.out.print("Enter bytes in decimal, separated by spaces: ");
        int[] words = getWords(s, wordLen, nWords);

        int sum = BitWrapper.wrapTo(IntStream.of(words).sum(), wordLen);
        int checkSumGenerated = BitWrapper.invertBits(sum, wordLen);

        if (checkSumGenerated == 0) {
            System.out.println("Checksum matches!");
        } else {
            System.out.println("Checksum does not match. Computed '" + checkSumGenerated + "'.");
        }

    }

    /**
     * Get some data words from the user, in decimal
     * @param s scanner
     * @param wordLen the length in bits of a word
     * @param nWords the number of words to get from the user
     * @return the array of words provided by the user
     */
    private static int[] getWords(Scanner s, int wordLen, int nWords) {
        // Allocate an array for the words
        int[] words = new int[nWords];
        for (int i = 0; i < nWords; i++) {
            // Get the word
            words[i] = s.nextInt();
            // check that it's valid
            if (words[i] > Math.pow(2, wordLen)) {
                System.out.println("Word " + words[i] + " is too large to fit in a word of length " + wordLen);
                System.exit(1);
            }
        }
        return words;
    }
}
