package frontend.Parser.expression.unaryexp;

import frontend.Parser.expression.AllExp;
import frontend.Parser.expression.arithmeticalexp.ArithExpAll;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class UnaryExp implements AllExp, ArithExpAll
{
    private String type = "<UnaryExp>";
    private UnaryAll unaryexp;

    private String valuetype;

    public UnaryExp(UnaryAll unaryexp)
    {
        this.unaryexp = unaryexp;
    }


    public UnaryExp ParserUnary(Tokenlist tokenlist, boolean flag)
    {
        Token token = tokenlist.getToken();
        UnaryAll unaryexp1;
        if (token.getType() == Token.Type.IDENFR && tokenlist.getNextToken().getType() == Token.Type.LPARENT)
        {
            UnaryFunc unaryFunc = new UnaryFunc(null, null, null, null);
            unaryexp1 = unaryFunc.ParseUnaryfunc(tokenlist, flag);
        }
        else if (token.getType() == Token.Type.PLUS || token.getType() == Token.Type.MINU || token.getType() == Token.Type.NOT)
        {
            UnaryOp unaryOp = new UnaryOp(null, null);
            unaryexp1 = unaryOp.ParseUnaryOp(tokenlist, flag);
        }
        else
        {
            UnaryPriExp unaryPriExp = new UnaryPriExp(null);
            unaryexp1 = unaryPriExp.ParseUnaryPriExp(tokenlist, flag);
        }
        UnaryExp unaryExp = new UnaryExp(unaryexp1);
        if (flag)
        {
            unaryExp.setValuetype(unaryexp1.getValuetype());
        }
        return unaryExp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(unaryexp.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public void setValuetype(String valuetype)
    {
        this.valuetype = valuetype;
    }

    public String getValuetype()
    {
        return valuetype;
    }

    public UnaryAll getUnaryexp()
    {
        return unaryexp;
    }

    public int calcExp(SymbolTable symbolTable)
    {
        if (unaryexp instanceof UnaryPriExp)
        {
            return ((UnaryPriExp) unaryexp).calcExp(symbolTable);
        }
        else if (unaryexp instanceof UnaryOp)
        {
            return ((UnaryOp) unaryexp).calcExp(symbolTable);
        }
        return 1000000000;
    }
}
