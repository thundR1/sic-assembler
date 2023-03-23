package structures;

import analyzers.Constants;

public class OPTable extends AbstractTable2D<String, String> {
    public OPTable() {
        super();
    }

    public boolean validateEntry(Instruction instruction) {
        if (instruction.operation == null || instruction.operation.isEmpty()) return false;

        boolean bad = false;

        bad |= Constants.isIn(Constants.DIRECTIVES, instruction.operation.toUpperCase());

        return !bad;
    }
}
