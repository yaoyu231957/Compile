package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class AddExp
{
    private String type = "<AddExp>";
    private ArrayList<MulExp> exps = new ArrayList<>();
    private ArrayList<Token> ops = new ArrayList<>();

    private String valuetype;

    public AddExp(ArrayList<MulExp> exps, ArrayList<Token> ops)
    {
        this.exps = exps;
        this.ops = ops;
    }

    public AddExp ParseAddExp(Tokenlist tokenlist, boolean flag)
    {
        ArrayList<MulExp> exps1 = new ArrayList<>();
        ArrayList<Token> ops1 = new ArrayList<>();
        MulExp mulExp = new MulExp(null, null);
        exps1.add(mulExp.ParseMulExp(tokenlist, flag));
        //tokenlist.ReadNext();
        while (tokenlist.getToken().getType() == Token.Type.PLUS || tokenlist.getToken().getType() == Token.Type.MINU)
        {
            ops1.add(tokenlist.getToken());
            tokenlist.ReadNext();
            exps1.add(mulExp.ParseMulExp(tokenlist, flag));
        }
        AddExp addExp = new AddExp(exps1, ops1);
        if (flag)
        {
            addExp.setValuetype(CalValueType(exps1));
        }
        return addExp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exps.get(0).toString());
        for (int i = 0; i < ops.size(); i++)
        {
            sb.append("<AddExp>\n");
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

    public ArrayList<MulExp> getExps()
    {
        return exps;
    }

    public ArrayList<Token> getOps()
    {
        return ops;
    }

    public String CalValueType(ArrayList<MulExp> exps)
    {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        for (MulExp mulExp : exps)
        {
            if (mulExp.getValuetype().equals("int"))
            {
                flag1 = true;
            }
            if (mulExp.getValuetype().equals("char"))
            {
                flag2 = true;
            }
            if (mulExp.getValuetype().equals("intarray"))
            {
                flag3 = true;
            }
            if (mulExp.getValuetype().equals("chararray"))
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
            if (token.getContent().equals("+"))
            {
                value += exps.get(i).calcExp(symbolTable);
            }
            else
            {
                value -= exps.get(i).calcExp(symbolTable);
            }
        }
        return value;
    }
}
