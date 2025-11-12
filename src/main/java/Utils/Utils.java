package Utils;

public class Utils {
    /**
     * Converts a hexadecimal string to an integer.
     *
     * @param str the hexadecimal string to convert
     * @return the integer value of the hexadecimal string
     * @throws NumberFormatException if the string is not a valid hexadecimal number
     */
    public int getHexValue(String str) {
        return Integer.parseInt(str, 16);
    }

    /**
     * Converts an integer to a hexadecimal string.
     *
     * @param num the integer to convert
     * @return the hexadecimal string representation of the integer
     */
    public String toHex(int num) {
        return Integer.toHexString(num);
    }
}
