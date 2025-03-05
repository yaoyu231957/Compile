package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class LAndExp
{
    private String type = "<LAndExp>";
    private ArrayList<EqExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    public LAndExp(ArrayList<EqExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public ArrayList<EqExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

    public LAndExp ParseLAndExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<EqExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        EqExp eqExp = new EqExp(null, null);
        exps1.add(eqExp.ParseEqExp(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.AND)
        {
            if (!tokenlist.getToken().getError())
            {
                ops1.add(tokenlist.getToken());
            }
            tokenlist.ReadNext();
            exps1.add(eqExp.ParseEqExp(tokenlist, flag));
        }
        return new LAndExp(exps1, ops1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<LAndExp>\n");
            sb.append(ops.get(i).myToString());
            sb.append(exps.get(i + 1).toString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }
}
