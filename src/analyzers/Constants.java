/**
 * @file This File is Responsible For Defining Constants
 *       Necessary For Operating And Identifying Main
 *       Supported Services Through The Assembler.
*/
package analyzers;

import java.util.HashMap;

import reducers.NumericalReducer;

/**
 * Constants Provider
 * 
 * Assembler - analysis.Constatns
*/
public class Constants {
    public static final int __DEFAULT_INSTRUCTION_SIZE__ = 3;
    public static final int __SUPPORTED_INSTRUCTIONS__ = 25;
    public static final int __DEFAULT_POSITION__ = 1;
    
    public static final String[] INSTRUCTIONS = {
        "LDA",
        "LDX",
        "LDL",
        "STA",
        "STX",
        "STL",
        "ADD",
        "SUB",
        "MUL",
        "DIV",
        "COMP",
        "TIX",
        "JEQ",
        "JGT",
        "JLT",
        "J",
        "AND",
        "OR",
        "JSUB",
        "RSUB",
        "LDCH",
        "STCH",
        "RD",
        "TD",
        "WD"
    };

    public static final String[] DIRECTIVES = {
        "BYTE",
        "RESB",
        "WORD",
        "RESW",
        "START",
        "END"
    };

    public static final HashMap<String, String> OP_CODE = buildMap();

    private Constants() {} /** Private Constructor, No Objects Available. */

    public static <T> boolean isIn(T[] container, T elem) {
        if (elem == null) return false;

        for (final T value : container)
            if (value.equals(elem))
                return true;

        return false;
    }

    private static HashMap<String, String> buildMap() {
        HashMap<String, String> codes = new HashMap<>();

        codes.put(INSTRUCTIONS[0], "00");

        for (int i = 1; i < INSTRUCTIONS.length - 3; i++) {
            int opCodeDec = NumericalReducer.reduceToDec(codes.get(INSTRUCTIONS[i - 1])) + 4;
            String opCodeHex = NumericalReducer.reduceToHex(opCodeDec, 2);
            codes.put(INSTRUCTIONS[i], opCodeHex);
        }

        String[] leftOvers = { "D8", "E0", "DC" };

        for (int i = 0; i < leftOvers.length; i++) {
            codes.put(INSTRUCTIONS[i + INSTRUCTIONS.length - 3], leftOvers[i]);
        }

        return codes;
    }
}
