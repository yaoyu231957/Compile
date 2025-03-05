import frontend.Parser.CompUnit;
import frontend.lexer.*;
import middle.llvmir.Value.IrBuilder;
import middle.llvmir.Value.IrModule;
import middle.semantic.SymbolTable;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;

public class Compiler
{


    public static void main(String[] args)
    {
        try
        {
            String content = Files.readString(Paths.get("testfile.txt"));
            Tokenlist tokenlist = new Tokenlist();
            Lexer lexer = new Lexer(content, tokenlist);
            lexer.LexicalAnalysis();
            ArrayList<Token> tokenList = lexer.getTokenlist();
            CompUnit ParserCompUnit = new CompUnit(null, null, null);
            CompUnit compUnit = ParserCompUnit.ParseCompUnit(tokenlist);
            IrBuilder irBuilder = new IrBuilder(compUnit);
            IrModule irModule = null;
            if (Errorlist.getErrorlist().getErrors().isEmpty())
            {
                irModule = irBuilder.buildIrModule();
            }
            if (Errorlist.getErrorlist().getErrors().isEmpty())
            {

                try (PrintWriter writer = new PrintWriter(new FileWriter("parser.txt")))
                {
                    writer.println(compUnit.toString());
                }
                catch (IOException e)
                {
                    //System.out.println("Output Error!");
                }


                try (PrintWriter writer = new PrintWriter(new FileWriter("symbol.txt")))
                {
                    SymbolTable symbolTable = SymbolTable.getAllsymboltable();
                    writer.println(symbolTable.toString());
                }
                catch (IOException e)
                {
                    //System.out.println("Output Error!");
                }

                try (PrintWriter writer = new PrintWriter(new FileWriter("llvm_ir.txt")))
                {
                    SymbolTable symbolTable = SymbolTable.getAllsymboltable();
                    ArrayList<String> output = irModule.irOutput();
                    for (String string : output)
                    {
                        writer.print(string);
                    }
                }
                catch (IOException e)
                {
                    //System.out.println("Output Error!");
                }
            }
            else
            {
                try (PrintWriter writer = new PrintWriter(new FileWriter("error.txt")))
                {
                    ArrayList<Errors> errorlist = Errorlist.getErrorlist().getErrors();
                    for (Errors error : errorlist)
                    {
                        writer.println(error.getLine() + " " + error.getType());
                    }
                }
                catch (IOException e)
                {
                    //System.out.println("Output Error!");
                }
            }
        }
        catch (IOException e)
        {
            //System.out.println("File Open Error!");
        }
    }

}