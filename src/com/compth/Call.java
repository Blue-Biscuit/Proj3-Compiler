package com.compth;

import java.util.ArrayList;

import lowlevel.Attribute;
import lowlevel.BasicBlock;
import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class Call extends Expression{
    private final String id;
    private final ArrayList<Expression> args;

    public Call(String id, ArrayList<Expression> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public String toString() {
        return toString(0, 3);
    }
    
    @Override
    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, spcInTab);
        sb.append(id);
        sb.append("(\n");
        for (Expression e : args) {
            sb.append(e.toString(tabs + 1, spcInTab));
        }
        Common.addTabs(sb, tabs, spcInTab);
        sb.append(")\n");

        return sb.toString();
    }

    @Override
    public void genLLCode(Program p, Function f) {
        BasicBlock currBlock;           // The current basic block.
        final Operation callOp;         // The operation for the function call.
        final Operation retMoveOp;      // The operation to move the return reg into the final register.

        currBlock = f.getCurrBlock();
        
        // 1. Generate the code for the parameters, and create an op for each to pass it into the function.
        for (Expression arg : args) {
            final Operation passOp;     // The operation to pass the param into the function. 

            arg.genLLCode(p, f);

            passOp = new Operation(Operation.OperationType.PASS, currBlock);
            passOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, arg.getRegister()));
            currBlock.appendOper(passOp);
        }

        // 2. Create the call instruction, and annotate it with parameter size.
        callOp = new Operation(Operation.OperationType.CALL, currBlock);
        callOp.addAttribute(new Attribute("size", Integer.toString(this.args.size())));
        callOp.setSrcOperand(0, new Operand(Operand.OperandType.STRING, this.id));
        currBlock.appendOper(callOp);

        // 3. Split the basic block after the jump.
        currBlock = new BasicBlock(f);
        f.appendToCurrentBlock(currBlock);
        f.setCurrBlock(currBlock);

        // 4. Move return reg into this register.
        this.register = f.getNewRegNum();
        retMoveOp = new Operation(Operation.OperationType.ASSIGN, currBlock);
        retMoveOp.setSrcOperand(0, new Operand(Operand.OperandType.MACRO, "RetReg"));
        retMoveOp.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, this.register));
    }
}
