package com.compth;

import lowlevel.BasicBlock;
import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class ReturnStatement extends Statement {
    public ReturnStatement(Expression expression) {
        super(expression);
    }

    public String toString(int tabs, int spacesInTab) {
        StringBuilder sb = new StringBuilder();

        // Print open bracket
        Common.addTabs(sb, tabs, spacesInTab);
        sb.append("ReturnStatement {\n");

        // Print expression, if non-null
        if (expression != null) {
            sb.append(expression.toString(tabs + 1, spacesInTab));
        }

        // Print close bracket
        Common.addTabs(sb, tabs, spacesInTab);
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public void genLLCode(Program p, Function f) {
        final Operation jumpToReturnOp;     // The operation to unconditionally jump to the return block.
        final BasicBlock returnBlock;       // The return block for the function.
        final BasicBlock currBlock;         // The current block at the start of this function's operation.
        final BasicBlock newBlock;          // The new block which this function creates after the jump.

        // 1. Generate the expression and move its result into the return register.
        currBlock = f.getCurrBlock();
        if (expression != null) {
            final Operation retRegMoveOp;       // The operation to move the expression result into the return reg.
            final int expResultRegNum;          // The register number where the expression stored its result.

            // a. Generate the sub-expression's code.
            expression.genLLCode(p, f);
            expResultRegNum = expression.getRegister();

            // b. Create the operation.
            retRegMoveOp = new Operation(Operation.OperationType.ASSIGN, currBlock);
            retRegMoveOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, expResultRegNum));
            retRegMoveOp.setDestOperand(0, new Operand(Operand.OperandType.MACRO, "RetReg"));

            // c. Add it to the function.
            currBlock.appendOper(retRegMoveOp);
        }

        // 2. Create the unconditional jump to the return block.
        returnBlock = f.getReturnBlock();
        jumpToReturnOp = new Operation(Operation.OperationType.JMP, currBlock);
        jumpToReturnOp.setSrcOperand(0, new Operand(Operand.OperandType.BLOCK, returnBlock.getBlockNum()));
        currBlock.appendOper(jumpToReturnOp);

        // 3. Add a new basic block and update the function accordingly.
        newBlock = new BasicBlock(f);
        f.appendToCurrentBlock(newBlock);
        f.setCurrBlock(newBlock);
    }

    
}
