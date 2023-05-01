package com.compth;

import lowlevel.CodeItem;
import lowlevel.Data;

public class VariableDeclaration extends Declaration {
    private final Integer index;

    public VariableDeclaration(String id, int index) {
        super(id, TokenType.INT);
        this.index = index;
    }

    public VariableDeclaration(String id) {
        super(id, TokenType.INT);
        this.index = null;
    }

    public String toString() {
        return toString(0, 3);
    }

    @Override
    public String toString(int tabs, int tabSpaces) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, tabSpaces);
        sb.append("Variable Declaration {\n");

        Common.addTabs(sb, tabs + 1, tabSpaces);
        sb.append("int ");
        sb.append(this.id);
        
        if (index != null) {
            sb.append("[");
            sb.append(index);
            sb.append("]\n");
        }

        Common.addTabs(sb, tabs, tabSpaces);
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public CodeItem genLLCode(Program p) {
        CodeItem result;        // The resultant CodeItem.

        // 1. Create the Data based upon loaded values.
        result = new Data(Data.TYPE_INT, this.id);

        return result;
    }
}
