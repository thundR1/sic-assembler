package analyzers;

import structures.Instruction;
import java.util.ArrayList;

import data.handlers.FileHandler;

public class LexicalAnalyzer {
    public static ArrayList<Instruction> parse(FileHandler sourceFile) {
        String[] lines = sourceFile.lines();
        ArrayList<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String currentLine = lines[i].trim();
            
            if (currentLine != null && !currentLine.isEmpty() && !currentLine.isBlank())
                if (currentLine.charAt(0) == '.')
                    continue;

            ArrayList<String> tokens = tokenize(currentLine);

            if (tokens.size() == 0) continue;

            int pos = instructions.size() == 0 ? 1 : instructions.get(instructions.size() - 1).position + 1;
            
            if (tokens.size() == 3) {
                instructions.add(new Instruction(tokens.get(0), tokens.get(1), tokens.get(2), pos));
            }
            else if (tokens.size() == 2) {
                instructions.add(new Instruction(tokens.get(0), tokens.get(1), pos));
            }
            else if (tokens.size() == 1) {
                instructions.add(new Instruction(tokens.get(0), null, pos));
            }
            else {
                throw new Error("Syntax Error!");
            }
        }

        return instructions;
    }

    public static ArrayList<String> tokenize(String line) {
        ArrayList<String> tokens = new ArrayList<String>();
        char[] newLine = line.trim().toCharArray();
        String allowed = ",'cCxX";

        for (int i = 0; i < newLine.length; i++) {
            if (Character.isLetterOrDigit(newLine[i]) || allowed.contains(String.valueOf(newLine[i]))) {
                String token = "", newAllowed = new String(allowed);
                int j = i;

                if (j + 1 < newLine.length && newLine[j] == 'C' && newLine[j + 1] == '\'') {
                    newAllowed += Character.toLowerCase(' ');
                    newAllowed += Character.toUpperCase(' ');
                }

                while (j < newLine.length && (Character.isLetterOrDigit(newLine[j]) || newAllowed.contains(String.valueOf(newLine[j])))) {
                    token += newLine[j];
                    j++;
                }
                
                tokens.add(token);
                i = j - 1;
            }
        }

        return tokens;
    }
}
