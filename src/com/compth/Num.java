package com.compth;

import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class Num extends Expression {
    private final int val;

    public Num(int val) {
        this.val = val;
    }

    @Override
    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, spcInTab);
        sb.append(val);
        sb.append('\n');

        return sb.toString();
    }

    @Override
    public void genLLCode(Program p, Function f) {
        final Operation op;     // The MOVE operation used to create the constant.

        // 1. Create the operation to put the constant in register.
        op = new Operation(Operation.OperationType.ASSIGN, f.getCurrBlock());
        this.register = f.getNewRegNum();
        op.setSrcOperand(0, new Operand(Operand.OperandType.INTEGER, this.val));
        op.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, this.register));

        // 2. Add the operation to the basic block.
        f.getCurrBlock().appendOper(op);
    }

}
