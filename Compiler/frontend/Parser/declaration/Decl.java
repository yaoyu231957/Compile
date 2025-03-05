package frontend.Parser.declaration;

import frontend.Parser.declaration.constant.ConstDecl;
import frontend.Parser.declaration.variable.VarDecl;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class Decl
{
    private String type = "<Decl>";
    private DeclAll decl;

    private String valuetype;

    public Decl(DeclAll decl, String valuetype)
    {
        this.decl = decl;
        this.valuetype = valuetype;
    }

    public Decl ParseDecl(Tokenlist tokenlist, boolean flag)
    {
        DeclAll declAll;
        String valuetype;
        if (tokenlist.getToken().getType() == Token.Type.CONSTTK)
        {
            valuetype = "const";
            ConstDecl constDecl = new ConstDecl(null, null, null, null, null);
            declAll = constDecl.ParseConstDecl(tokenlist, flag);
        }
        else
        {
            valuetype = "var";
            VarDecl varDecl = new VarDecl(null, null, null, null);
            declAll = varDecl.ParseVarDecl(tokenlist, flag);
        }
        return new Decl(declAll, valuetype);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(decl.toString());
        //sb.append(type + "\n");
        return sb.toString();
    }

    public String getValuetype()
    {
        return valuetype;
    }

    public DeclAll getDecl()
    {
        return decl;
    }
}
