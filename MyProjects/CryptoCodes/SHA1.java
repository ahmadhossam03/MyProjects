import java.math.BigInteger;
import java.util.Scanner;

public class SHA1 {
    //SHA-1 ALGORITHM
    public static String Hex2Binary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }

    public static String Bin2Hex(String bin) {
        return new BigInteger(bin, 2).toString(16).toUpperCase();
    }

    // Converts a binary string to a decimal string.
    public static String BinToDec(String bin) {
        return new BigInteger(bin, 2).toString(10);
    }

    // Converts a decimal string to a binary string.
    public static String DecToBin(String dec) {
        return new BigInteger(dec, 10).toString(2);
    }

    // Converts an ASCII string to a binary string.
    public static String AsciiToBin(String ascii) {
        byte[] bytes = ascii.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    public static String BinToAscii(String bin) {
        String[] binArray = bin.split("    ");
        StringBuilder ascii = new StringBuilder();
        for (String binStr : binArray) {
            int charCode = Integer.parseInt(binStr, 2);
            ascii.append((char) charCode);
        }
        return ascii.toString();
    }

    public static String h0 = "01100111010001010010001100000001";
    public static String h1 = "11101111110011011010101110001001";
    public static String h2 = "10011000101110101101110011111110";
    public static String h3 = "00010000001100100101010001110110";
    public static String h4 = "11000011110100101110000111110000";

    public static String xorBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            sb.append(a.charAt(i) ^ b.charAt(i));
        }
        return sb.toString();
    }

    public static String andBinary(String a, String b) {
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            result += (a.charAt(i) == '1' && b.charAt(i) == '1') ? '1' : '0';
        }
        return result;
    }

    public static String notBinary(String a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            sb.append(a.charAt(i) == '0' ? '1' : '0');
        }
        return sb.toString();
    }

    public static String[] splitWord(String word) {
        //Create array word that hold 80 elements
        String[] W = new String[80];
        //Split the word into 16 32-bit words
        for (int i = 0; i < 16; i++) {
            W[i] = word.substring(i * 32, (i + 1) * 32);
        }
        for (int i = 16; i < 80; i++) {
            String xor1 = xorBinary(W[i - 3], W[i - 8]);
            String xor2 = xorBinary(xor1, W[i - 14]);
            W[i] = xorBinary(xor2, W[i - 16]);
            //rotate left by 1
            W[i] = W[i].substring(1) + W[i].charAt(0);
        }
        return W;
    }


    public static String leftRotate(String bin, int n) {
        for (int i = 0; i < n; i++) {
            bin = bin.substring(1) + bin.charAt(0);
        }
        return bin;
    }

    public static String padding(String bin) {
        int len = bin.length();
        //remove spaces from the binary string
        bin = bin.replaceAll("\\s", "");
        //add 1 to the end of the binary string
        bin = bin + "1";
        //keep adding 0's to binary string until the length of the binary string mod 512 is 448
        while (bin.length() % 512 != 448) {
            bin = bin + "0";
        }
        //add the length of the binary string as a 64-bit binary number
        String binLen = DecToBin(Integer.toString(len));
        while (binLen.length() < 64) {
            binLen = "0" + binLen;
        }
        bin = bin + binLen;
        return bin;
    }

    //function to add the 32-bit binary strings , ignore carry
    public static String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            int sum = (a.charAt(i) - '0') + (b.charAt(i) - '0') + carry;
            sb.append(sum % 2);
            carry = sum / 2;
        }
        return sb.reverse().toString();
    }

    public static String functionOne(String B, String C, String D) {
        String S = xorBinary(andBinary(B, C), andBinary(notBinary(B), D));
        return S;
    }

    public static String functionTwo(String B, String C, String D) {
        String S = xorBinary(xorBinary(D, B), C);
        return S;
    }

    public static String functionThree(String B, String C, String D) {
        String s1 = andBinary(C, B);
        String s2 = andBinary(B, D);
        String s3 = andBinary(D, C);
        String S = xorBinary(xorBinary(s1, s2), s3);
        return S;
    }

    public static String functionFour(String B, String C, String D) {
        String S = xorBinary(xorBinary(D, B), C);
        return S;
    }

    public static String function(String[] Words) {

        String A = h0;
        String B = h1;
        String C = h2;
        String D = h3;
        String E = h4;

        for (int j = 0; j < 80; j++) {
            String F = "";
            String K = "";
            if (j >= 0 && j <= 19) {
                F = functionOne(B, C, D);
                K = "01011010100000100111100110011001";
            } else if (j >= 20 && j <= 39) {
                F = functionTwo(B, C, D);
                K = "01101110110110011110101110100001";
            } else if (j >= 40 && j <= 59) {
                F = functionThree(B, C, D);
                K = "10001111000110111011110011011100";
            } else if (j >= 60 && j <= 79) {
                F = functionFour(B, C, D);
                K = "11001010011000101100000111010110";
            }
            String temp = addBinary(addBinary(addBinary(addBinary(leftRotate(A, 5), F), E), K), Words[j]);
            E = D;
            D = C;
            C = leftRotate(B, 30);
            B = A;
            A = temp;


        }
        h0 = addBinary(A, h0);
        h1 = addBinary(B, h1);
        h2 = addBinary(C, h2);
        h3 = addBinary(D, h3);
        h4 = addBinary(E, h4);
        String res = h0 + h1 + h2 + h3 + h4;
        return res;


    }

    public static String SHA1(String word) {
        String binary = AsciiToBin(word); //convert the word to binary
        binary = binary.replaceAll("\\s", ""); //remove spaces from the binary string
        String padded = padding(binary); //pad the binary string
        String x = function(splitWord(padded)); //call the function to hash the word , SplitWord function splits the word into 80 32-bit words
        return Bin2Hex(x); //convert the binary hash to hexadecimal
    }


    public static void main(String[] args) {
        String word;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a word to hash (type 'exit' to terminate) : ");
            word = sc.nextLine();
            if (word.equals("exit")) {
                break;
            }
            System.out.println("SHA-1 hash: " + SHA1(word));


        }
    }
}
