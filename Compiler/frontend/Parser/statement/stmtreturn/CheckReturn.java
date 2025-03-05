package frontend.Parser.statement.stmtreturn;

import frontend.Parser.function.FuncType;
import frontend.Parser.statement.StmtIf;
import frontend.Parser.statement.stmtfor.StmtFor;
import frontend.lexer.Errorlist;
import frontend.lexer.Errors;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

import java.util.ArrayList;

public class CheckReturn
{
    private int type;//1:return ; 2: return + value ; 3: null
    private int line;

    public CheckReturn(int type, int line)
    {
        this.line = line;
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public int getLine()
    {
        return line;
    }

    public ArrayList<CheckReturn> CheckReturn(Tokenlist tokenlist)
    {
        boolean flag = false;//判断最后有没有return
        boolean flag1 = false;//判断是否最外层有if包裹
        ArrayList<CheckReturn> checkReturns = new ArrayList<>();
        int over = 1;
        int line = 0;
        int cur_pos = tokenlist.getCur_pos();
        while (over != 0)
        {
            tokenlist.ReadNext();
            if (tokenlist.IsOver())
            {
                break;
            }
            if (tokenlist.getToken().getType() == Token.Type.LBRACE)
            {
                over++;
            }
            if (tokenlist.getToken().getType() == Token.Type.RBRACE)
            {
                over--;
            }


            if (tokenlist.getToken().getType() == Token.Type.IFTK)
            {
                int cur_pos1 = tokenlist.getCur_pos();
                StmtIf stmtIf = new StmtIf(null, null, null, null, null, null, null);
                StmtIf stmtIf1 = stmtIf.ParseStmtIf(tokenlist, false);
                if (tokenlist.getToken().getType() == Token.Type.RBRACE && over == 1)
                {
                    flag1 = true;
                    break;
                }
                tokenlist.ChangeCur_pos(cur_pos1);
            }
            if (tokenlist.getToken().getType() == Token.Type.FORTK)
            {
                int cur_pos1 = tokenlist.getCur_pos();
                StmtFor stmtFor = new StmtFor(null, null, null, null, null, null, null, null);
                StmtFor stmtFor1 = stmtFor.ParseStmtFor(tokenlist, false);
                if (tokenlist.getToken().getType() == Token.Type.RBRACE && over == 1)
                {
                    flag1 = true;
                    break;
                }
                tokenlist.ChangeCur_pos(cur_pos1);
            }


            if (!tokenlist.IsOver() && tokenlist.getToken().getType() == Token.Type.RETURNTK)
            {
                line = tokenlist.getToken().getLine();
                /*
                boolean flag1 = true;
                if (tokenlist.getForwardToken().getType() == Token.Type.IFTK || tokenlist.getForwardToken().getType() == Token.Type.ELSETK)
                {
                    flag1 = false;
                }

                 */
                if (tokenlist.getNextToken().getType() == Token.Type.SEMICN || tokenlist.getNextToken().getType() == Token.Type.RBRACE)
                {
                    checkReturns.add(new CheckReturn(1, line));
                }
                else
                {
                    checkReturns.add(new CheckReturn(2, line));
                }
                StmtReturn stmtReturn1 = new StmtReturn(null, null, null);
                StmtReturn stmtReturn2 = stmtReturn1.ParseStmtReturn(tokenlist, false);

                /*
                while (tokenlist.getToken().getType() != Token.Type.SEMICN && tokenlist.getToken().getType() != Token.Type.RBRACE)
                {
                    tokenlist.ReadNext();
                }
                if ((tokenlist.getToken().getType() == Token.Type.SEMICN && tokenlist.getNextToken().getType() == Token.Type.RBRACE && over == 1) || (tokenlist.getToken().getType() == Token.Type.RBRACE && over == 1))
                {
                    flag = true;
                    break;
                }

                 */
                if (tokenlist.getToken().getType() == Token.Type.RBRACE && over == 1)
                {
                    flag = true;
                    break;
                }
                else
                {
                    tokenlist.ReadForward();
                }
            }
        }
        if (!flag || flag1)
        {
            line = tokenlist.getToken().getLine();
            checkReturns.add(new CheckReturn(3, line));
        }
        tokenlist.ChangeCur_pos(cur_pos);
        return checkReturns;
    }
}
