package com.compth;

import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class BinaryOperation extends Expression {
    private Expression lhs;
    private Expression rhs;
    TokenType op;

    public BinaryOperation(TokenType op, Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public String toString() {
        return toString(0, 3);
    }

    @Override
    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        // Open bracket
        Common.addTabs(sb, tabs, spcInTab);
        sb.append(this.op);
        sb.append(" {\n");

        // LHS
        sb.append(lhs.toString(tabs + 1, spcInTab));

        // RHS
        sb.append(rhs.toString(tabs + 1, spcInTab));

        // Close bracket
        Common.addTabs(sb, tabs, spcInTab);
        sb.append("}\n");

        return sb.toString();
    }

    @Deprecated
    @Override
    public void annotateWithRegister(Function f) {
        lhs.annotateWithRegister(f);
        rhs.annotateWithRegister(f);
        super.annotateWithRegister(f);
    }

    @Override
    public void genLLCode(Program p, Function f) {
        final Operation op;                     // The resultant binary operand which has been generated.
        final Operation.OperationType opt;      // The type of operation to be generated.
        
        // 1. Gen code for the left and right child, in a post-order fashion.
        lhs.genLLCode(p, f);
        rhs.genLLCode(p, f);

        // 2. Create the operation based upon the binary operator.
        switch (this.op) {
            case ASSIGN:
                opt = Operation.OperationType.ASSIGN;
                op = new Operation(opt, f.getCurrBlock());
                op.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, rhs.getRegister()));
                op.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, lhs.getRegister()));
                break;
            case DIV:
                opt = Operation.OperationType.DIV_I;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case EQ:
                opt = Operation.OperationType.EQUAL;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case GT:
                opt = Operation.OperationType.GT;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case GTE:
                opt = Operation.OperationType.GTE;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case LT:
                opt = Operation.OperationType.LT;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case LTE:
                opt = Operation.OperationType.LTE;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case MINUS:
                opt = Operation.OperationType.SUB_I;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case NEQ:
                opt = Operation.OperationType.NOT_EQUAL;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case TIMES:
                opt = Operation.OperationType.MUL_I;
                op = setupThreeOperandBinOp(opt, f);
                break;
            case PLUS:
                opt = Operation.OperationType.ADD_I;
                op = setupThreeOperandBinOp(opt, f);
                break;
            default:
                throw new IllegalBinaryOperatorException("Illegal binary operator: " + this.op);
        }

        f.getCurrBlock().appendOper(op);
    }

    private Operation setupThreeOperandBinOp(Operation.OperationType opt, Function f) {
        final Operation result;     // The result of the function.
        
        this.register = f.getNewRegNum();
        result = new Operation(opt, f.getCurrBlock());
        result.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, lhs.getRegister()));
        result.setSrcOperand(1, new Operand(Operand.OperandType.REGISTER, rhs.getRegister()));
        result.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, this.register));

        return result;
    }
}
