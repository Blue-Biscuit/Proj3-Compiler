package com.compth;

public interface Parser {
    public Program parse();
    public String printAST();
    public String printAST(Program p);
}

