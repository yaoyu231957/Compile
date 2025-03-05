package frontend.Parser.declaration.constant;

import frontend.Parser.declaration.Btype;
import frontend.Parser.declaration.constant.consinitval.ConstInitVal;
import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolConst;
import middle.semantic.SymbolTable;

public class ConstDef
{
    private String type = "<ConstDef>";
    private Token ident;

    private Token lbrack;

    private Token rbrack;

    private Token assign;
    private ConstExp constExp;
    private ConstInitVal constInitVal;

    public ConstDef(Token ident, Token lbrack, Token rbrack, Token assign, ConstExp constExp, ConstInitVal constInitVal)
    {
        this.ident = ident;
        this.lbrack = lbrack;
        this.rbrack = rbrack;
        this.assign = assign;
        this.constExp = constExp;
        this.constInitVal = constInitVal;
    }

    public ConstDef ParseConstDef(Tokenlist tokenlist, Btype btype, boolean flag)
    {
        Token ident = tokenlist.getToken();
        ConstExp constExp1 = null;
        ConstExp constExp2 = new ConstExp(null);
        tokenlist.ReadNext();
        Token lbrack = null;
        Token rbrack = null;
        Token assign = null;
        if (tokenlist.getToken().getType() == Token.Type.LBRACK)
        {
            lbrack = tokenlist.getToken();
            tokenlist.ReadNext();
            constExp1 = constExp2.ParseConstExp(tokenlist, flag);
            //tokenlist.ReadNext();
            if (tokenlist.getToken().getType() != Token.Type.RBRACK)
            {
                if (flag)
                {
                    Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'k'));
                    System.out.println("ConstDef + 缺少一个右中括号");
                }
                tokenlist.ReadForward();
            }
            else
            {
                rbrack = tokenlist.getToken();
            }
            tokenlist.ReadNext();
        }
        if (tokenlist.getToken().getType() != Token.Type.ASSIGN)
        {
            System.out.println("ConstDef + 缺少一个等号");
            tokenlist.ReadForward();
        }
        else
        {
            assign = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        ConstInitVal constInitVal1 = new ConstInitVal(null);
        ConstInitVal constInitVal2 = constInitVal1.ParseConstInitVal(tokenlist, flag);
        if (flag)
        {
            addSymbols(btype, lbrack, ident);
        }
        return new ConstDef(ident, lbrack, rbrack, assign, constExp1, constInitVal2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.myToString());
        if (lbrack != null)
        {
            sb.append(lbrack.myToString());
            sb.append(constExp.myToString());
            sb.append(rbrack.myToString());
        }
        sb.append(assign.myToString());
        sb.append(constInitVal.myToString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public void addSymbols(Btype btype, Token lbrack, Token ident)
    {
        Symbol symbol;
        if (lbrack == null)
        {
            if (btype.getToken().getType() == Token.Type.INTTK)
            {
                symbol = new SymbolConst(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.ConstInt);
            }
            else
            {
                symbol = new SymbolConst(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.ConstChar);
            }
        }
        else
        {
            if (btype.getToken().getType() == Token.Type.INTTK)
            {
                symbol = new SymbolConst(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.ConstIntArray);
            }
            else
            {
                symbol = new SymbolConst(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.ConstCharArray);
            }
        }
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.checkHasSymbol(symbol))
        {
            Errors errors = new Errors(symbol.getLine(), 'b');
            Errorlist.getErrorlist().AddError(errors);
        }
        else
        {
            symbolTable.addSymbol(symbol);
        }
    }

    public Token getIdent()
    {
        return ident;
    }

    public ConstExp getConstExp()
    {
        return constExp;
    }

    public ConstInitVal getConstInitVal()
    {
        return constInitVal;
    }
}
