package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class RelExp
{
    private String type = "<RelExp>";
    private ArrayList<AddExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    public RelExp(ArrayList<AddExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public RelExp ParseRelExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<AddExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        AddExp addExp = new AddExp(null, null);
        RelExp relExp = new RelExp(null, null);
        exps1.add(addExp.ParseAddExp(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.LSS || tokenlist.getToken().getType() == Token.Type.GRE || tokenlist.getToken().getType() == Token.Type.LEQ || tokenlist.getToken().getType() == Token.Type.GEQ)
        {
            ops1.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(addExp.ParseAddExp(tokenlist, flag));
        }
        return new RelExp(exps1, ops1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<RelExp>\n");
            sb.append(ops.get(i).myToString());
            sb.append(exps.get(i + 1).toString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    public ArrayList<AddExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

}
