package frontend.Parser.declaration.variable.variableinitval;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class VarInitVal
{
    private String type = "<InitVal>";
    private VarAllInitVal varInitVal;

    public VarInitVal(VarAllInitVal varInitVal)
    {
        this.varInitVal = varInitVal;
    }

    public VarInitVal ParseVarInitVal(Tokenlist tokenlist, boolean flag)
    {
        VarAllInitVal varAllInitVal;
        if (tokenlist.getToken().getType() == Token.Type.LBRACE)
        {
            VarIVArray varIVArray = new VarIVArray(null, null, null, null);
            varAllInitVal = varIVArray.ParseVarIVArray(tokenlist, flag);
        }
        else if (tokenlist.getToken().getType() == Token.Type.STRCON)
        {
            VarIVStrConst varIVStrConst = new VarIVStrConst(null);
            varAllInitVal = varIVStrConst.ParseVarIVStrConst(tokenlist);
        }
        else
        {
            VarIVExp varIVExp = new VarIVExp(null);
            varAllInitVal = varIVExp.ParseVarIVExp(tokenlist, flag);
        }
        return new VarInitVal(varAllInitVal);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(varInitVal.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public VarAllInitVal getVarInitVal()
    {
        return varInitVal;
    }
}
