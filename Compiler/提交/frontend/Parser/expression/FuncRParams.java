package frontend.Parser.expression;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class FuncRParams implements AllExp
{
    private String type = "<FuncRParams>";
    private ArrayList<Token> commas = new ArrayList<>();
    private ArrayList<Exp> exps = new ArrayList<>();

    public FuncRParams(ArrayList<Token> commas, ArrayList<Exp> exps)
    {
        this.commas = commas;
        this.exps = exps;
    }

    public FuncRParams ParseFuncRParams(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<Token> commas1 = new ArrayList<>();
        ArrayList<Exp> exps1 = new ArrayList<>();
        Exp exp = new Exp(null);
        tokenlist.ReadNext();
        if (IsExp(tokenlist) && tokenlist.getToken().getLine() == tokenlist.getForwardToken().getLine())
        {
            exps1.add(exp.ParseExp(tokenlist, flag));
        }
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType().equals(Token.Type.COMMA))
        {
            commas1.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(exp.ParseExp(tokenlist, flag));
            //tokenlist.ReadNext();
        }
        FuncRParams funcRParams = new FuncRParams(commas1, exps1);
        return funcRParams;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (!exps.isEmpty())
        {
            sb.append(exps.get(0).myToString());
            for (int i = 0; i < commas.size(); i++)
            {
                sb.append(commas.get(i).myToString());
                sb.append(exps.get(i + 1).myToString());
            }
            sb.append(type + "\n");
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

    public ArrayList<Exp> getExps()
    {
        return exps;
    }
}
