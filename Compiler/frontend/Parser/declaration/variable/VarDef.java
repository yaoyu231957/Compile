package frontend.Parser.declaration.variable;

import frontend.Parser.declaration.Btype;
import frontend.Parser.declaration.variable.variableinitval.VarInitVal;
import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolTable;
import middle.semantic.SymbolVar;

public class VarDef
{
    private String type = "<VarDef>";
    private Token ident;
    private Token lbrack;

    private Token rbrack;

    private Token assign;
    private ConstExp constExp;
    private VarInitVal varInitVal;

    public VarDef(Token ident, Token lbrack, Token rbrack, Token assign, ConstExp constExp, VarInitVal varInitVal)
    {
        this.ident = ident;
        this.lbrack = lbrack;
        this.rbrack = rbrack;
        this.assign = assign;
        this.constExp = constExp;
        this.varInitVal = varInitVal;
    }

    public VarDef ParseVarDef(Tokenlist tokenlist, Btype btype, boolean flag)
    {
        Token ident = tokenlist.getToken();
        ConstExp constExp1 = null;
        ConstExp constExp2 = new ConstExp(null);
        VarInitVal varInitVal1 = new VarInitVal(null);
        VarInitVal varInitVal2 = null;
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
                    System.out.println("VarDef + 缺少一个右中括号");
                }
                tokenlist.ReadForward();
            }
            else
            {
                rbrack = tokenlist.getToken();
            }
            tokenlist.ReadNext();
        }
        if (tokenlist.getToken().getType() == Token.Type.ASSIGN)
        {
            assign = tokenlist.getToken();
            tokenlist.ReadNext();
            varInitVal2 = varInitVal1.ParseVarInitVal(tokenlist, flag);
        }
        if (flag)
        {
            addSymbols(btype, lbrack, ident);
        }
        return new VarDef(ident, lbrack, rbrack, assign, constExp1, varInitVal2);
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
        if (assign != null)
        {
            sb.append(assign.myToString());
            sb.append(varInitVal.toString());
        }
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
                symbol = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.Int);
            }
            else
            {
                symbol = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.Char);
            }
        }
        else
        {
            if (btype.getToken().getType() == Token.Type.INTTK)
            {
                symbol = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.IntArray);
            }
            else
            {
                symbol = new SymbolVar(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.CharArray);
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

    public VarInitVal getVarInitVal()
    {
        return varInitVal;
    }

    public ConstExp getConstExp()
    {
        return constExp;
    }
}
