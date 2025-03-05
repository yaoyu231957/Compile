package frontend.Parser.statement.stmtfor;

import frontend.Parser.expression.Exp;
import frontend.Parser.expression.primaryexp.LVal;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;

public class ForStmt
{
    private String type = "<ForStmt>";
    private LVal lVal;
    private Token assign;
    private Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp)
    {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    public LVal getlVal()
    {
        return lVal;
    }

    public Exp getExp()
    {
        return exp;
    }

    public ForStmt ParseForStmt(Tokenlist tokenlist, boolean flag)
    {
        LVal lVal1 = new LVal(null, null, null, null);
        LVal lVal2 = lVal1.ParseLVal(tokenlist, flag);
        Token assign = tokenlist.getToken();
        if (flag)
        {
            handleHError(lVal2, assign);
        }
        tokenlist.ReadNext();
        //tokenlist.ReadNext();
        Exp exp1 = new Exp(null);
        Exp exp2 = exp1.ParseExp(tokenlist, flag);
        //tokenlist.ReadNext();
        return new ForStmt(lVal2, assign, exp2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal.toString());
        sb.append(assign.myToString());
        sb.append(exp.myToString());
        sb.append(type + '\n');
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
}
