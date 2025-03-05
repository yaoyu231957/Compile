package frontend.Parser.expression.unaryexp;

import frontend.Parser.expression.Exp;
import frontend.Parser.expression.FuncRParams;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Tokenlist;
import frontend.lexer.Token;
import middle.semantic.Symbol;
import middle.semantic.SymbolFunc;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class UnaryFunc implements UnaryAll
{
    private Token name;
    private Token lparent;
    private Token rparent;
    private FuncRParams funcRParams;

    private String valuetype = null;

    public UnaryFunc(Token name, Token lparent, Token rparent, FuncRParams funcRParams)
    {
        this.name = name;
        this.lparent = lparent;
        this.rparent = rparent;
        this.funcRParams = funcRParams;
    }

    public UnaryFunc ParseUnaryfunc(Tokenlist tokenlist, boolean flag)
    {
        Token name1 = tokenlist.getToken();
        tokenlist.ReadNext();
        if (flag)
        {
            handleCError(name1);
        }
        Token lparent = tokenlist.getToken();
        Token rparent = null;
        FuncRParams funcRParams1 = new FuncRParams(null, null);
        FuncRParams funcRParams2 = funcRParams1.ParseFuncRParams(tokenlist, flag);
        if (flag)
        {
            handleDError(name1, funcRParams2);
            handleEError(name1, funcRParams2);
        }
        if (tokenlist.getToken().getType() == Token.Type.RPARENT)
        {
            rparent = tokenlist.getToken();
            tokenlist.ReadNext();
        }
        else
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
                System.out.println("MainFuncDef + 缺少一个右小括号");
            }
        }
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        Symbol symbol = symbolTable.getSymbol(name1.getContent());
        UnaryFunc unaryFunc = new UnaryFunc(name1, lparent, rparent, funcRParams2);
        if (symbol != null && symbol.getType() == Symbol.Type.IntFunc)
        {
            unaryFunc.setValuetype("int");
        }
        else
        {
            unaryFunc.setValuetype("char");
        }
        return unaryFunc;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name.myToString());
        sb.append(lparent.myToString());
        sb.append(funcRParams.toString());
        if (rparent != null)
        {
            sb.append(rparent.myToString());
        }
        return sb.toString();
    }

    public Token getName()
    {
        return name;
    }

    public FuncRParams getFuncRParams()
    {
        return funcRParams;
    }


    private void handleCError(Token ident)
    {
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.checkNotHasSymbol(ident.getContent()))
        {
            Errors errors = new Errors(ident.getLine(), 'c');
            Errorlist.getErrorlist().AddError(errors);
        }
    }

    private void handleDError(Token token, FuncRParams funcRParams)
    {
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        Symbol symbol = symbolTable.getSymbol(token.getContent());
        if (!(symbol instanceof SymbolFunc))
        {
            return;
        }
        SymbolFunc symbolFunc = (SymbolFunc) symbol;
        /* 处理没有参数的正确情况 */
        if ((funcRParams.getExps() == null && symbolFunc.getSymbols().size() != 0) || funcRParams.getExps().size() != symbolFunc.getSymbols().size())
        {
            Errors errors = new Errors(token.getLine(), 'd');
            Errorlist.getErrorlist().AddError(errors);
        }
    }

    private void handleEError(Token token, FuncRParams funcRParams)
    {
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        Symbol symbol = symbolTable.getSymbol(token.getContent());
        if (!(symbol instanceof SymbolFunc))
        {
            return;
        }
        SymbolFunc symbolFunc = (SymbolFunc) symbol;
        ArrayList<Symbol> symbols = symbolFunc.getSymbols();
        /* 无参数说明为正确情况，不应当处理 */
        if (this.funcRParams == null && symbols.size() == 0)
        {
            return;
        }
        /* 参数列表长度不匹配，说明已经被作为d类错误处理过 */
        if ((funcRParams == null && symbols.size() != 0) ||
                funcRParams.getExps().size() != symbols.size())
        {
            return;
        }
        ArrayList<Exp> exps = funcRParams.getExps();
        int len = symbols.size();
        for (int i = 0; i < len; i++)
        {
            Exp exp = exps.get(i);
            symbol = symbols.get(i);
            if ((symbol.getType() == Symbol.Type.Int && !exp.getValuetype().equals("int") && !exp.getValuetype().equals("char")) || (symbol.getType() == Symbol.Type.Char && !exp.getValuetype().equals("char") && !exp.getValuetype().equals("int")) || (symbol.getType() == Symbol.Type.IntArray && !exp.getValuetype().equals("intarray")) || (symbol.getType() == Symbol.Type.CharArray && !exp.getValuetype().equals("chararray")))
            {
                Errors errors = new Errors(token.getLine(), 'e');
                Errorlist.getErrorlist().AddError(errors);
                break;
            }
        }
    }

    public void setValuetype(String valuetype)
    {
        this.valuetype = valuetype;
    }

    public String getValuetype()
    {
        return valuetype;
    }
}
