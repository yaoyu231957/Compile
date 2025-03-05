package frontend.Parser.declaration.constant;

import frontend.Parser.declaration.Btype;
import frontend.Parser.declaration.DeclAll;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class ConstDecl implements DeclAll
{
    private String type = "<ConstDecl>";

    private Token consttk;
    private ArrayList<Token> commas;

    private Token semicn;
    private Btype btype;
    private ArrayList<ConstDef> constDefs;

    public ConstDecl(Token consttk, ArrayList<Token> commas, Token semicn, Btype btype, ArrayList<ConstDef> constDefs)
    {
        this.consttk = consttk;
        this.commas = commas;
        this.semicn = semicn;
        this.btype = btype;
        this.constDefs = constDefs;
    }

    public ConstDecl ParseConstDecl(Tokenlist tokenlist, boolean flag)
    {
        Token token = tokenlist.getToken();
        Token consttk1 = null;
        if (token.getType() != Token.Type.CONSTTK)
        {
            //System.out.println("缺少Const");
            tokenlist.ReadForward();
        }
        else
        {
            consttk1 = token;
        }
        tokenlist.ReadNext();
        Btype btype1 = new Btype(null);
        Btype btype2 = btype1.ParseBtype(tokenlist);
        //tokenlist.ReadNext();
        ArrayList<ConstDef> constDefs1 = new ArrayList<>();
        ConstDef constDef = new ConstDef(null, null, null, null, null, null);
        constDefs1.add(constDef.ParseConstDef(tokenlist, btype2, flag));
        //tokenlist.ReadNext();
        //boolean flag = false;
        ArrayList<Token> commas = new ArrayList<>();
        while (tokenlist.getToken().getType() == Token.Type.COMMA)
        {
            commas.add(tokenlist.getToken());
            tokenlist.ReadNext();
            constDefs1.add(constDef.ParseConstDef(tokenlist, btype2, flag));
            //tokenlist.ReadNext();
            //flag = true;
        }
//        if (flag)
//        {
//            tokenlist.ReadForward();
//        }
        Token semicn = null;
        if (tokenlist.getToken().getType() != Token.Type.SEMICN)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                System.out.println("ConstDecl + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new ConstDecl(consttk1, commas, semicn, btype2, constDefs1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(consttk.myToString());
        sb.append(btype.toString());
        int n = constDefs.size();
        sb.append(constDefs.get(0).toString());
        for (int i = 1; i < n; i++)
        {
            sb.append(commas.get(i - 1).myToString());
            sb.append(constDefs.get(i).toString());
        }
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    public ArrayList<ConstDef> getConstDefs()
    {
        return constDefs;
    }

    public Btype getBtype()
    {
        return btype;
    }
}
