package frontend.Parser.declaration.variable.variableinitval;

import frontend.Parser.expression.Exp;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class VarIVArray implements VarAllInitVal
{
    private ArrayList<Exp> exps;

    private Token lbrace;
    private Token rbrace;
    private ArrayList<Token> commas;

    public VarIVArray(Token lbrace, Token rbrace, ArrayList<Token> commas, ArrayList<Exp> exps)
    {
        this.lbrace = lbrace;
        this.rbrace = rbrace;
        this.commas = commas;
        this.exps = exps;
    }

    public ArrayList<Exp> getExps()
    {
        return exps;
    }

    public VarIVArray ParseVarIVArray(Tokenlist tokenlist, boolean flag)
    {
        Token lbrace = null;
        Token rbrace = null;
        ArrayList<Token> commas = new ArrayList<>();
        lbrace = tokenlist.getToken();
        ArrayList<Exp> exps1 = new ArrayList<>();
        Exp exp = new Exp(null);
        tokenlist.ReadNext();
        exps1.add(exp.ParseExp(tokenlist, flag));
        //boolean flag = false;
        while (tokenlist.getToken().getType() == Token.Type.COMMA)
        {
            commas.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(exp.ParseExp(tokenlist, flag));
            //tokenlist.ReadNext();
            //flag = true;
        }
//        if (flag)
//        {
//            tokenlist.ReadForward();
//        }
        if (tokenlist.getToken().getType() != Token.Type.RBRACE)
        {
            System.out.println("VarIVArray + 缺少一个右大括号");
            tokenlist.ReadForward();
        }
        else
        {
            rbrace = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new VarIVArray(lbrace, rbrace, commas, exps1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lbrace.myToString());
        sb.append(exps.get(0).myToString());
        if (commas.size() > 0)
        {
            for (int i = 0; i < commas.size(); i++)
            {
                sb.append(commas.get(i).myToString());
                sb.append(exps.get(i + 1).myToString());
            }
        }
        sb.append(rbrace.myToString());
        return sb.toString();
    }
}
