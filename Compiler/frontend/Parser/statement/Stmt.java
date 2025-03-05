package frontend.Parser.statement;

import frontend.Parser.expression.primaryexp.LVal;
import frontend.Parser.statement.block.StmtBlock;
import frontend.Parser.statement.stmtfor.StmtFor;
import frontend.Parser.statement.stmtreturn.StmtReturn;
import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class Stmt
{
    private String type = "<Stmt>";
    private StmtAll stmt;

    private String valuetype;

    public Stmt(StmtAll stmt)
    {
        this.stmt = stmt;
    }

    public Stmt ParseStmt(Tokenlist tokenlist, boolean flag)
    {
        StmtAll stmtAll = null;
        Token token = tokenlist.getToken();
        switch (token.getType())
        {
            case SEMICN:
                StmtExp stmtExp = new StmtExp(null, null);
                stmtAll = stmtExp.ParseStmtExp(tokenlist, flag);
                valuetype = "semicolon";  // 赋值为 "semicolon"
                break;
            case IFTK:
                StmtIf stmtIf = new StmtIf(null, null, null, null, null, null, null);
                stmtAll = stmtIf.ParseStmtIf(tokenlist, flag);
                valuetype = "if";  // 赋值为 "if"
                break;
            case FORTK:
                StmtFor stmtFor = new StmtFor(null, null, null, null, null, null, null, null);
                stmtAll = stmtFor.ParseStmtFor(tokenlist, flag);
                valuetype = "for";  // 赋值为 "for"
                break;
            case BREAKTK:
                StmtBreak stmtBreak = new StmtBreak(null, null);
                stmtAll = stmtBreak.ParseStmtBreak(tokenlist, flag);
                valuetype = "break";  // 赋值为 "break"
                break;
            case CONTINUETK:
                StmtContinue stmtContinue = new StmtContinue(null, null);
                stmtAll = stmtContinue.ParseStmtContinue(tokenlist, flag);
                valuetype = "continue";  // 赋值为 "continue"
                break;
            case RETURNTK:
                StmtReturn stmtReturn = new StmtReturn(null, null, null);
                stmtAll = stmtReturn.ParseStmtReturn(tokenlist, flag);
                valuetype = "return";  // 赋值为 "return"
                break;
            case PRINTFTK:
                StmtPrintf stmtPrintf = new StmtPrintf(null, null, null, null, null, null, null);
                stmtAll = stmtPrintf.ParseStmtPrintf(tokenlist, flag);
                valuetype = "printf";  // 赋值为 "printf"
                break;
            case LBRACE:
                StmtBlock stmtBlock = new StmtBlock(null, null, null);
                stmtAll = stmtBlock.ParseStmtBlock(tokenlist, true, flag);
                valuetype = "block";
                break;
            case IDENFR:
                stmtAll = HandleIdent(tokenlist, flag);
                valuetype = "identifier";  // 赋值为 "identifier"
                break;
            case LPARENT:
            case PLUS:
            case MINU:
            case INTCON:
            case CHRCON:
                StmtExp stmtExp1 = new StmtExp(null, null);
                stmtAll = stmtExp1.ParseStmtExp(tokenlist, flag);
                valuetype = "expression";  // 赋值为 "expression"
                break;
            default:
                System.out.println(token.getType() + "  没有对应的Stmt");
                valuetype = "unknown";  // 赋值为 "unknown" 用于默认情况
        }
        return new Stmt(stmtAll);
    }

    private StmtAll HandleIdent(Tokenlist tokenlist, boolean flag)
    {
        int count = tokenlist.getCur_pos();
        StmtAll stmtAll;
        Token token = tokenlist.getToken();
        if (tokenlist.getNextToken().getType() == Token.Type.LPARENT)
        {
            StmtExp stmtExp1 = new StmtExp(null, null);
            stmtAll = stmtExp1.ParseStmtExp(tokenlist, flag);
            valuetype = "function";  // 赋值为 "function_call"
        }
        else
        {
            LVal lVal1 = new LVal(null, null, null, null);
            LVal lVal2 = lVal1.ParseLVal(tokenlist, false);
            if (tokenlist.getToken().getType() == Token.Type.ASSIGN &&
                    (tokenlist.getNextToken().getType() == Token.Type.GETINTTK ||
                            tokenlist.getNextToken().getType() == Token.Type.GETCHARTK))
            {
                tokenlist.ChangeCur_pos(count);
                StmtGet stmtGet = new StmtGet(null, null, null, null, null, null);
                stmtAll = stmtGet.ParseStmtGet(tokenlist, flag);
                valuetype = "get";  // 赋值为 "get"
            }
            else if (tokenlist.getToken().getType() == Token.Type.ASSIGN)
            {
                tokenlist.ChangeCur_pos(count);
                StmtAssign stmtAssign = new StmtAssign(null, null, null, null);
                stmtAll = stmtAssign.ParseAssign(tokenlist, flag);
                valuetype = "assign";  // 赋值为 "assign"
            }
            else
            {
                tokenlist.ChangeCur_pos(count);
                StmtExp stmtExp1 = new StmtExp(null, null);
                stmtAll = stmtExp1.ParseStmtExp(tokenlist, flag);
                valuetype = "expression";  // 赋值为 "expression"
            }
        }
        return stmtAll;
    }

            /*
            while (token.getType() != Token.Type.SEMICN && tokenlist.getCur_pos() + 1 < tokenlist.getTokenlist().size())
            {
                tokenlist.ReadNext();
                token = tokenlist.getToken();
                count += 1;
                if (token.getType() == Token.Type.ASSIGN)
                {
                    flag = true;
                }
                if (token.getType() == Token.Type.GETINTTK || token.getType() == Token.Type.GETCHARTK)
                {
                    mode = 1;
                }
            }
            tokenlist.ChangeCur_pos(count);
            if (flag)
            {
                if (mode == 1)
                {
                    StmtGet stmtGet = new StmtGet(null, null, null, null, null, null);
                    stmtAll = stmtGet.ParseStmtGet(tokenlist);
                }
                else
                {
                    StmtAssign stmtAssign = new StmtAssign(null, null, null, null);
                    stmtAll = stmtAssign.ParseAssign(tokenlist);
                }
            }

             */

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(stmt.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public String getValuetype()
    {
        return valuetype;
    }

    public StmtAll getStmt()
    {
        return stmt;
    }
}
