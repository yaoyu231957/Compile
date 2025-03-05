package frontend.Parser.statement.stmtreturn;

import frontend.Parser.expression.Exp;
import frontend.Parser.statement.StmtAll;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class StmtReturn implements StmtAll
{
    private String type = "<Stmt>";
    private Token returntk;
    private Exp exp;
    private Token semicn;

    public StmtReturn(Token returntk, Exp exp, Token semicn)
    {
        this.returntk = returntk;
        this.exp = exp;
        this.semicn = semicn;
    }

    public StmtReturn ParseStmtReturn(Tokenlist tokenlist, boolean flag)
    {
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        Exp exp1 = new Exp(null);
        Exp exp2 = null;
        Token semicn = null;
        if (IsExp(tokenlist) && tokenlist.getToken().getLine() == tokenlist.getForwardToken().getLine())
        {
            exp2 = exp1.ParseExp(tokenlist, flag);
            //tokenlist.ReadNext();
            if (tokenlist.getToken().getType() != Token.Type.SEMICN)
            {
                if (flag)
                {
                    Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                    System.out.println("StmtReturn + 缺少一个分号");
                }
                tokenlist.ReadForward();
            }
            else
            {
                semicn = tokenlist.getToken();
            }
            tokenlist.ReadNext();
        }
        else
        {
            if (tokenlist.getToken().getType() == Token.Type.SEMICN)
            {
                semicn = tokenlist.getToken();
                tokenlist.ReadNext();
            }
            else if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                System.out.println("StmtReturn + 缺少一个分号");
            }
        }
        return new StmtReturn(token1, exp2, semicn);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(returntk.myToString());
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

    public boolean IsExp(Tokenlist tokenlist)
    {
        Token token = tokenlist.getToken();
        if (token.getType() == Token.Type.LPARENT || token.getType() == Token.Type.IDENFR || token.getType() == Token.Type.INTCON || token.getType() == Token.Type.CHRCON || token.getType() == Token.Type.PLUS || token.getType() == Token.Type.MINU)
        {
            return true;
        }
        return false;
    }

    public Exp getExp()
    {
        return exp;
    }
}
