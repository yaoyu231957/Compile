package frontend.Parser;

import frontend.Parser.declaration.Decl;
import frontend.Parser.function.FuncDef;
import frontend.Parser.function.MainFuncDef;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class CompUnit
{
    private String type = "<CompUnit>";
    private ArrayList<Decl> decls;
    private ArrayList<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef)
    {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    public CompUnit ParseCompUnit(Tokenlist tokenlist)
    {
        ArrayList<Decl> decls1 = new ArrayList<>();
        ArrayList<FuncDef> funcDefs1 = new ArrayList<>();
        MainFuncDef mainFuncDef1 = null;
        Token first = tokenlist.getToken();
        Token second = tokenlist.getNextToken();
        while (!tokenlist.IsOver())
        {
            Token third = tokenlist.getThirdToken();
            if (third.getType() == Token.Type.LPARENT || second.getType() == Token.Type.MAINTK)
            {
                break;
            }
            if (first.getType() == Token.Type.CONSTTK || ((first.getType() == Token.Type.INTTK || first.getType() == Token.Type.CHARTK) && second.getType() == Token.Type.IDENFR))
            {
                Decl decl = new Decl(null, null);
                decls1.add(decl.ParseDecl(tokenlist, true));
            }
            if (tokenlist.getCur_pos() + 1 < tokenlist.getTokenlist().size())
            {
                first = tokenlist.getToken();
                second = tokenlist.getNextToken();
            }
            else
            {
                break;
            }
        }
        while (!tokenlist.IsOver())
        {
            if (second.getType() == Token.Type.MAINTK)
            {
                break;
            }
            FuncDef funcDef = new FuncDef(null, null, null, null, null, null, null, null);
            funcDefs1.add(funcDef.ParseFuncDef(tokenlist));
            if (tokenlist.getCur_pos() + 1 < tokenlist.getTokenlist().size())
            {
                first = tokenlist.getToken();
                second = tokenlist.getNextToken();
            }
            else
            {
                break;
            }
        }
        if (tokenlist.getCur_pos() + 1 < tokenlist.getTokenlist().size())
        {
            MainFuncDef mainFuncDef2 = new MainFuncDef(null, null, null, null, null);
            mainFuncDef1 = mainFuncDef2.ParseMainFuncDef(tokenlist);
        }
        return new CompUnit(decls1, funcDefs1, mainFuncDef1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Decl decl : decls)
        {
            sb.append(decl.toString());
        }
        for (FuncDef funcDef : funcDefs)
        {
            sb.append(funcDef.toString());
        }
        sb.append(mainFuncDef.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public ArrayList<Decl> getDecls()
    {
        return decls;
    }

    public ArrayList<FuncDef> getFuncDefs()
    {
        return funcDefs;
    }

    public MainFuncDef getMainFuncDef()
    {
        return mainFuncDef;
    }

}
