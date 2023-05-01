package com.compth;

import lowlevel.Function;

public abstract class Expression {
    protected int register;       // The register which this expression will store its result into.

    public abstract String toString(int tabs, int spcInTab);

    /**
     * Determines the register which this expression will write to, based upon its current
     * functional context.
     * @param f The function within which this expression is.
     */
    public void annotateWithRegister(Function f) {
        this.register = f.getNewRegNum();
        
    }

    /**
     * Gets the register which this expression will place its result inside of.
     * @return The register.
     */
    public int getRegister() {
        return this.register;
    }

    public abstract void genLLCode(Program p, Function f);
}
