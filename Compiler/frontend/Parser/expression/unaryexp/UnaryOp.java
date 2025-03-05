package frontend.Parser.expression.unaryexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class UnaryOp implements UnaryAll
{
    private String type = "<UnaryOp>";
    private Token op;
    private UnaryExp exp;

    private String valuetype = null;

    public UnaryOp(Token op, UnaryExp exp)
    {
        this.op = op;
        this.exp = exp;
    }


    public UnaryOp ParseUnaryOp(Tokenlist tokenlist, boolean flag)
    {
        Token op1 = tokenlist.getToken();
        tokenlist.ReadNext();
        UnaryExp exp2 = new UnaryExp(null);
        UnaryExp exp1 = exp2.ParserUnary(tokenlist, flag);
        UnaryOp unaryOp = new UnaryOp(op1, exp1);
        unaryOp.setValuetype(exp1.getValuetype());
        return unaryOp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(op.myToString());
        sb.append(type + "\n");
        sb.append(exp.toString());
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

    public UnaryExp getExp()
    {
        return exp;
    }

    public Token getOp()
    {
        return op;
    }

    public int calcExp(SymbolTable symbolTable)
    {
        if (op.getContent().equals("+"))
        {
            return exp.calcExp(symbolTable);
        }
        else if (op.getContent().equals("-"))
        {
            return (-1) * exp.calcExp(symbolTable);
        }
        return 1000000000;
    }
}
