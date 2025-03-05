package frontend.Parser.statement;

import frontend.Parser.expression.primaryexp.LVal;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;

public class StmtGet implements StmtAll
{
    private String type = "<Stmt>";
    private LVal lVal;
    private Token assign;
    private Token gettk;

    private Token lparent;
    private Token rparent;
    private Token semicn;

    public StmtGet(LVal lVal, Token assign, Token gettk, Token lparent, Token rparent, Token semicn)
    {
        this.gettk = gettk;
        this.assign = assign;
        this.lVal = lVal;
        this.lparent = lparent;
        this.rparent = rparent;
        this.semicn = semicn;
    }

    public StmtGet ParseStmtGet(Tokenlist tokenlist, boolean flag)
    {
        LVal lVal1 = new LVal(null, null, null, null);
        LVal lVal2 = lVal1.ParseLVal(tokenlist, flag);
        Token assign = tokenlist.getToken();
        tokenlist.ReadNext();
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        Token lparent = null;
        Token rparent = null;
        Token semicn = null;
        if (flag)
        {
            handleHError(lVal2, assign);
        }
        if (tokenlist.getToken().getType() != Token.Type.LPARENT)
        {
            System.out.println("StmtGet + 缺少一个左小括号");
            tokenlist.ReadForward();
        }
        else
        {
            lparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
                System.out.println("StmtGet + 缺少一个右小括号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            rparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        if (tokenlist.getToken().getType() != Token.Type.SEMICN)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                System.out.println("StmtGet + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new StmtGet(lVal2, assign, token1, lparent, rparent, semicn);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal.toString());
        sb.append(assign.myToString());
        sb.append(gettk.myToString());
        sb.append(lparent.myToString());
        sb.append(rparent.myToString());
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        return sb.toString();
    }

    private void handleHError(LVal lval, Token assign)
    {
        Symbol symbol = lval.getSymbol();
        if (symbol.getType() == null)
        {
            return;
        }
        if (symbol.getType() == Symbol.Type.ConstChar || symbol.getType() == Symbol.Type.ConstInt || symbol.getType() == Symbol.Type.ConstIntArray || symbol.getType() == Symbol.Type.ConstCharArray)
        {
            Errors errors = new Errors(assign.getLine(), 'h');
            Errorlist.getErrorlist().AddError(errors);
        }
    }

    public LVal getlVal()
    {
        return lVal;
    }

    public Token getGettk()
    {
        return gettk;
    }
}
