package frontend.Parser.expression.arithmeticalexp;

import frontend.Parser.expression.unaryexp.UnaryExp;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class MulExp
{
    private String type = "<MulExp>";
    private ArrayList<UnaryExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    private String valuetype;

    public MulExp(ArrayList<UnaryExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public MulExp ParseMulExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<UnaryExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        UnaryExp unaryExp = new UnaryExp(null);
        exps1.add(unaryExp.ParserUnary(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.MULT || tokenlist.getToken().getType() == Token.Type.DIV || tokenlist.getToken().getType() == Token.Type.MOD)
        {
            ops1.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(unaryExp.ParserUnary(tokenlist, flag));
        }
        MulExp mulExp = new MulExp(exps1, ops1);
        if (flag)
        {
            mulExp.setValuetype(CalValueType(exps1));
        }
        return mulExp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<MulExp>\n");
            sb.append(ops.get(i).myToString());
            sb.append(exps.get(i + 1).toString());
        }
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

    public ArrayList<UnaryExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

    public String CalValueType(ArrayList<UnaryExp> exps)
    {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        for (UnaryExp unaryExp : exps)
        {
            if (unaryExp.getValuetype() == null)
            {
                continue;
            }
            if (unaryExp.getValuetype().equals("int"))
            {
                flag1 = true;
            }
            if (unaryExp.getValuetype().equals("char"))
            {
                flag2 = true;
            }
            if (unaryExp.getValuetype().equals("intarray"))
            {
                flag3 = true;
            }
            if (unaryExp.getValuetype().equals("chararray"))
            {
                flag4 = true;
            }
        }
        if (flag1 && !flag3 && !flag4)
        {
            return "int";
        }
        if (!flag1 && flag2 && !flag3 && !flag4)
        {
            return "char";
        }
        if ((flag1 || flag2) && (flag3 || flag4))
        {
            return "error";
        }
        if (flag3 && flag4)
        {
            return "error";
        }
        if (flag3 && !flag4)
        {
            return "intarray";
        }
        if (flag4 && !flag3)
        {
            return "chararray";
        }
        return "error";
    }

    public int calcExp(SymbolTable symbolTable)
    {
        int value;
        int i = 0;
        value = exps.get(i).calcExp(symbolTable);
        for (Token token : ops)
        {
            i++;
            if (token.getContent().equals("*"))
            {
                value *= exps.get(i).calcExp(symbolTable);
            }
            else if (token.getContent().equals("/"))
            {
                value /= exps.get(i).calcExp(symbolTable);
            }
            else if (token.getContent().equals("%"))
            {
                value %= exps.get(i).calcExp(symbolTable);
            }
        }
        return value;
    }
}
