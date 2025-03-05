package frontend.Parser.statement;

import frontend.Parser.expression.Exp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class StmtExp implements StmtAll
{
    private String type = "<Stmt>";
    private Exp exp;

    private Token semicn;

    public StmtExp(Exp exp, Token semicn)
    {
        this.exp = exp;
        this.semicn = semicn;
    }

    public StmtExp ParseStmtExp(Tokenlist tokenlist, boolean flag)
    {
        Exp exp1 = null;
        Token semicn = null;
        if (tokenlist.getToken().getType() != Token.Type.SEMICN)
        {
            Exp exp2 = new Exp(null);
            exp1 = exp2.ParseExp(tokenlist, flag);
            //tokenlist.ReadNext();
            if (tokenlist.getToken().getType() != Token.Type.SEMICN)
            {
                if (flag)
                {
                    Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                    System.out.println("StmtExp + 缺少一个分号");
                }
            }
            else
            {
                semicn = tokenlist.getToken();
                tokenlist.ReadNext();
            }
        }
        else
        {
            semicn = tokenlist.getToken();
            tokenlist.ReadNext();
        }
        return new StmtExp(exp1, semicn);

    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (exp != null)
        {
            sb.append(exp.myToString());
        }
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        return sb.toString();
    }

    public Exp getExp()
    {
        return exp;
    }
}
