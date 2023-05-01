package com.compth;

import lowlevel.Function;

public class ExpressionStatement extends Statement {

    public ExpressionStatement(Expression expr) {
        super(expr);
    }

    @Override 
    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, spcInTab);
        sb.append("Expression Statement {\n");

        if (this.expression != null) {
            sb.append(this.expression.toString(tabs + 1, spcInTab));
        }

        Common.addTabs(sb, tabs, spcInTab);
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public void genLLCode(Program p, Function f) {
        // If the expression is non-null, generate the expression.
        if (this.expression != null) {
            this.expression.genLLCode(p, f);
        }
    }
}