package frontend.Parser.statement.block;

import frontend.Parser.declaration.Decl;
import frontend.lexer.Tokenlist;

public class BlockDecl implements BlockItem
{
    private String type = "<BlockItem>";
    private Decl decl;

    public BlockDecl(Decl decl)
    {
        this.decl = decl;
    }

    public BlockDecl ParseBlockDecl(Tokenlist tokenlist, boolean flag)
    {
        Decl decl1 = new Decl(null, null);
        Decl decl2 = decl1.ParseDecl(tokenlist, flag);
        return new BlockDecl(decl2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(decl.toString());
        //sb.append(type + "\n");
        return sb.toString();
    }

    public Decl getDecl()
    {
        return decl;
    }
}
