package com.compth;
import java.util.ArrayList;
import lowlevel.CodeItem;

public class Program {
    private final ArrayList<Declaration> declarations;

    public Program(ArrayList<Declaration> decls) {
        this.declarations = decls;
    }

    /**
     * Generates a linked-list of compiled, low-level code.
     * @return The linked-list.
     */
    public CodeItem genLLCode() {
        CodeItem result;        // The resultant linked-list of low-level code.
        CodeItem last;          // The last link on the linked-list.

        // 1. If the linked-list is empty, return an empty linked-list (null).
        if (declarations.size() == 0) {
            return null;
        }

        // 2. Initialize the linked-list with the first declaration.
        result = declarations.get(0).genLLCode(this);
        last = result;

        // 3. Generate the linked-list based upon the declarations ArrayList.
        for (int i = 1; i < declarations.size(); i++) {
            CodeItem next;      // The LL code for declarations[i]

            next = declarations.get(i).genLLCode(this);
            last.setNextItem(next);
            last = next;
        }

        return result;
    }

    public ArrayList<Declaration> getDeclarations() {
        return this.declarations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Program {\n");
        for (Declaration d : declarations) {
            sb.append(d.toString(1, 3) + "\n");
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Given a string, return whether there is a global variable with that identifier.
     * @param id The identifier.
     * @return True if a global of this name exists.
     */
    public boolean isGlobal(String id) {
        for (Declaration d : declarations) {
            if (d instanceof VariableDeclaration) {
                VariableDeclaration vd = (VariableDeclaration)d;

                if (vd.getID().equals(id)) {
                    return true;
                }
            }
        }

        return false;
    }
}
