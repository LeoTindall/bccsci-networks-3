import java.util.Arrays;
import java.util.Scanner;

public class CRC {

    public static void doCRC() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter number of bits in dataword: ");
        int nDataBits = s.nextInt();

        System.out.print("Enter number of bits in generator: ");
        int nGenBits = s.nextInt();

        System.out.print("Enter data bits, space separated: ");
        int[] dataBits = new int[nDataBits];
        for (int i = 0; i < nDataBits; i++) {
            dataBits[i] = s.nextInt();
        }

        System.out.print("Enter generator bits, space separated: ");
        int[] genBits = new int[nGenBits];
        for (int i = 0; i < nGenBits; i++) {
            genBits[i] = s.nextInt();
        }

        // Some additional zeroes are required, for buffering.
        int nRemainderBits = nGenBits - 1;
        int[] dataBitsWithBuffer = new int[nDataBits + nGenBits];
        System.arraycopy(dataBits, 0, dataBitsWithBuffer,0, nDataBits);
        System.out.println("dataBitsWithBuffer is " + Arrays.toString(dataBitsWithBuffer));

        // Copy initial bits to be XOR'd into a buffer
        int[] operand = new int[nGenBits];
        System.arraycopy(dataBitsWithBuffer, 0, operand, 0, nGenBits);

        int [] result = new int[nGenBits];
        // Iterate over offsets until we bump up against the end of the array
        for (int i = 0; i < dataBitsWithBuffer.length - nGenBits; i++) {
            // Perform XOR operation
            int [] operator;
            if (operand[0] == 1) {
                operator = genBits;
            } else {
                operator = new int[nGenBits];
            }
            System.out.println("\tComparing " + Arrays.toString(operand) + " with " + Arrays.toString(operator));
            for (int j = 0; j < nGenBits; j++) {
                result[j] = operand[j] ^ operator[j];
            }
            System.out.println("\tGot " + Arrays.toString(result));

            // Copy all the end of result into the beginning of operand
            System.arraycopy(result, 1, operand, 0, result.length - 1);
            // Copy the next bit from the data bits
            operand[operand.length - 1] = dataBitsWithBuffer[i + nGenBits];
        }

        int [] remainder = new int[nRemainderBits];
        System.arraycopy(result, 1, remainder, 0, nRemainderBits);

        System.out.println("The remainder is " + Arrays.toString(remainder));

        int [] toSend = new int[nDataBits + nRemainderBits];
        System.arraycopy(dataBits, 0, toSend, 0, nDataBits);
        System.arraycopy(remainder, 0, toSend, nDataBits, nRemainderBits);
        System.out.println("The following bits should be sent: " + Arrays.toString(toSend));

    }

    public static void checkCRC() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter number of bits in dataword: ");
        int nDataBits = s.nextInt();

        System.out.print("Enter number of bits in generator: ");
        int nGenBits = s.nextInt();

        System.out.print("Enter " + (nDataBits + nGenBits - 1) + " received data bits (incl. CRC), space separated: ");
        int[] dataBits = new int[nDataBits + nGenBits];
        for (int i = 0; i < nDataBits + nGenBits - 1; i++) {
            dataBits[i] = s.nextInt();
        }

        System.out.print("Enter generator bits, space separated: ");
        int[] genBits = new int[nGenBits];
        for (int i = 0; i < nGenBits; i++) {
            genBits[i] = s.nextInt();
        }

        int nRemainderBits = nGenBits - 1;
        int[] dataBitsWithBuffer = dataBits;

        // Copy initial bits to be XOR'd into a buffer
        int[] operand = new int[nGenBits];
        System.arraycopy(dataBitsWithBuffer, 0, operand, 0, nGenBits);

        int [] result = new int[nGenBits];
        // Iterate over offsets until we bump up against the end of the array
        for (int i = 0; i < dataBitsWithBuffer.length - nGenBits; i++) {
            // Perform XOR operation
            int [] operator;
            if (operand[0] == 1) {
                operator = genBits;
            } else {
                operator = new int[nGenBits];
            }
            System.out.println("\tComparing " + Arrays.toString(operand) + " with " + Arrays.toString(operator));
            for (int j = 0; j < nGenBits; j++) {
                result[j] = operand[j] ^ operator[j];
            }
            System.out.println("\tGot " + Arrays.toString(result));

            // Copy all the end of result into the beginning of operand
            System.arraycopy(result, 1, operand, 0, result.length - 1);
            // Copy the next bit from the data bits
            operand[operand.length - 1] = dataBitsWithBuffer[i + nGenBits];
        }

        int [] remainder = new int[nRemainderBits];
        System.arraycopy(result, 1, remainder, 0, nRemainderBits);

        System.out.println("The syndrome is " + Arrays.toString(remainder));

        for (int i = 0; i < nRemainderBits; i++) {
            if (remainder[i] != 0) {
                System.out.println("CRC does not match!");
                return;
            }
        }

        System.out.println("CRC matches!");

    }
}
