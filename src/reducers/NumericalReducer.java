package reducers;

import java.util.BitSet;

public class NumericalReducer {
    public static int reduceToBytes(String operand, String operation) {
        if (operand == null || operand.isEmpty()) return 0;
        if (operation.toUpperCase().equals("START") || operation.toUpperCase().equals("END")) return 0;

        int numOfBytes = 0;

        switch (Character.toUpperCase(operand.charAt(0))) {
            case 'C':
                numOfBytes = operand.length() - 3;
                break;
            case 'X':
                numOfBytes = (operand.length() - 3) >> 1;
                break;
            default:
                if (operation.equals("RESB")) {
                    numOfBytes = Integer.parseInt(operand);
                }
                else if (operation.equals("RESW")) {
                    numOfBytes = Integer.parseInt(operand) * 3;
                }
                else {
                    assert operation.toUpperCase().equals("WORD");
                    numOfBytes = 3;
                }
                break;
        }

        return numOfBytes;
    }

    public static String placeIn(int digits, String str) {
        String newHex = new String(str);
        
        while (newHex.length() < digits)
            newHex = "0" + newHex;

        return newHex.toUpperCase();
    }

    public static boolean isIndexed(String operand) {
        if (operand == null || operand.isEmpty()) return false;

        for (int i = 0; i < operand.length(); i++)
            if (i + 1 < operand.length() && operand.substring(i, i + 2).toUpperCase().equals(",X"))
                return true;
        
        return false;
    }

    public static String bitSetToHex(BitSet binInstruction) {
        String hexString = "";

        for (int i = 0; i < binInstruction.length(); i++)
            hexString += binInstruction.get(i) == true ? '1' : '0';
        
        hexString = Integer.toHexString(Integer.parseInt(hexString, 2));

        return placeIn(6, hexString);
    }

    public static String dirToObj(String operation, String operand) {
        String hexObjCode = "";

        switch (operation.toUpperCase()) {
            case "BYTE":
                if (Character.toUpperCase(operand.charAt(0)) == 'C') {
                    for (int i = 0; i < operand.length(); i++) {
                        if (i == 0 || i == 1 || i == operand.length() - 1) continue;
                        hexObjCode = hexObjCode.concat(reduceToHex((int) operand.charAt(i), 2));
                    }
                }
                else if (Character.toUpperCase(operand.charAt(0)) == 'X') {
                    for (int i = 0; i < operand.length(); i++) {
                        if (i == 0 || i == 1 || i == operand.length() - 1) continue;
                        hexObjCode += Character.toUpperCase(operand.charAt(i));
                    }
                }
                break;
            case "RESB":
                hexObjCode = null;
                break;
            case "WORD":
                hexObjCode = reduceToHex(Integer.parseInt(operand), 6);
                break;
            case "RESW":
                hexObjCode = null;
                break;
            default:
                throw new IllegalArgumentException("Unidentified Directive");
        }

        return hexObjCode;
    }

    public static int reduceToDec(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static String fromHexToBinary(String hex) {
        return Integer.toBinaryString(Integer.parseInt(hex, 16));
    }

    public static String fromHexToBinary(String hex, int digits) {
        return placeIn(digits, Integer.toBinaryString(Integer.parseInt(hex, 16)));
    }

    public static String reduceToHex(int dec) {
        return Integer.toHexString(dec).toUpperCase();
    }

    public static String reduceToHex(int dec, int digits) {
        return placeIn(digits, Integer.toHexString(dec).toUpperCase());
    }
}
