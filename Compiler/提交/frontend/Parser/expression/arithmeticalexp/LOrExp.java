package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class LOrExp
{
    private String type = "<LOrExp>";
    private ArrayList<LAndExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    public LOrExp(ArrayList<LAndExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public ArrayList<LAndExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

    public LOrExp ParseLOrExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<LAndExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        LAndExp lAndExp = new LAndExp(null, null);
        exps1.add(lAndExp.ParseLAndExp(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.OR)
        {
            if (!tokenlist.getToken().getError())
            {
                ops1.add(tokenlist.getToken());
            }
            tokenlist.ReadNext();
            exps1.add(lAndExp.ParseLAndExp(tokenlist, flag));
        }
        return new LOrExp(exps1, ops1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<LOrExp>\n");
            sb.append(ops.get(i).myToString());
            sb.append(exps.get(i + 1).toString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }
}
