
package jetserver.util;


public class Strings {

    public static byte[] getAsciiBytes(String string) {
	int size = string.length();
	byte result[] = new byte[size];
	for (int i = 0; i < size; i++) {
	    result[i] = (byte) string.charAt(i);
	}
	return result;
    }
}
