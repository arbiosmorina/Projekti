import java.io.*;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Base32Test {
    public class base32Imp {
        private static final String base32Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        private static final int[] base32Lookup = { 0xFF, 0xFF, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
                0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
                0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
                0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
                0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
                0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
                0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
                0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF
        };

        /**
         * Encodes byte array to Base32 String.
         *
         * @param bytes Bytes to encode.
         * @return Encoded byte array <code>bytes</code> as a String.
         *
         */

        static public String encode(final byte[] bytes) {
            int i = 0, index = 0, digit = 0;
            int currByte, nextByte;
            StringBuffer base32 = new StringBuffer((bytes.length + 7) * 8 / 5);

            while (i < bytes.length) {
                currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256);

                /* Is the current digit going to span a byte boundary? */
                if (index > 3) {
                    if ((i + 1) < bytes.length) {
                        nextByte = (bytes[i + 1] >= 0)
                                ? bytes[i + 1]
                                : (bytes[i + 1] + 256);
                    } else {
                        nextByte = 0;
                    }

                    digit = currByte & (0xFF >> index);
                    index = (index + 5) % 8;
                    digit <<= index;
                    digit |= nextByte >> (8 - index);
                    i++;
                } else {
                    digit = (currByte >> (8 - (index + 5))) & 0x1F;
                    index = (index + 5) % 8;
                    if (index == 0)
                        i++;
                }
                base32.append(base32Chars.charAt(digit));
            }

            return base32.toString();
        }

        /**
         * Decodes the given Base32 String to a raw byte array.
         *
         * @param base32
         * @return Decoded <code>base32</code> String as a raw byte array.
         */
        static public byte[] decode(final String base32) {
            int i, index, lookup, offset, digit;
            byte[] bytes = new byte[base32.length() * 5 / 8];

            for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
                lookup = base32.charAt(i) - '0';

                /* Skip chars outside the lookup table */
                if (lookup < 0 || lookup >= base32Lookup.length) {
                    continue;
                }

                digit = base32Lookup[lookup];

                /* If this digit is not in the table, ignore it */
                if (digit == 0xFF) {
                    continue;
                }

                if (index <= 3) {
                    index = (index + 5) % 8;
                    if (index == 0) {
                        bytes[offset] |= digit;
                        offset++;
                        if (offset >= bytes.length)
                            break;
                    } else {
                        bytes[offset] |= digit << (8 - index);
                    }
                } else {
                    index = (index + 5) % 8;
                    bytes[offset] |= (digit >>> index);
                    offset++;

                    if (offset >= bytes.length) {
                        break;
                    }
                    bytes[offset] |= digit << (8 - index);
                }
            }
            return bytes;
        }



//      * For testing, take a command-line argument in Base32, decode,
//      * print in hex, encode, print
        static public void main(String[] args) {
            if (args.length == 0) {
                System.out.println("Supply a Base32-encoded argument.");
                return;
            }
            System.out.println(" Original: " + args[0]);
            byte[] decoded = base32Imp.decode(args[0]);
            System.out.print("      Hex: ");
            for (int i = 0; i < decoded.length; i++) {
                int b = decoded[i];
                if (b < 0) {
                    b += 256;
                }
                System.out.print(
                        (Integer.toHexString(b + 256)).substring(1));
            }
            System.out.println();
            System.out.println("Reencoded: " + base32Imp.encode(decoded));
        }
    }
    public static void main(String[] a) {

        Scanner input = new Scanner(System.in);
        String test;
        System.out.println("Jepni nje input i cili do te enkodohet: ");
        String theInput;
        theInput = input.nextLine();




        String theEncoded = base32Imp.encode(theInput.getBytes());
        byte[] theDecoded = base32Imp.decode(theEncoded);
        System.out.println("Input   : "+theInput);
        System.out.println("Encoded : "+theEncoded);

        System.out.println("Decoded : "+new String(theDecoded));
        input.close();
        return;

    }
}


