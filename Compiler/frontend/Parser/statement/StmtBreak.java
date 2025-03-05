package frontend.Parser.statement;

import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class StmtBreak implements StmtAll
{
    private String type = "<Stmt>";
    private Token breaktk;
    private Token semicn;

    public StmtBreak(Token breaktk, Token semicn)
    {
        this.breaktk = breaktk;
        this.semicn = semicn;
    }

    public StmtBreak ParseStmtBreak(Tokenlist tokenlist, boolean flag)
    {
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        if (flag)
        {
            handleMError(token1);
        }
        Token semicn = null;
        if (tokenlist.getToken().getType() != Token.Type.SEMICN)
        {
            if (flag)
            {
                Errorlist.getErrorlist().AddError(new Errors(tokenlist.getForwardToken().getLine(), 'i'));
                System.out.println("StmtBreak + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new StmtBreak(token1, semicn);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(breaktk.myToString());
        if (semicn != null)
        {
            sb.append(semicn.myToString());
        }
        return sb.toString();
    }

    private void handleMError(Token token)
    {
        SymbolTable symbolTable = SymbolTable.getAllsymboltable();
        if (symbolTable.getCircle() == 0)
        {
            Errors errors = new Errors(token.getLine(), 'm');
            Errorlist.getErrorlist().AddError(errors);
        }
    }
}
