package structures;

public class SymbolTable extends AbstractTable2D<String, String> {
    public SymbolTable() {
        super();
    }

    public boolean validateEntry(Instruction instruction) {
        if (instruction.label == null || instruction.label.isEmpty()) return false;
        return !(instruction.operation.toUpperCase().equals("START"));
    }
}
