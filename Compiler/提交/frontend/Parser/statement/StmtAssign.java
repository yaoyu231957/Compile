package frontend.Parser.statement;

import frontend.Parser.expression.Exp;
import frontend.Parser.expression.primaryexp.LVal;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;

public class StmtAssign implements StmtAll
{
    private String type = "<Stmt>";
    private LVal lVal;
    private Token assign;
    private Token semicn;
    private Exp exp;

    public StmtAssign(LVal lVal, Token assign, Token semicn, Exp exp)
    {
        this.lVal = lVal;
        this.assign = assign;
        this.semicn = semicn;
        this.exp = exp;
    }

    public StmtAssign ParseAssign(Tokenlist tokenlist, boolean flag)
    {
        LVal lVal1 = new LVal(null, null, null, null);
        LVal lVal2 = lVal1.ParseLVal(tokenlist, flag);
        Token assign = tokenlist.getToken();
        tokenlist.ReadNext();
        //tokenlist.ReadNext();
        if (flag)
        {
            handleHError(lVal2, assign);
        }
        Exp exp1 = new Exp(null);
        Exp exp2 = exp1.ParseExp(tokenlist, flag);
        //tokenlist.ReadNext();
        Token semicn = null;
        if (tokenlist.getToken().getType() != Token.Type.SEMICN)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                System.out.println("StmtAssign + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new StmtAssign(lVal2, assign, semicn, exp2);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal.toString());
        sb.append(assign.myToString());
        sb.append(exp.myToString());
        if (semicn != null)
        {
            sb.append(semicn);
        }
        return sb.toString();
    }

    private void handleHError(LVal lval, Token token)
    {
        Symbol symbol = lval.getSymbol();
        if (symbol == null || symbol.getType() == null)
        {
            return;
        }
        if (symbol.getType() == Symbol.Type.ConstChar || symbol.getType() == Symbol.Type.ConstInt || symbol.getType() == Symbol.Type.ConstIntArray || symbol.getType() == Symbol.Type.ConstCharArray)
        {
            Errors errors = new Errors(token.getLine(), 'h');
            Errorlist.getErrorlist().AddError(errors);
        }
    }

    public LVal getlVal()
    {
        return lVal;
    }

    public Exp getExp()
    {
        return exp;
    }
}
