package frontend.Parser.declaration.constant.consinitval;

import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class ConIVArray implements ConstAllInitVal
{
    private Token lbrace;
    private Token rbrace;
    private ArrayList<Token> commas;
    private ArrayList<ConstExp> constExps;

    public ConIVArray(Token lbrace, Token rbrace, ArrayList<Token> commas, ArrayList<ConstExp> constExps)
    {
        this.lbrace = lbrace;
        this.rbrace = rbrace;
        this.commas = commas;
        this.constExps = constExps;
    }

    public ConIVArray ParseConIVArray(Tokenlist tokenlist, boolean flag)
    {
        Token lbrace = null;
        Token rbrace = null;
        ArrayList<Token> commas = new ArrayList<>();
        lbrace = tokenlist.getToken();
        ArrayList<ConstExp> constExps1 = new ArrayList<>();
        ConstExp constExp = new ConstExp(null);
        tokenlist.ReadNext();
        if (tokenlist.getToken().getType() != Token.Type.RBRACE)
        {
            constExps1.add(constExp.ParseConstExp(tokenlist, flag));
        }
        //boolean flag = false;
        while (tokenlist.getToken().getType() == Token.Type.COMMA)
        {
            commas.add(tokenlist.getToken());
            tokenlist.ReadNext();
            constExps1.add(constExp.ParseConstExp(tokenlist, flag));
            //tokenlist.ReadNext();
            //flag = flase;
        }
//        if (flag)
//        {
//            tokenlist.ReadForward();
//        }
        if (tokenlist.getToken().getType() != Token.Type.RBRACE)
        {
            System.out.println("ConIVArray + 缺少一个右大括号");
            tokenlist.ReadForward();
        }
        else
        {
            rbrace = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new ConIVArray(lbrace, rbrace, commas, constExps1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(lbrace.myToString());
        if (!constExps.isEmpty())
        {
            sb.append(constExps.get(0).myToString());
        }
        if (commas.size() > 0)
        {
            for (int i = 0; i < commas.size(); i++)
            {
                sb.append(commas.get(i).myToString());
                sb.append(constExps.get(i + 1).myToString());
            }
        }
        sb.append(rbrace.myToString());
        return sb.toString();
    }

    public ArrayList<ConstExp> getConstExps()
    {
        return constExps;
    }
}
