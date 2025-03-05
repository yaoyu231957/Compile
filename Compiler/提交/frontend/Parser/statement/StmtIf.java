package frontend.Parser.statement;

import frontend.Parser.expression.CondExp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class StmtIf implements StmtAll
{
    private String type = "<Stmt>";
    private Token iftk;
    private Token lparent;
    private Token rparent;
    private Token elsetk;
    private CondExp condExp;
    private Stmt stmt1;
    private Stmt stmt2;

    public StmtIf(Token iftk, Token lparent, Token rparent, Token elsetk, CondExp condExp, Stmt stmt1, Stmt stmt2)
    {
        this.iftk = iftk;
        this.lparent = lparent;
        this.rparent = rparent;
        this.elsetk = elsetk;
        this.condExp = condExp;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    public CondExp getCondExp()
    {
        return condExp;
    }

    public Stmt getStmt1()
    {
        return stmt1;
    }

    public Stmt getStmt2()
    {
        return stmt2;
    }

    public StmtIf ParseStmtIf(Tokenlist tokenlist, boolean flag)
    {
        Token iftk = tokenlist.getToken();
        tokenlist.ReadNext();
        Token lparent = null;
        Token rparent = null;
        Token elsetk = null;
        if (tokenlist.getToken().getType() != Token.Type.LPARENT)
        {
            System.out.println("StmtIf + 缺少一个左小括号");
            tokenlist.ReadForward();
        }
        else
        {
            lparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        CondExp condExp1 = new CondExp(null);
        CondExp condExp2 = condExp1.ParseCondExp(tokenlist, flag);
        //tokenlist.ReadNext();
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
                System.out.println("StmtIf + 缺少一个右小括号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            rparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        Stmt stmt1 = new Stmt(null);
        Stmt stmt2 = stmt1.ParseStmt(tokenlist, flag);
        //tokenlist.ReadNext();
        Stmt stmt3 = null;
        if (tokenlist.getToken().getType() == Token.Type.ELSETK)
        {
            elsetk = tokenlist.getToken();
            tokenlist.ReadNext();
            stmt3 = stmt1.ParseStmt(tokenlist, flag);
            //tokenlist.ReadNext();
        }
        return new StmtIf(iftk, lparent, rparent, elsetk, condExp2, stmt2, stmt3);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(iftk.myToString());
        sb.append(lparent.myToString());
        sb.append(condExp.toString());
        sb.append(rparent.myToString());
        sb.append(stmt1.toString());
        if (elsetk != null)
        {
            sb.append(elsetk.myToString());
            sb.append(stmt2.toString());
        }
        return sb.toString();
    }

}
