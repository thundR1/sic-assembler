/**
 * @file This File Contains 'Builder' Class Which
 *       is Responsible for Implementing Methods
 *       That Builds The Assembler Output.
*/
package analyzers;

/** Internal Imports */
import structures.OPTable;
import structures.SymbolTable;
import structures.Instruction;
import structures.ObjectTable;
import memory.RegistersProvider;
import reducers.NumericalReducer;
import data.handlers.FileHandler;

/** External Imports */
import java.util.BitSet;
import java.util.ArrayList;

/**
 * @implNote It Build LocationCounter in 4 HexDigits,
 *           OPTable (i.e. OP_Code) in 2 HexDigits,
 *           ObjectCode in 6 HedDigits.
*/
public class Builder {
    private ArrayList<Instruction> sourceFile;

    /**
     * @param sourceFile sourceCode File
    */
    public Builder(FileHandler sourceFile) {
        this.sourceFile = LexicalAnalyzer.parse(sourceFile);

        if (this.sourceFile.isEmpty())
            throw new IllegalArgumentException("SourceFile is Empty!");
    }

    /**
     * @return locationCounter - String[]
    */
    public String[] buildLocationCounter() {
        int[] locationCounter = new int[this.sourceFile.size()];
        Instruction first = this.sourceFile.get(0);

        locationCounter[0] = first.operation.toUpperCase().equals("START") ? NumericalReducer.reduceToDec(first.operand) : 0;

        for (int i = 1; i < locationCounter.length; i++)
            locationCounter[i] = locationCounter[i - 1] + this.sourceFile.get(i - 1).getSize();
        
        String[] hexLocationCounter = new String[locationCounter.length];

        for (int i = 0; i < locationCounter.length; i++)
            hexLocationCounter[i] = NumericalReducer.reduceToHex(locationCounter[i], 4);

        return hexLocationCounter;
    }

    /**
     * @return SymbolTable - SymbolTable
    */
    public SymbolTable buildSymbolTable() {
        SymbolTable symbolTable = new SymbolTable();

        String[] locationCounter = this.buildLocationCounter();
        
        for (int i = 0; i < this.sourceFile.size(); i++) {
            Instruction curr = this.sourceFile.get(i);

            if (!symbolTable.validateEntry(curr)) continue;

            symbolTable.add(curr.label, locationCounter[curr.position - 1]);
        }

        return symbolTable;
    }

    /**
     * @return OPTable - OPTable
    */
    public OPTable buildOPTable() {
        OPTable opTable = new OPTable();

        for (int i = 0; i < this.sourceFile.size(); i++) {
            String currOperation = this.sourceFile.get(i).operation;

            if (!opTable.validateEntry(this.sourceFile.get(i))) continue;

            String opCode = Constants.OP_CODE.get(currOperation);

            opTable.add(currOperation, NumericalReducer.placeIn(2, opCode));
        }

        return opTable;
    }

    /**
     * @param provider
     * 
     * @return ObjectCode Table - ObjectTable
    */
    public ObjectTable buildObjectCode(RegistersProvider provider) {
        ObjectTable objectTable = new ObjectTable();

        String[] locationCounter = this.buildLocationCounter();

        for (int i = 0; i < this.sourceFile.size(); i++) {
            Instruction curr = this.sourceFile.get(i);

            if (curr.operation.toUpperCase().equals("START")) {
                objectTable.add(null);
                continue;
            }

            if (curr.operation.toUpperCase().equals("END")) {
                objectTable.add(null);
                continue;
            }

            if (Constants.isIn(Constants.DIRECTIVES, curr.operation.toUpperCase())) {
                objectTable.add(NumericalReducer.dirToObj(curr.operation.toUpperCase(), curr.operand));
                continue;
            }

            String opCode = Constants.OP_CODE.get(curr.operation);
            boolean indexed = curr.getIndexed();

            String newOperand = curr.operand;

            if (curr.operand != null && curr.operand.contains(",")) {
                int start = 0;

                while (start < curr.operand.length() && curr.operand.charAt(start) != ',')
                    start++;
                
                newOperand = curr.operand.substring(0, start);
            }

            String targetAddress = getCounterOf(newOperand, locationCounter);

            if (indexed) {
                int decimalTAX = NumericalReducer.reduceToDec(targetAddress) + provider.X();
                targetAddress = NumericalReducer.reduceToHex(decimalTAX, 4);
            }

            BitSet binObjectCode = new BitSet(24);

            String binaryOpCode = NumericalReducer.fromHexToBinary(opCode, 8);
            String binaryIndexed = indexed ? "1" : "0";
            String binaryTA = NumericalReducer.fromHexToBinary(targetAddress, 15);

            String finalBinInstruction = binaryOpCode.concat(binaryIndexed).concat(binaryTA);

            assert finalBinInstruction.length() == 24;

            for (int j = 0; j < finalBinInstruction.length(); j++) {
                binObjectCode.set(j, finalBinInstruction.charAt(j) == '0' ? false : true);
            }

            objectTable.add(NumericalReducer.bitSetToHex(binObjectCode));
        }

        return objectTable;
    }

    public ArrayList<String> buildCompressedOC() {
        Instruction first = this.sourceFile.get(0);
        Instruction last = this.sourceFile.get(this.sourceFile.size() - 1);

        if (!first.operation.toUpperCase().equals("START")) {
            throw new IllegalArgumentException("Start of The Program Should Contain START directive.");
        }

        if (!last.operation.toUpperCase().equals("END")) {
            throw new IllegalArgumentException("End of The Program Should Contain END directive.");
        }

        String[] locationCounter = this.buildLocationCounter();
        ObjectTable objectTable = this.buildObjectCode(RegistersProvider.getProvider());

        ArrayList<String> compressed = new ArrayList<>();

        for (int i = 0; i < this.sourceFile.size(); i++) {
            if (i == 0) {
                compressed.add(compressLine(this.sourceFile.get(i), locationCounter, objectTable, 0, 0));
                continue;
            }

            if (i == this.sourceFile.size() - 1) {
                int begEnd = this.sourceFile.size() - 1;
                compressed.add(compressLine(this.sourceFile.get(i), locationCounter, objectTable, begEnd, begEnd));
                continue;
            }

            int begin = i, end = this.getStopIndex(begin);
            
            compressed.add(compressLine(this.sourceFile.get(i), locationCounter, objectTable, begin, end));
            i = end;
        }

        return compressed;
    }

    /**
     * @param operand
     * @param locationCounter
     * 
     * @return LocationCounter of Operand Label - String
     */
    private String getCounterOf(String operand, String[] locationCounter) {
        if (operand == null || operand.isEmpty()) return "0".repeat(4);

        for (int i = 0; i < this.sourceFile.size(); i++) {
            String currLabel = this.sourceFile.get(i).label;

            if (currLabel != null && currLabel.equals(operand)) {
                return locationCounter[i];
            }
        }

        throw new IllegalArgumentException("Operand Doesn't Exist in as a Label in SourceCode.");
    }

    private int getStopIndex(int begin) {
        if (begin == 0) return 0;

        int end = begin, bytes = 0;
        
        while ((end < this.sourceFile.size() - 1) && (bytes + this.evaluate(this.sourceFile.get(end))) <= 30) {
            bytes += this.evaluate(this.sourceFile.get(end));
            end++;
        }

        return end - 1;
    }

    private String compressLine(Instruction currInst, String[] lc, ObjectTable objectTable, int begin, int end) {
        String compressed = "";

        if (begin == 0 && begin == end) {
            compressed += 'H';
            String progName = currInst.label, startAddress = currInst.operand;

            if (progName == null || progName.length() > 6)
                throw new IllegalArgumentException("Invalid Program Name!");
            
            while (progName.length() < 6)
                progName = progName.concat(" ");
            
            compressed += "^".concat(progName);
            compressed += "^".concat(NumericalReducer.placeIn(6, startAddress));
            
            int progSize = NumericalReducer.reduceToDec(lc[lc.length - 1]);
            progSize -= NumericalReducer.reduceToDec(lc[0]);

            String hexProgSize = NumericalReducer.reduceToHex(progSize, 6);

            compressed += "^".concat(hexProgSize);

            return compressed;
        }

        if (begin == this.sourceFile.size() - 1 && begin == end) {
            compressed += "E^".concat(NumericalReducer.placeIn(6, lc[1]));
            return compressed;
        }

        compressed += 'T';
        compressed += "^".concat(NumericalReducer.placeIn(6, lc[currInst.position - 1]));

        int numOfBytes = 0;

        for (int i = begin; i <= end; i++)
            numOfBytes += this.evaluate(this.sourceFile.get(i));

        compressed += "^".concat(NumericalReducer.reduceToHex(numOfBytes, 2));

        for (int i = begin; i <= end; i++) {
            String currObjCode = objectTable.getContent().get(i);

            if (currObjCode != null)
                compressed += "^".concat(NumericalReducer.placeIn(6, currObjCode));
        }

        return compressed;
    }

    private int evaluate(Instruction instruction) {
        if (instruction == null) return 0;

        String op = instruction.operation.toUpperCase();

        if (op.equals("BYTE") || op.equals("WORD")) {
            String currOperand = instruction.operand.toUpperCase();
            char identifier = currOperand.charAt(0);

            int denom = identifier == 'X' ? 2 : 1;
            int size = (currOperand.length() - 3 + denom - 1) / denom; // ceil

            return size;
        }

        if (Constants.isIn(Constants.DIRECTIVES, op)) {
            return 0;
        }

        return 3;
    }
}
