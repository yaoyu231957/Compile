package frontend.lexer;

import java.util.ArrayList;

public class Lexer
{
    private int cur = 0;

    private int line = 1;

    private String procedure;

    private int errorflag = 0;

    private ArrayList<Token> tokenlist;

    private ArrayList<Errors> errorlist = Errorlist.getErrorlist().getErrors();

    public Lexer(String procedure, Tokenlist tokenlist)
    {
        this.procedure = procedure;
        this.tokenlist = tokenlist.getTokenlist();
    }

    public void LexicalAnalysis()
    {
        int length = procedure.length();
        char cur_pos;
        String word;
        String value;
        Token.Type type;
        while (cur < length)
        {
            SkipWhitespace();
            cur_pos = procedure.charAt(cur);
            if (IsLetter(cur_pos))
            {
                word = IdentifyWord();
                type = IsKeyword(word);
                if (type == null)
                {
                    tokenlist.add(new Token(Token.Type.IDENFR, word, line, false));
                }
                else
                {
                    tokenlist.add(new Token(type, word, line, false));
                }
                continue;
            }
            if (IsDigit(cur_pos))
            {
                value = IdentifyValue();
                tokenlist.add(new Token(Token.Type.INTCON, value, line, false));
                continue;
            }
            if (IsChar(cur_pos))
            {
                IdentifyChar();
                continue;
            }
            if (cur_pos != ' ' && cur_pos != '\r' && cur_pos != '\n' && cur_pos != '\t')
            {
                ErrorOccurred(cur_pos, line);
                errorflag = 1;
            }
        }
    }

    public Boolean IsLetter(char c)
    {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean IsDigit(char c)
    {
        if (c >= '0' && c <= '9')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean IsChar(char c)
    {
        if (c == '!' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '<' || c == '>' || c == '=' || c == ';' || c == ',' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' || c == '\"' || c == '\'')
        {
            return true;
        }
        else if (c == '&')
        {
            if (procedure.charAt(cur + 1) == '&')
            {
                return true;
            }
        }
        else if (c == '|')
        {
            if (procedure.charAt(cur + 1) == '|')
            {
                return true;
            }
        }
        cur++;
        return false;
    }

    public Token.Type IsKeyword(String word)
    {
        switch (word)
        {
            case "main":
                return Token.Type.MAINTK;
            case "const":
                return Token.Type.CONSTTK;
            case "int":
                return Token.Type.INTTK;
            case "char":
                return Token.Type.CHARTK;
            case "break":
                return Token.Type.BREAKTK;
            case "continue":
                return Token.Type.CONTINUETK;
            case "if":
                return Token.Type.IFTK;
            case "else":
                return Token.Type.ELSETK;
            case "for":
                return Token.Type.FORTK;
            case "getint":
                return Token.Type.GETINTTK;
            case "getchar":
                return Token.Type.GETCHARTK;
            case "printf":
                return Token.Type.PRINTFTK;
            case "return":
                return Token.Type.RETURNTK;
            case "void":
                return Token.Type.VOIDTK;
            default:
                ;
        }
        return null;
    }

    public String IdentifyWord()
    {
        StringBuilder sb = new StringBuilder();
        char cur_pos = procedure.charAt(cur);
        sb.append(cur_pos);
        cur++;
        cur_pos = procedure.charAt(cur);
        while (IsLetter(cur_pos) || IsDigit(cur_pos) || cur_pos == '_')
        {
            sb.append(cur_pos);
            cur++;
            cur_pos = procedure.charAt(cur);
        }
        return String.valueOf(sb);
    }

    public String IdentifyValue()
    {
        StringBuilder sb = new StringBuilder();
        char cur_pos = procedure.charAt(cur);
        sb.append(cur_pos);
        cur++;
        cur_pos = procedure.charAt(cur);
        while (IsDigit(cur_pos))
        {
            sb.append(cur_pos);
            cur++;
            cur_pos = procedure.charAt(cur);
        }
        return String.valueOf(sb);
    }

    public void IdentifyChar()
    {
        StringBuilder sb = new StringBuilder();
        char cur_pos = procedure.charAt(cur);
        switch (cur_pos)
        {
            case '+':
                tokenlist.add(new Token(Token.Type.PLUS, "+", line, false));
                break;
            case '-':
                tokenlist.add(new Token(Token.Type.MINU, "-", line, false));
                break;
            case '*':
                tokenlist.add(new Token(Token.Type.MULT, "*", line, false));
                break;
            case '/':
                if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '/')
                {
                    while (cur + 1 < procedure.length() && cur_pos != '\n')
                    {
                        cur++;
                        cur_pos = procedure.charAt(cur);
                        if (cur_pos == '\n')
                        {
                            line++;
                        }
                    }
                    ////System.out.println("找到//");
                }
                else if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '*')
                {
                    cur = cur + 2;
                    cur_pos = procedure.charAt(cur);
                    while (cur + 1 < procedure.length() && (cur_pos != '*' || procedure.charAt(cur + 1) != '/'))
                    {
                        cur++;
                        cur_pos = procedure.charAt(cur);
                        if (cur_pos == '\n')
                        {
                            line++;
                        }
                    }
                    cur++;
                    ////System.out.println("找到/**/");
                }
                else
                {
                    tokenlist.add(new Token(Token.Type.DIV, "/", line, false));
                }
                break;
            case '%':
                tokenlist.add(new Token(Token.Type.MOD, "%", line, false));
                break;
            case ';':
                tokenlist.add(new Token(Token.Type.SEMICN, ";", line, false));
                break;
            case ',':
                tokenlist.add(new Token(Token.Type.COMMA, ",", line, false));
                break;
            case '(':
                tokenlist.add(new Token(Token.Type.LPARENT, "(", line, false));
                break;
            case ')':
                tokenlist.add(new Token(Token.Type.RPARENT, ")", line, false));
                break;
            case '[':
                tokenlist.add(new Token(Token.Type.LBRACK, "[", line, false));
                break;
            case ']':
                tokenlist.add(new Token(Token.Type.RBRACK, "]", line, false));
                break;
            case '{':
                tokenlist.add(new Token(Token.Type.LBRACE, "{", line, false));
                break;
            case '}':
                tokenlist.add(new Token(Token.Type.RBRACE, "}", line, false));
                break;
            case '\"':
                StringBuilder sb1 = new StringBuilder();
                int flag = 0;//前一个字符是否转义的标识
                sb1.append('\"');
                cur++;
                char c = procedure.charAt(cur);
                while (c != '\"' || (c == '\"' && procedure.charAt(cur - 1) == '\\' && flag == 0))
                {
                    if (c == '\'' || c == '\\' || c == '\"')
                    {
                        if (procedure.charAt(cur - 1) == '\\' && flag == 0)
                        {
                            //sb1.deleteCharAt(sb1.length() - 1);
                            if (c == '\\')
                            {
                                flag = 1;
                            }
                        }
                        else
                        {
                            flag = 0;
                        }
                    }
                    sb1.append(c);
                    cur++;
                    c = procedure.charAt(cur);
                }
                sb1.append('\"');
                tokenlist.add(new Token(Token.Type.STRCON, String.valueOf(sb1), line, false));
                break;
            case '\'':
                StringBuilder sb2 = new StringBuilder();
                flag = 0;
                sb2.append('\'');
                cur++;
                c = procedure.charAt(cur);

                while (c != '\'' || (c == '\'' && procedure.charAt(cur - 1) == '\\' && flag == 0))
                {
                    if (c == '\'' || c == '\\' || c == '\"')
                    {
                        if (procedure.charAt(cur - 1) == '\\' && flag == 0)
                        {
                            //sb2.deleteCharAt(sb2.length() - 1);
                            if (c == '\\')
                            {
                                flag = 1;
                            }
                        }
                        else
                        {
                            flag = 0;
                        }
                    }
                    sb2.append(c);
                    cur++;
                    c = procedure.charAt(cur);
                }


                /*
                while (c != '\'' || (c == '\'' && procedure.charAt(cur - 1) == '\\'))
                {
                    sb2.append(c);
                    cur++;
                    //c = procedure.charAt(cur);

                    if (cur < procedure.length())
                    {
                        c = procedure.charAt(cur);
                    }
                    else
                    {
                        break;
                    }
                }

                 */
                sb2.append('\'');
                tokenlist.add(new Token(Token.Type.CHRCON, String.valueOf(sb2), line, false));
                break;
            case '!':
                if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '=')
                {
                    cur++;
                    tokenlist.add(new Token(Token.Type.NEQ, "!=", line, false));
                }
                else
                {
                    tokenlist.add(new Token(Token.Type.NOT, "!", line, false));
                }
                break;
            case '<':
                if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '=')
                {
                    cur++;
                    tokenlist.add(new Token(Token.Type.LEQ, "<=", line, false));
                }
                else
                {
                    tokenlist.add(new Token(Token.Type.LSS, "<", line, false));
                }
                break;
            case '>':
                if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '=')
                {
                    cur++;
                    tokenlist.add(new Token(Token.Type.GEQ, ">=", line, false));
                }
                else
                {
                    tokenlist.add(new Token(Token.Type.GRE, ">", line, false));
                }
                break;
            case '=':
                if (cur + 1 < procedure.length() && procedure.charAt(cur + 1) == '=')
                {
                    cur++;
                    tokenlist.add(new Token(Token.Type.EQL, "==", line, false));
                }
                else
                {
                    tokenlist.add(new Token(Token.Type.ASSIGN, "=", line, false));
                }
                break;
            case '&':
                cur++;
                tokenlist.add(new Token(Token.Type.AND, "&&", line, false));
                break;
            case '|':
                cur++;
                tokenlist.add(new Token(Token.Type.OR, "||", line, false));
                break;
            default:
                ;

        }
        cur++;
    }

    public void SkipWhitespace()
    {
        char cur_pos = procedure.charAt(cur);
        while (cur + 1 < procedure.length() && (cur_pos == '\n' || cur_pos == '\t' || cur_pos == ' ' || cur_pos == '\r'))
        {
            if (cur_pos == '\n')
            {
                line++;
            }
            cur++;
            cur_pos = procedure.charAt(cur);
        }
    }

    public void ErrorOccurred(char cur_pos, int line)
    {
        ////System.out.println(line + " " + 'a');
        if (cur_pos == '&')
        {
            tokenlist.add(new Token(Token.Type.AND, "&", line, true));
        }
        if (cur_pos == '|')
        {
            tokenlist.add(new Token(Token.Type.OR, "|", line, true));
        }
        errorlist.add(new Errors(line, 'a'));
    }

    public ArrayList<Token> getTokenlist()
    {
        return tokenlist;
    }

    public ArrayList<Errors> geterrorlist()
    {
        return errorlist;
    }

    public int getErrorflag()
    {
        return errorflag;
    }
}