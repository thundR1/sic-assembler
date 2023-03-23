package data.tabulation;

import java.util.ArrayList;

import structures.Instruction;
import structures.ObjectTable;
import data.handlers.FileHandler;
import analyzers.Assembler;
import analyzers.LexicalAnalyzer;
import structures.AbstractTable2D;

public class Table {
    private final FileHandler sourceFile;
    private final String outputDirectory;

    public Table(FileHandler file, String outputDirectory) {
        this.sourceFile = file.copy();
        this.outputDirectory = outputDirectory;
    }

    public void tableLocationCounter(String[] locationCounter) {
        FileHandler outputFile = new FileHandler(this.outputDirectory + "/locationCounter.txt");
        outputFile.clear();

        ArrayList<Instruction> lines = LexicalAnalyzer.parse(this.sourceFile);
        int offset = 1 - longestInst(lines) % 2;

        for (int i = 0; i < locationCounter.length; i++) {
            Instruction curr = lines.get(i);

            String newLine = locationCounter[i], sep = " ".repeat(4);

            int diffLabel = curr.label == null ? 1 + offset : Math.abs(curr.label.length() - longestLabel(lines));
            int diffOperation = Math.abs(curr.operation.length() - longestOperation(lines));

            newLine = newLine.concat(sep + (curr.label == null ? sep : curr.label))
                             .concat(" ".repeat(4 + diffLabel) + curr.operation)
                             .concat(" ".repeat(4 + diffOperation) + (curr.operand == null ? "" : curr.operand));

            outputFile.append(newLine);
        }
    }

    public <T extends AbstractTable2D<String, String>> void table2DTable(T table) {
        FileHandler outputFile = new FileHandler(this.outputDirectory + String.format("/%s.txt", table.getClass().getSimpleName()));
        outputFile.clear();

        int longestFirst = Integer.MIN_VALUE;

        for (var p : table.getContent())
            longestFirst = Math.max(longestFirst, p.first.length());

        for (var p : table.getContent()) {
            int diffLabel = Math.abs(p.first.length() - longestFirst);
            outputFile.append(p.first + " ".repeat(4 + diffLabel) + p.second);
        }
    }

    public void tableObjectTable(ObjectTable objectTable) {
        FileHandler outputFile = new FileHandler(this.outputDirectory + String.format("/%s.txt", objectTable.getClass().getSimpleName()));
        outputFile.clear();

        ArrayList<Instruction> lines = LexicalAnalyzer.parse(this.sourceFile);
        int offset = 1 - longestInst(lines) % 2;

        for (int i = 0; i < lines.size(); i++) {
            Instruction curr = lines.get(i);
            
            String sep = " ".repeat(4);

            int diffLabel = curr.label == null ? 1 + offset : Math.abs(curr.label.length() - longestLabel(lines));
            int diffOperation = Math.abs(curr.operation.length() - longestOperation(lines));
            int diffOperand = Math.abs((curr.operand == null ? 0 : curr.operand.length()) - longestOperand(lines));

            String currLineObj = objectTable.getContent().get(i);
            currLineObj = currLineObj == null ? "" : currLineObj;

            String newLine = "".concat(curr.label == null ? sep : curr.label)
                               .concat(" ".repeat(4 + diffLabel) + curr.operation)
                               .concat(" ".repeat(4 + diffOperation) + (curr.operand == null ? "" : curr.operand))
                               .concat(" ".repeat(4 + diffOperand) + currLineObj);

            outputFile.append(newLine);
        }
    }

    public void tableCompressedOC(ArrayList<String> compressedOC) {
        FileHandler outputFile = new FileHandler(this.outputDirectory + "/objectProgram.txt");
        outputFile.clear();

        outputFile.append(compressedOC.get(0) + "\n");

        for (int i = 1; i < compressedOC.size() - 1; i++) {
            outputFile.append("\n" + compressedOC.get(i));
        }

        outputFile.append("\n" + compressedOC.get(compressedOC.size() - 1));
    }

    public void tableAll(Assembler asm) {
        this.tableLocationCounter(asm.getLocationCounter());
        this.table2DTable(asm.getSymbolTable());
        this.table2DTable(asm.getOPTable());
        this.tableObjectTable(asm.getObjectTable());
        this.tableCompressedOC(asm.getCompressedOC());
    }

    private int longestLabel(ArrayList<Instruction> lines) {
        int answer = Integer.MIN_VALUE;
        
        for (final Instruction instruction : lines)
            if (instruction.label != null)
                answer = Math.max(answer, instruction.label.length());

        return answer;
    }

    private int longestOperation(ArrayList<Instruction> lines) {
        int answer = Integer.MIN_VALUE;
        
        for (final Instruction instruction : lines)
            if (instruction.operation != null)
                answer = Math.max(answer, instruction.operation.length());

        return answer;
    }

    private int longestOperand(ArrayList<Instruction> lines) {
        int answer = Integer.MIN_VALUE;
        
        for (final Instruction instruction : lines)
            if (instruction.operand != null)
                answer = Math.max(answer, instruction.operand.length());

        return answer;
    }

    private int longestInst(ArrayList<Instruction> lines) {
        int answer = Integer.MIN_VALUE;
        
        for (final Instruction instruction : lines) {
            int curr1 = instruction.label == null ? 0 : instruction.label.length();
            int curr2 = instruction.operation == null ? 0 : instruction.operation.length();
            int curr3 = instruction.operand == null ? 0 : instruction.operand.length();

            answer = Math.max(answer, curr1 + curr2 + curr3);
        }

        return answer;
    }
}
