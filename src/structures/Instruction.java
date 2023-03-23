package structures;

import analyzers.Constants;
import reducers.NumericalReducer;
import java.util.Arrays;

public class Instruction {
    public String label, operation, operand;
    public int position = Constants.__DEFAULT_POSITION__;
    private int instructionSize = Constants.__DEFAULT_INSTRUCTION_SIZE__;
    public int length;
    private boolean indexed;

    public Instruction() {}

    public Instruction(String operation, String operand, int position) {
        this.operation = operation;
        this.operand = operand;
        this.position = position;
        this.length = (operation == null ? 0 : operation.length()) + (operand == null ? 0 : operand.length());
        this.indexed = NumericalReducer.isIndexed(operand);
        
        if (Constants.isIn(Constants.DIRECTIVES, operation)) {
            this.instructionSize = NumericalReducer.reduceToBytes(this.operand, this.operation);
        }
    }
    
    public Instruction(String label, String operation, String operand, int position) {
        this.label = label;
        this.operation = operation;
        this.operand = operand;
        this.position = position;
        this.length = (label == null ? 0 : label.length());
        this.length += (operation == null ? 0 : operation.length()) + (operand == null ? 0 : operand.length());
        this.indexed = NumericalReducer.isIndexed(operand);

        if (Constants.isIn(Constants.DIRECTIVES, operation)) {
            this.instructionSize = NumericalReducer.reduceToBytes(this.operand, this.operation);
        }
    }

    public int getSize() {
        return this.instructionSize;
    }

    public boolean getIndexed() {
        return this.indexed;
    }

    public String toString() {
        return Arrays.toString(new String[] {
            Integer.toString(this.position),
            this.label,
            this.operation,
            this.operand,
            Boolean.toString(this.indexed),
            Integer.toString(this.instructionSize),
        });
    }
}
