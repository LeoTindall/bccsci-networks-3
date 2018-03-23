public class BitWrapper {
    /**
     * Wraps a given number to the appropriate number of bits
     * @param n the number to wrap
     * @param bits the number of bits
     */
    public static int wrapTo(int n, int bits) {
        // So long as the input is too long, keep wrapping around.
        while (n >= Math.pow(2, bits)) {
            // Take the rightmost bits; this is a little complex.
            // (1<<bits) - 1 creates a number like 111111 of bits digits; it creates (2^bits) -1
            int rightmost_bits = n&((1<<bits) - 1);
            // Take the leftmost bits by shifting off the rightmost bits.
            int leftmost_bits = n >> bits;
            // Sum the two parts.
            n = rightmost_bits + leftmost_bits;
        }
        return n;
    }

    /**
     * Inverts only the specified number of bits in the input
     * @param n input bits
     * @param bits number of bits to convert
     */
    public static int invertBits(int n, int bits) {
        // Create a mask of the correct number of 1s
        int mask = (1<<bits) - 1;
        // XOR that mask, inverting the bits
        return n ^ mask;
    }
}
