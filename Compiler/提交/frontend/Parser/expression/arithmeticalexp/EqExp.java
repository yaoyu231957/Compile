package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class EqExp
{
    private String type = "<EqExp>";
    private ArrayList<RelExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    public EqExp(ArrayList<RelExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public EqExp ParseEqExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<RelExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        RelExp relExp = new RelExp(null, null);
        exps1.add(relExp.ParseRelExp(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.EQL || tokenlist.getToken().getType() == Token.Type.NEQ)
        {
            ops1.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(relExp.ParseRelExp(tokenlist, flag));
        }
        return new EqExp(exps1, ops1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<EqExp>\n");
            sb.append(ops.get(i).myToString());
            sb.append(exps.get(i + 1).toString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    public ArrayList<RelExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

}
