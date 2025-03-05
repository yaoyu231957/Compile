package frontend.Parser.expression.primaryexp;

import frontend.Parser.expression.Exp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolTable;

public class LVal implements PrimaryAllExp
{
    private String type = "<LVal>";

    private Token ident;

    private Token lbrack;
    private Token rbrack;
    private Exp exp;
    private Symbol symbol;
    private String valuetype;

    public LVal(Token ident, Token lbrack, Token rbrack, Exp exp)
    {
        this.ident = ident;
        this.lbrack = lbrack;
        this.rbrack = rbrack;
        this.exp = exp;
    }

    public LVal ParseLVal(Tokenlist tokenlist, boolean flag)
    {
        Token ident1 = tokenlist.getToken();
        Exp exp1 = new Exp(null);
        Exp exp2 = null;
        tokenlist.ReadNext();
        Token lbrack = null;
        Token rbrack = null;
        Symbol symbol1 = null;
        if (flag)
        {
            symbol1 = handleCError(ident1);
        }
        if (tokenlist.getToken().getType() == Token.Type.LBRACK)
        {
            lbrack = tokenlist.getToken();
            tokenlist.ReadNext();
            exp2 = exp1.ParseExp(tokenlist, flag);
            //tokenlist.ReadNext();
            if (tokenlist.getToken().getType() != Token.Type.RBRACK)
            {
                if (flag)
                {
                    Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'k'));
                    System.out.println("LVal + 缺少一个右中括号");
                }
            }
            else
            {
                rbrack = tokenlist.getToken();
                tokenlist.ReadNext();
            }
        }
        LVal lVal = new LVal(ident1, lbrack, rbrack, exp2);
        if (flag && symbol1 != null)
        {
            lVal.setSymbol(symbol1);
            if (lbrack == null)
            {
                if (symbol1.getType() == Symbol.Type.Int || symbol1.getType() == Symbol.Type.ConstInt)
                {
                    lVal.setValuetype("int");
                }
                else if (symbol1.getType() == Symbol.Type.Char || symbol1.getType() == Symbol.Type.ConstChar)
                {
                    lVal.setValuetype("char");
                }
                else if (symbol1.getType() == Symbol.Type.IntArray || symbol1.getType() == Symbol.Type.ConstIntArray)
                {
                    lVal.setValuetype("intarray");
                }
                else if (symbol1.getType() == Symbol.Type.CharArray ||
                        symbol1.getType() == Symbol.Type.ConstCharArray)
                {
                    lVal.setValuetype("chararray");
                }
            }
            else
            {
                if (symbol1.getType() == Symbol.Type.IntArray || symbol1.getType() == Symbol.Type.ConstIntArray)
                {
                    lVal.setValuetype("int");
                }
                else if (symbol1.getType() == Symbol.Type.CharArray ||
                        symbol1.getType() == Symbol.Type.ConstCharArray)
                {
                    lVal.setValuetype("char");
                }
            }
        }
        return lVal;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.myToString());
        if (exp != null)
        {
            sb.append(lbrack.myToString());
            sb.append(exp.myToString());
            sb.append(rbrack.myToString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    private Symbol handleCError(Token ident)
    {
        Symbol symbol = null;
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.checkNotHasSymbol(ident.getContent()))
        {
            Errors errors = new Errors(ident.getLine(), 'c');
            Errorlist.getErrorlist().AddError(errors);
            symbol = null;
        }
        else
        {
            symbol = symbolTable.getSymbol(ident.getContent());
        }
        return symbol;
    }

    public Symbol getSymbol()
    {
        return symbol;
    }

    public void setSymbol(Symbol symbol)
    {
        this.symbol = symbol;
    }

    public void setValuetype(String valuetype)
    {
        this.valuetype = valuetype;
    }

    public String getValuetype()
    {
        return valuetype;
    }

    public Exp getExp()
    {
        return exp;
    }

    public Token getIdent()
    {
        return ident;
    }
}
