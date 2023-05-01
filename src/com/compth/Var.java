package com.compth;

import java.util.HashMap;

import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class Var extends Expression {
    private final String id;

    public Var(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return toString(0, 3);
    }

    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, spcInTab);
        sb.append(id);
        sb.append('\n');

        return sb.toString();
    }

    // TODO: add stack stuff for parameters
    @Override
    public void genLLCode(Program p, Function f) {
        // 1. If this is a global variable...
        if (p.isGlobal(this.id)) {
            final Operation loadOP;     // The load operation to get the global.

            // a. Get a new register for the result of the load.
            this.register = f.getNewRegNum();

            // b. Create the global variable load.
            loadOP = new Operation(Operation.OperationType.LOAD_I, f.getCurrBlock());
            loadOP.setSrcOperand(0, new Operand(Operand.OperandType.STRING, this.id));
            loadOP.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, this.register));

            // c. Add the register to the current basic block.
            f.getCurrBlock().appendOper(loadOP);
        }

        // 2. Otherwise, if it is in the symbol table...
        else {
            final HashMap<String, Integer> sym;      // The symbol table.

            // a. Load the symbol table
            sym = f.getTable();

            // b. Load the register from the symbol table, if it exists.
            if (sym.containsKey(this.id)) {
                this.register = sym.get(this.id);
            }
            else {
                throw new UndefinedVariableException("Variable '" + this.id + "' was undefined.");
            }
        }
    }
}
