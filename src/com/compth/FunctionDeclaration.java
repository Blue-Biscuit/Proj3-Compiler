package com.compth;

import java.util.ArrayList;
import java.util.HashMap;

import lowlevel.BasicBlock;
import lowlevel.CodeItem;
import lowlevel.Data;
import lowlevel.FuncParam;
import lowlevel.Function;

public class FunctionDeclaration extends Declaration {
    private final ArrayList<Param> params;
    private final CompoundStatement stmts;

    public FunctionDeclaration(String id, TokenType dataType, ArrayList<Param> params, CompoundStatement statements) {
        super(id, dataType);
        this.params = params;
        this.stmts = statements;
    }

    @Override
    public String toString(int tabs, int spcInTab) {
        StringBuilder sb = new StringBuilder();

        Common.addTabs(sb, tabs, spcInTab);
        sb.append("Function Declaration {\n");

        Common.addTabs(sb, tabs + 1, spcInTab);
        sb.append("identifier: ");
        sb.append(this.id);
        sb.append('\n');

        Common.addTabs(sb, tabs + 1, spcInTab);
        sb.append("Params {\n");
        for (int i = 0; i < params.size(); i++) {
            sb.append(this.params.get(i).toString(tabs + 2, spcInTab));
        }
        Common.addTabs(sb, tabs + 1, spcInTab);
        sb.append("}\n");

        sb.append(stmts.toString(tabs + 1, spcInTab));

        Common.addTabs(sb, tabs, spcInTab);
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public CodeItem genLLCode(Program prog) {
        Function result;                               // The resultant function
        int returnType;                                // The return type of the function.
        String id;                                     // The name of the function.
        FuncParam params;                              // The linked-list of parameters.
        final HashMap<String, Integer> symTable;       // The symbol table.

        // 1. Get type and name data from the AST
        if (this.dataType == TokenType.INT) {
            returnType = Data.TYPE_INT;
        }
        else {
            returnType = Data.TYPE_VOID;
        }
        
        id = this.id;

        result = new Function(returnType, id);
        symTable = result.getTable();

        // 2. Setup function parameters
        params = null;

        if (this.params.size() > 0) {
            Param p;            // this.params[i]
            FuncParam last;     // The last param in the linked-list.

            // 1. Setup the first param
            p = this.params.get(0);
            params = new FuncParam(Data.TYPE_INT, p.getID());
            last = params;
            symTable.put(p.getID(), result.getNewRegNum());        // Add to symbol table

            // 2. Setup subsequent params
            for (int i = 1; i < this.params.size(); i++) {
                FuncParam curr;     // The FuncParam being created this iteration.

                // 1. Create the param.
                p = this.params.get(i);
                curr = new FuncParam(Data.TYPE_INT, p.getID());
                symTable.put(p.getID(), result.getNewRegNum());

                // 2. Hook up the param to the linked-list.
                last.setNextParam(curr);

                // 3. Iterate the loop.
                last = curr;
            }
        }

        result.setFirstParam(params);

        // 3. Setup the first Block and the one after it
        result.createBlock0();
        BasicBlock nextBlock = new BasicBlock(result);
        result.setCurrBlock(nextBlock);
        result.appendBlock(nextBlock);
        
        // 4. Generate the code for the compound-statement.
        this.stmts.genLLCode(prog, result);

        // 5. Append the exit block.
        result.appendBlock(result.getReturnBlock());

        return result;
    }
}
