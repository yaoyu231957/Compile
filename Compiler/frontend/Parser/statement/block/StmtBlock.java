package frontend.Parser.statement.block;

import frontend.Parser.statement.StmtAll;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class StmtBlock implements StmtAll
{
    private String type = "<Block>";
    private Token lbarce;
    private Token rbarce;
    ArrayList<BlockItem> blockItems;

    public StmtBlock(ArrayList<BlockItem> blockItems, Token lbarce, Token rbarce)
    {
        this.blockItems = blockItems;
        this.lbarce = lbarce;
        this.rbarce = rbarce;
    }

    public StmtBlock ParseStmtBlock(Tokenlist tokenlist, boolean flag1, boolean flag2)
    {
        ArrayList<BlockItem> blockItems1 = new ArrayList<>();
        Token lbarce = null;
        Token rbarce = null;
        lbarce = tokenlist.getToken();
        tokenlist.ReadNext();
        if (flag1 && flag2)
        {
            SymbolTable.UpLevel();
            SymbolTable symbolTable1 = new SymbolTable(SymbolTable.getAllsymboltable());
            SymbolTable.getAllsymboltable().addChildren(symbolTable1);
            if (SymbolTable.getAllsymboltable().getCircle() != 0)
            {
                symbolTable1.GetInCircle();
            }
            SymbolTable.changeAllSymbolTable(symbolTable1);
        }
        while (!tokenlist.IsOver() && tokenlist.getToken().getType() != Token.Type.RBRACE)
        {
            if (tokenlist.getToken().getType() == Token.Type.CONSTTK || tokenlist.getToken().getType() == Token.Type.INTTK || tokenlist.getToken().getType() == Token.Type.CHARTK)
            {
                BlockDecl blockDecl = new BlockDecl(null);
                blockItems1.add(blockDecl.ParseBlockDecl(tokenlist, flag2));
            }
            else
            {
                BlockStmt blockStmt = new BlockStmt(null);
                blockItems1.add(blockStmt.ParseBlockStmt(tokenlist, flag2));
            }
        }

        if (!tokenlist.IsOver())
        {
            rbarce = tokenlist.getToken();
        }
        tokenlist.ReadNext();


        /*
        rbarce = tokenlist.getToken();
        tokenlist.ReadNext();

         */
        if (flag1 && flag2)
        {
            SymbolTable.changeAllSymbolTable(SymbolTable.getAllsymboltable().getParent());
        }
        return new StmtBlock(blockItems1, lbarce, rbarce);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lbarce.myToString());
        for (BlockItem blockItem : blockItems)
        {
            sb.append(blockItem.toString());
        }
        sb.append(rbarce.myToString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public ArrayList<BlockItem> getBlockItems()
    {
        return blockItems;
    }
}
