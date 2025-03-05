package frontend.Parser.function;

import frontend.Parser.statement.block.StmtBlock;
import frontend.Parser.statement.stmtreturn.CheckReturn;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.Symbol;
import middle.semantic.SymbolFunc;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class FuncDef
{
    private String type = "<FuncDef>";

    private String type_inner = "<FuncFParams>";
    private FuncType funcType;
    private Token ident;

    private Token lparent;

    private Token rparent;
    private ArrayList<Token> commas;
    private ArrayList<FuncFParam> funcFParams;

    private StmtBlock stmtBlock;

    private SymbolFunc symbolFunc;

    public FuncDef(FuncType funcType, Token ident, Token lparent, Token rparent, ArrayList<Token> commas, ArrayList<FuncFParam> funcFParams, StmtBlock stmtBlock, SymbolFunc symbolFunc)
    {
        this.funcType = funcType;
        this.ident = ident;
        this.lparent = lparent;
        this.rparent = rparent;
        this.commas = commas;
        this.funcFParams = funcFParams;
        this.stmtBlock = stmtBlock;
        this.symbolFunc = symbolFunc;
    }

    public FuncDef ParseFuncDef(Tokenlist tokenlist)
    {
        FuncType funcType1 = new FuncType(null);
        FuncType funcType2 = funcType1.ParseFunctype(tokenlist);
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        Token lparent = null;
        Token rparent = null;
        Symbol symbol = addSymbols(token1, funcType2);
        SymbolFunc symbolFunc = (SymbolFunc) symbol;
        SymbolTable.UpLevel();
        SymbolTable symbolTable1 = new SymbolTable(SymbolTable.getAllsymboltable());
        SymbolTable.getAllsymboltable().addChildren(symbolTable1);
        SymbolTable.changeAllSymbolTable(symbolTable1);
        if (tokenlist.getToken().getType() != Token.Type.LPARENT)
        {
            System.out.println("FuncDef + 缺少一个左小括号");
            tokenlist.ReadForward();
        }
        else
        {
            lparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        ArrayList<FuncFParam> funcFParams1 = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        if (tokenlist.getToken().getType() == Token.Type.INTTK || tokenlist.getToken().getType() == Token.Type.CHARTK)
        {
            FuncFParam funcFParam = new FuncFParam(null, null, null, null, false);
            funcFParams1.add(funcFParam.ParseFuncFParam(tokenlist, symbolFunc));
            //tokenlist.ReadNext();
            //boolean flag = false;
            while (tokenlist.getToken().getType() == Token.Type.COMMA)
            {
                commas.add(tokenlist.getToken());
                tokenlist.ReadNext();
                funcFParams1.add(funcFParam.ParseFuncFParam(tokenlist, symbolFunc));
                //tokenlist.ReadNext();
                //flag = true;
            }
//            if (flag)
//            {
//                tokenlist.ReadForward();
//            }
        }
        if (tokenlist.getToken().getType() != Token.Type.RPARENT)
        {
            Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'j'));
            System.out.println("FuncDef + 缺少一个右小括号");
            tokenlist.ReadForward();
        }
        else
        {
            rparent = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        CheckReturn checkReturn = new CheckReturn(1, 0);
        ArrayList<CheckReturn> checkReturns = checkReturn.CheckReturn(tokenlist);
        StmtBlock stmtBlock1 = new StmtBlock(null, null, null);
        StmtBlock stmtBlock2 = stmtBlock1.ParseStmtBlock(tokenlist, false, true);
        handleFGError(funcType2, checkReturns);
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        SymbolTable.changeAllSymbolTable(SymbolTable.getAllsymboltable().getParent());
        return new FuncDef(funcType2, token1, lparent, rparent, commas, funcFParams1, stmtBlock2, symbolFunc);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(funcType.toString());
        sb.append(ident.myToString());
        sb.append(lparent.myToString());
        if (!funcFParams.isEmpty())
        {
            sb.append(funcFParams.get(0));
            for (int i = 0; i < commas.size(); i++)
            {
                sb.append(commas.get(i));
                sb.append(funcFParams.get(i + 1).toString());
            }
            sb.append(type_inner + "\n");
        }
        sb.append(rparent.myToString());
        sb.append(stmtBlock.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public Symbol addSymbols(Token ident, FuncType funcType)
    {
        Symbol symbol;
        if (funcType.getFtype().getType() == Token.Type.INTTK)
        {
            symbol = new SymbolFunc(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.IntFunc);
        }
        else if (funcType.getFtype().getType() == Token.Type.CHARTK)
        {
            symbol = new SymbolFunc(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.CharFunc);
        }
        else
        {
            symbol = new SymbolFunc(ident.getLine(), SymbolTable.getAllsymboltable().getLevel(), ident.getContent(), Symbol.Type.
                    VoidFunc);
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
        //SymbolTable.UpLevel();
        /*
        SymbolTable symbolTable1 = new SymbolTable(SymbolTable.getAllsymboltable());
        SymbolTable.getAllsymboltable().addChildren(symbolTable1);
        SymbolTable.changeAllSymbolTable(symbolTable1);

         */

        return symbol;
    }

    private void handleFGError(FuncType funcType, ArrayList<CheckReturn> checkReturns)
    {
        if (funcType.getFtype().getType() == Token.Type.VOIDTK)
        {
            /* f error */
            for (CheckReturn checkReturn : checkReturns)
            {
                if (checkReturn.getType() == 2)
                {
                    Errors errors = new Errors(checkReturn.getLine(), 'f');
                    Errorlist.getErrorlist().AddError(errors);
                }
            }
        }
        else
        {
            /* g error */

            if (checkReturns.get(checkReturns.size() - 1).getType() == 3)
            {
                Errors errors = new Errors(checkReturns.get(checkReturns.size() - 1).getLine(), 'g');
                Errorlist.getErrorlist().AddError(errors);
            }
        }
    }

    public FuncType getFuncType()
    {
        return funcType;
    }

    public ArrayList<FuncFParam> getFuncFParams()
    {
        return funcFParams;
    }

    public Token getIdent()
    {
        return ident;
    }

    public StmtBlock getStmtBlock()
    {
        return stmtBlock;
    }
}
