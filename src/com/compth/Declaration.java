package com.compth;

import lowlevel.CodeItem;

public abstract class Declaration {
    protected final String id;
    protected final TokenType dataType;
    
    public Declaration(String id, TokenType dataType) {
        this.id = id;
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return toString(0, 3);
    }

    public String getID() {
        return this.id;
    }

    public abstract CodeItem genLLCode(Program p);

    public abstract String toString(int numTabs, int tabSpaces);
}
