package frontend.Parser.expression.primaryexp;

import frontend.Parser.expression.Exp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class ExpPrim implements PrimaryAllExp
{
    private Exp exp;

    private Token lparent;
    private Token rparent;

    private String valuetype;

    public ExpPrim(Exp exp, Token lparent, Token rparent)
    {
        this.exp = exp;
        this.lparent = lparent;
        this.rparent = rparent;
    }

    public ExpPrim ParseExpPrim(Tokenlist tokenlist, boolean flag)
    {
        Token lparent = null;
        Token rparent = null;
        lparent = tokenlist.getToken();
        tokenlist.ReadNext();
        Exp exp1 = new Exp(null);
        Exp exp2 = exp1.ParseExp(tokenlist, flag);
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
                System.out.println("PrimaryExp + 缺少一个右小括号");
            }
            tokenlist.ReadForward();
        }
        rparent = tokenlist.getToken();
        tokenlist.ReadNext();
        ExpPrim expPrim = new ExpPrim(exp2, lparent, rparent);
        expPrim.setValuetype(exp2.getValuetype());
        return expPrim;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lparent.myToString());
        sb.append(exp.myToString());
        sb.append(rparent.myToString());
        return sb.toString();
    }

    public Exp getExp()
    {
        return exp;
    }

    public void setValuetype(String valuetype)
    {
        this.valuetype = valuetype;
    }

    public String getValuetype()
    {
        return valuetype;
    }
}
