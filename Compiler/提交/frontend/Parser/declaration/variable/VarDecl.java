package frontend.Parser.declaration.variable;

import frontend.Parser.declaration.Btype;
import frontend.Parser.declaration.DeclAll;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class VarDecl implements DeclAll
{
    private String type = "<VarDecl>";
    private Btype btype;

    private ArrayList<Token> commas;

    private Token semicn;
    private ArrayList<VarDef> varDefs;

    public VarDecl(ArrayList<Token> commas, Token semicn, Btype btype, ArrayList<VarDef> varDefs)
    {
        this.commas = commas;
        this.semicn = semicn;
        this.btype = btype;
        this.varDefs = varDefs;
    }

    public VarDecl ParseVarDecl(Tokenlist tokenlist, boolean flag)
    {
        Btype btype1 = new Btype(null);
        Btype btype2 = btype1.ParseBtype(tokenlist);
        ArrayList<VarDef> varDefs1 = new ArrayList<>();
        VarDef varDef = new VarDef(null, null, null, null, null, null);
        varDefs1.add(varDef.ParseVarDef(tokenlist, btype2, flag));
        //tokenlist.ReadNext();
        //boolean flag = false;
        ArrayList<Token> commas = new ArrayList<>();
        while (tokenlist.getToken().getType() == Token.Type.COMMA)
        {
            commas.add(tokenlist.getToken());
            tokenlist.ReadNext();
            varDefs1.add(varDef.ParseVarDef(tokenlist, btype2, flag));
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
                System.out.println("VarDecl + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new VarDecl(commas, semicn, btype2, varDefs1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(btype.toString());
        int n = varDefs.size();
        sb.append(varDefs.get(0).toString());
        for (int i = 1; i < n; i++)
        {
            sb.append(commas.get(i - 1).myToString());
            sb.append(varDefs.get(i).toString());
        }
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    public Btype getBtype()
    {
        return btype;
    }

    public ArrayList<VarDef> getVarDefs()
    {
        return varDefs;
    }
}
