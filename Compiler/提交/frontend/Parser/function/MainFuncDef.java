package frontend.Parser.function;

import frontend.Parser.statement.block.StmtBlock;
import frontend.Parser.statement.stmtreturn.CheckReturn;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolFunc;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class MainFuncDef
{
    private String type = "<MainFuncDef>";
    private Token inttk;
    private Token maintk;
    private Token lparent;
    private Token rparent;
    private StmtBlock stmtBlock;

    public MainFuncDef(Token inttk, Token maintk, Token lparent, Token rparent, StmtBlock stmtBlock)
    {
        this.inttk = inttk;
        this.maintk = maintk;
        this.lparent = lparent;
        this.rparent = rparent;
        this.stmtBlock = stmtBlock;
    }

    public MainFuncDef ParseMainFuncDef(Tokenlist tokenlist)
    {
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        Token token2 = tokenlist.getToken();
        tokenlist.ReadNext();
        Token lparent = null;
        Token rparent = null;
        addFuncSymbol(token2);
        if (tokenlist.getToken().getType() != Token.Type.LPARENT)
        {
            System.out.println("MainFuncDef + 缺少一个左小括号");
            tokenlist.ReadForward();
        }
        else
        {
            lparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
            System.out.println("MainFuncDef + 缺少一个右小括号");
            tokenlist.ReadForward();
        }
        else
        {
            rparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        CheckReturn checkReturn = new CheckReturn(1, 0);
        ArrayList<CheckReturn> checkReturns = checkReturn.CheckReturn(tokenlist);
        StmtBlock stmtBlock1 = new StmtBlock(null, null, null);
        StmtBlock stmtBlock2 = stmtBlock1.ParseStmtBlock(tokenlist, true, true);
        handleGError(checkReturns);
        //SymbolTable.UpLevel();

        //SymbolTable.changeAllSymbolTable(SymbolTable.getAllsymboltable().getParent());

        return new MainFuncDef(token1, token2, lparent, rparent, stmtBlock2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(inttk.myToString());
        sb.append(maintk.myToString());
        if (lparent != null)
        {
            sb.append(lparent.myToString());
        }
        if (rparent != null)
        {
            sb.append(rparent.myToString());
        }
        sb.append(stmtBlock.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    private void addFuncSymbol(Token maintk)
    {
        /* 生成新符号 */
        SymbolFunc symbolFunc = new SymbolFunc(maintk.getLine(), SymbolTable.getAllsymboltable().getLevel(), "", Symbol.Type.IntFunc);
        /* 检查b类错误 */
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.checkHasSymbol(symbolFunc))
        {
            Errors errors = new Errors(symbolFunc.getLine(), 'b');
            Errorlist.getErrorlist().AddError(errors);
        }
        /*
        SymbolTable symbolTable1 = new SymbolTable(SymbolTable.getAllsymboltable());
        SymbolTable.getAllsymboltable().addChildren(symbolTable1);
        SymbolTable.changeAllSymbolTable(symbolTable1);

         */
    }

    private void handleGError(ArrayList<CheckReturn> checkReturns)
    {

        /* g error */

        if (checkReturns.get(checkReturns.size() - 1).getType() == 3)
        {
            Errors errors = new Errors(checkReturns.get(checkReturns.size() - 1).getLine(), 'g');
            Errorlist.getErrorlist().AddError(errors);
        }

    }

    public StmtBlock getStmtBlock()
    {
        return stmtBlock;
    }
}
