package analyzers;

import structures.OPTable;
import structures.ObjectTable;
import structures.SymbolTable;
import memory.RegistersProvider;
import data.handlers.FileHandler;

import java.util.ArrayList;

public class Assembler {
    private final RegistersProvider provider;
    private final Builder builder;

    public Assembler(FileHandler sourceCode) {
        this.provider = RegistersProvider.getProvider();
        this.builder = new Builder(sourceCode);
    }

    public String[] getLocationCounter() {
        return this.builder.buildLocationCounter();
    }

    public SymbolTable getSymbolTable() {
        return this.builder.buildSymbolTable();
    }

    public OPTable getOPTable() {
        return this.builder.buildOPTable();
    }

    public ObjectTable getObjectTable() {
        return this.builder.buildObjectCode(this.provider);
    }

    public ArrayList<String> getCompressedOC() {
        return this.builder.buildCompressedOC();
    }
}
