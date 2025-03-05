package frontend.Parser.statement;

import frontend.Parser.expression.Exp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class StmtPrintf implements StmtAll
{
    private String type = "<Stmt>";
    private Token printftk;
    private Token lparent;
    private Token rparent;
    private Token semicn;
    private Token stringconst;
    private ArrayList<Token> commas;
    private ArrayList<Exp> exps;

    public StmtPrintf(Token printftk, Token lparent, Token rparent, Token semicn, Token stringconst, ArrayList<Token> commas, ArrayList<Exp> exps)
    {
        this.printftk = printftk;
        this.lparent = lparent;
        this.rparent = rparent;
        this.semicn = semicn;
        this.commas = commas;
        this.stringconst = stringconst;
        this.exps = exps;
    }

    public StmtPrintf ParseStmtPrintf(Tokenlist tokenlist, boolean flag)
    {
        Token printfk = tokenlist.getToken();
        tokenlist.ReadNext();
        Token lparent = null;
        Token rparent = null;
        Token semicn = null;
        ArrayList<Token> commas = new ArrayList<>();
        if (tokenlist.getToken().getType() != Token.Type.LPARENT)
        {
            System.out.println("StmtPrintf + 缺少一个左小括号");
            tokenlist.ReadForward();
        }
        else
        {
            lparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        Token stringconst = tokenlist.getToken();
        ArrayList<Exp> exps1 = new ArrayList<>();
        tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.COMMA)
        {
            commas.add(tokenlist.getToken());
            tokenlist.ReadNext();
            Exp exp = new Exp(null);
            exps1.add(exp.ParseExp(tokenlist, flag));
            //tokenlist.ReadNext();
        }
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
                System.out.println("StmtPrintf + 缺少一个右小括号");
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
                System.out.println("StmtPrintf + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        handleLError(stringconst, exps1.size());
        return new StmtPrintf(printfk, lparent, rparent, semicn, stringconst, commas, exps1);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(printftk.myToString());
        sb.append(lparent.myToString());
        sb.append(stringconst.myToString());
        for (int i = 0; i < commas.size(); i++)
        {
            sb.append(commas.get(i).myToString());
            sb.append(exps.get(i).myToString());
        }
        sb.append(rparent.myToString());
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        return sb.toString();
    }

    private void handleLError(Token stringconst, int expnum)
    {
        String string = stringconst.getContent();
        int num = 0;
        for (int i = 0; i < string.length(); i++)
        {
            if (string.charAt(i) == '%' && (string.charAt(i + 1) == 'd' || string.charAt(i + 1) == 'c'))
            {
                num++;
            }
        }
        if (num != expnum)
        {
            Errors errors = new Errors(stringconst.getLine(), 'l');
            Errorlist.getErrorlist().AddError(errors);
        }
    }

    public Token getStringconst()
    {
        return stringconst;
    }

    public ArrayList<Exp> getExps()
    {
        return exps;
    }
}
