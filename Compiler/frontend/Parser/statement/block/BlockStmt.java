package frontend.Parser.statement.block;

import frontend.Parser.declaration.Decl;
import frontend.Parser.statement.Stmt;
import frontend.lexer.Tokenlist;

public class BlockStmt implements BlockItem
{
    private String type = "<BlockItem>";
    private Stmt stmt;

    public BlockStmt(Stmt stmt)
    {
        this.stmt = stmt;
    }

    public BlockStmt ParseBlockStmt(Tokenlist tokenlist, boolean flag)
    {
        Stmt stmt1 = new Stmt(null);
        Stmt stmt2 = stmt1.ParseStmt(tokenlist, flag);
        return new BlockStmt(stmt2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(stmt.toString());
        //sb.append(type + "\n");
        return sb.toString();
    }

    public Stmt getStmt()
    {
        return stmt;
    }
}
