package frontend.Parser.expression.primaryexp;

import frontend.Parser.expression.Exp;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;
import middle.semantic.Symbol;
import middle.semantic.SymbolConst;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class PrimaryExp
{
    private String type = "<PrimaryExp>";
    private PrimaryAllExp exp;

    private String valuetype;

    public PrimaryExp(PrimaryAllExp exp)
    {
        this.exp = exp;
    }

    public PrimaryExp ParsePrimaryExp(Tokenlist tokenlist, boolean flag)
    {
        Token token = tokenlist.getToken();
        PrimaryAllExp exp1;
        if (token.getType() == Token.Type.LPARENT)
        {
            ExpPrim expPrim = new ExpPrim(null, null, null);
            //tokenlist.ReadNext();
            exp1 = expPrim.ParseExpPrim(tokenlist, flag);
            //tokenlist.ReadNext();
            //tokenlist.ReadNext();
        }
        else if (token.getType() == Token.Type.IDENFR)
        {
            LVal lVal = new LVal(null, null, null, null);
            exp1 = lVal.ParseLVal(tokenlist, flag);
            //tokenlist.ReadNext();
        }
        else if (token.getType() == Token.Type.INTCON)
        {
            Number number = new Number(null);
            exp1 = number.ParseIntConst(tokenlist);
            tokenlist.ReadNext();
        }
        else
        {
            Character character = new Character(null);
            exp1 = character.ParseCharConst(tokenlist);
            tokenlist.ReadNext();
        }
        PrimaryExp primaryExp = new PrimaryExp(exp1);
        primaryExp.setValuetype(exp1.getValuetype());
        return primaryExp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.toString());
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

    public PrimaryAllExp getExp()
    {
        return exp;
    }

    public int calcExp(SymbolTable symbolTable)
    {
        if (exp instanceof Number)
        {
            Number number = (Number) exp;
            return Integer.parseInt(number.getIntconst().getContent());
        }
        else if (exp instanceof Character)
        {
            Character character = (Character) exp;
            String cha = character.getCharconst().getContent();
            if (cha.length() == 3)
            {
                int c = cha.charAt(1);
                return c;
            }
            else
            {
                cha = cha.substring(1, cha.length() - 1);
                switch (cha)
                {
                    case "\\t":
                        return 9;
                    case "\\a":
                        return 7;
                    case "\\b":
                        return 8;

                    case "\\n":
                        return 10;

                    case "\\v":
                        return 11;

                    case "\\f":
                        return 12;

                    case "\\\"":
                        return 34;

                    case "\\'":
                        return 39;

                    case "\\\\":
                        return 92;

                    case "\\0":
                        return 0;

                }
            }
            return character.getCharconst().getContent().charAt(1);
        }
        else if (exp instanceof ExpPrim)
        {
            ExpPrim expPrim = (ExpPrim) exp;
            return ((ExpPrim) exp).getExp().getExp().calcExp(symbolTable);
        }
        else
        {
            LVal lVal = (LVal) exp;
            Token ident = lVal.getIdent();
            Exp exp1 = lVal.getExp();
            Symbol symbol = symbolTable.getSymbol(ident.getContent());
            if (exp1 == null)
            {
                return Integer.parseInt(symbol.getValue());
            }
            else
            {
                int num = exp1.getExp().calcExp(symbolTable);
                SymbolConst symbolConst = (SymbolConst) symbol;
                ArrayList<String> values = symbolConst.getConstarray();
                return Integer.parseInt(values.get(num));
            }
        }
    }
}
