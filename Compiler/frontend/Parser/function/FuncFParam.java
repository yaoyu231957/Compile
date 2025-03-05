package frontend.Parser.function;

import frontend.Parser.declaration.Btype;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolFunc;
import middle.semantic.SymbolTable;
import middle.semantic.SymbolVar;

public class FuncFParam
{
    private String type = "<FuncFParam>";
    private Btype btype;
    private Token ident;

    private Token lbrack;
    private Token rbrack;

    private boolean isArray;

    public FuncFParam(Btype btype, Token ident, Token lbrack, Token rbrack, boolean isArray)
    {
        this.btype = btype;
        this.ident = ident;
        this.lbrack = lbrack;
        this.rbrack = rbrack;
        this.isArray = isArray;
    }

    public FuncFParam ParseFuncFParam(Tokenlist tokenlist, SymbolFunc symbolFunc)
    {
        Btype btype1 = new Btype(null);
        Btype btype2 = btype1.ParseBtype(tokenlist);
        //tokenlist.ReadNext();
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        boolean isArray = false;
        Token lbrack = null;
        Token rbrack = null;
        if (tokenlist.getToken().getType() == Token.Type.LBRACK)
        {
            isArray = true;
            lbrack = tokenlist.getToken();
            tokenlist.ReadNext();
            if (tokenlist.getToken().getType() != Token.Type.RBRACK)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'k'));
                System.out.println("FuncFParam + 缺少一个右中括号");
                tokenlist.ReadForward();
            }
            else
            {
                rbrack = tokenlist.getToken();
            }
            tokenlist.ReadNext();
        }
        addSymbol(token1, lbrack, btype2, symbolFunc);
        return new FuncFParam(btype2, token1, lbrack, rbrack, isArray);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(btype.toString());
        sb.append(ident.myToString());
        if (lbrack != null)
        {
            sb.append(lbrack.myToString());
            sb.append(rbrack.myToString());
        }
        sb.append(type + "\n");
        return sb.toString();
    }

    public void addSymbol(Token ident, Token lbrack, Btype btype, SymbolFunc symbolFunc)
    {
        SymbolVar symbolVar;
        if (btype.getToken().getType() == Token.Type.INTTK)
        {
            if (lbrack == null)
            {
                symbolVar = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.Int);
            }
            else
            {
                symbolVar = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.IntArray);
            }
        }
        else
        {
            if (lbrack == null)
            {
                symbolVar = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.Char);
            }
            else
            {
                symbolVar = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.CharArray);
            }
        }
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.checkHasSymbol(symbolVar))
        {
            Errors errors = new Errors(symbolVar.getLine(), 'b');
            Errorlist.getErrorlist().AddError(errors);
        }
        symbolFunc.addSymbol(symbolVar);
        symbolTable.addSymbol(symbolVar);

    }

    public Btype getBtype()
    {
        return btype;
    }

    public Token getIdent()
    {
        return ident;
    }

    public boolean isArray()
    {
        return isArray;
    }
}
