package frontend.Parser.statement;

import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class StmtContinue implements StmtAll
{
    private String type = "<Stmt>";
    private Token continuetk;
    private Token semicn;

    public StmtContinue(Token continuetk, Token semicn)
    {
        this.continuetk = continuetk;
        this.semicn = semicn;
    }

    public StmtContinue ParseStmtContinue(Tokenlist tokenlist, boolean flag)
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
                System.out.println("StmtContinue + 缺少一个分号");
            }
            tokenlist.ReadForward();
        }
        else
        {
            semicn = tokenlist.getToken();
        }
        tokenlist.ReadNext();
        return new StmtContinue(token1, semicn);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(continuetk.myToString());
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
