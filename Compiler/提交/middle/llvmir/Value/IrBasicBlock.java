package middle.llvmir.Value;

import frontend.Parser.expression.CondExp;
import frontend.Parser.expression.arithmeticalexp.EqExp;
import frontend.Parser.expression.arithmeticalexp.LAndExp;
import frontend.Parser.expression.arithmeticalexp.LOrExp;
import frontend.Parser.statement.Stmt;
import frontend.Parser.statement.StmtAll;
import frontend.Parser.statement.StmtAssign;
import frontend.Parser.statement.StmtIf;
import frontend.Parser.statement.block.BlockDecl;
import frontend.Parser.statement.block.BlockItem;
import frontend.Parser.statement.block.BlockStmt;
import frontend.Parser.statement.block.StmtBlock;
import frontend.Parser.statement.stmtfor.ForStmt;
import frontend.Parser.statement.stmtfor.StmtFor;
import middle.llvmir.Value.GlobalValue.IrFunction;
import middle.llvmir.Value.IrInstruction.*;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class IrBasicBlock extends IrValue
{
    private String name; // 块的名字（label），可能没有
    private boolean needName = false;
    private ArrayList<IrInstruction> instructions;
    private IrFunction function; // 父function
    private SymbolTable symbolTable;
    private IrBlockInfo irBlockInfo;
    private StmtAll stmtAll;
    private CondExp condExp;
    private Stmt stmtif;
    private Stmt stmtelse;
    private EqExp eqExp;
    private ArrayList<BlockItem> blockItems = null;
    private ArrayList<IrBasicBlock> basicBlocks = new ArrayList<>();
    private ArrayList<IrBasicBlock> basicBlocks_for = null;

    public IrBasicBlock(String name)
    {
        super(IrType.i32);//todo
        this.name = name;
        this.instructions = new ArrayList<>();
    }

    public IrBasicBlock(SymbolTable symbolTable, StmtAll stmtAll)
    {
        super(IrType.i32);//todo
        this.symbolTable = symbolTable;
        this.stmtAll = stmtAll;
        this.instructions = new ArrayList<>();
    }

    public IrBasicBlock(SymbolTable symbolTable, CondExp condExp)
    {
        super(IrType.i32);//todo
        this.symbolTable = symbolTable;
        this.condExp = condExp;
        this.instructions = new ArrayList<>();
    }

    public IrBasicBlock(SymbolTable symbolTable, EqExp eqExp)
    {
        super(IrType.i32);//todo
        this.symbolTable = symbolTable;
        this.eqExp = eqExp;
    }

    public void addIrInstruction(IrInstruction instruction)
    {
        this.instructions.add(instruction);
    }

    public void addAllIrInstruction(ArrayList<IrInstruction> instructions)
    {
        this.instructions.addAll(instructions);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setNeedName(boolean needName)
    {
        this.needName = needName;
    }

    public boolean getNeedName()
    {
        return needName;
    }

    public void setBasicBlocks_for(ArrayList<IrBasicBlock> basicBlocks_for)
    {
        this.basicBlocks_for = basicBlocks_for;
    }

    public ArrayList<IrBasicBlock> getBasicBlocks_for()
    {
        return basicBlocks_for;
    }

    public ArrayList<IrBasicBlock> buildIrBasicBlock()
    {
        if (stmtAll instanceof StmtBlock)
        {
            /* 说明传入元素是Block */
            blockItems = ((StmtBlock) stmtAll).getBlockItems();
            return handleIRBlock();
        }
        else if (stmtAll instanceof StmtIf)
        {
            /* 说明传入元素是StmtIf */
            return handleIRIf();
//            ArrayList<IrBasicBlock> blocks = new ArrayList<>();
//            return blocks;
        }
        else if (stmtAll instanceof StmtFor)
        {
            return handleIRFor();
        }
        else
        {
            System.out.println("ERROR in IrBasicBlock : should not reach here");
        }
        return null;
    }

    private ArrayList<IrBasicBlock> handleIRBlock()
    {
        for (int i = 0; i < blockItems.size(); i++)
        {
            BlockItem blockItem = this.blockItems.get(i);
            if (blockItem instanceof BlockStmt)
            {
                /* 说明将要解析的元素是块类，会返回一个基本块列表 */
                /* 生成并进入新的符号表 */
                SymbolTable newSymbolTable = new SymbolTable(this.symbolTable);
                IrBasicBlock irBasicBlock = null;
                Stmt stmt = ((BlockStmt) blockItem).getStmt();
                StmtAll stmtEle = stmt.getStmt();
                if (stmtEle instanceof StmtBlock || stmtEle instanceof StmtIf || stmtEle instanceof StmtFor)
                {
                    irBasicBlock = new IrBasicBlock(newSymbolTable, stmtEle);
                    irBasicBlock.setFunction(function);
                    irBasicBlock.setBasicBlocks_for(basicBlocks_for);
                    this.addAllIrBasicBlocks(irBasicBlock.buildIrBasicBlock());
                }
                else
                {
                    /* 说明将要解析的元素是指令，一直解析直到元素末尾或遇到块类元素 */
                    IrBasicBlock basicBlock = new IrBasicBlock("NO NAME");
                    basicBlock.setFunction(function);
                    basicBlock.setBasicBlocks_for(basicBlocks_for);
                    /* 始终解析ptr指向的BlockItemEle，并加到当前的IrBasicBlock中，直到结束或遇到块类 */
                    IrInstruction irInstruction = new IrInstruction(basicBlock, symbolTable, blockItem);
                    ArrayList<IrInstruction> irInstructions = irInstruction.buildIrInstruction();
                    if (irInstructions != null && !irInstructions.isEmpty())
                    {
                        basicBlock.addAllIrInstruction(irInstructions);
                    }
                    basicBlocks.add(basicBlock);
                }
            }
            else if (blockItem instanceof BlockDecl)
            {
                IrBasicBlock basicBlock = new IrBasicBlock("NO NAME");
                /* 始终解析ptr指向的BlockItemEle，并加到当前的IrBasicBlock中，直到结束或遇到块类 */
                basicBlock.setFunction(function);
                IrInstruction irInstruction = new IrInstruction(basicBlock, symbolTable, blockItem);
                ArrayList<IrInstruction> irInstructions = irInstruction.buildIrInstruction();
                if (irInstructions != null && !irInstructions.isEmpty())
                {
                    basicBlock.addAllIrInstruction(irInstructions);
                }
                basicBlocks.add(basicBlock);
            }
        }
        return basicBlocks;
    }

    private ArrayList<IrBasicBlock> handleIRIf()
    {
        //todo
        StmtIf stmtIf = (StmtIf) stmtAll;
        ArrayList<IrBasicBlock> basicBlocks_if = new ArrayList<>();

        //Cond
        CondExp condExp = stmtIf.getCondExp();
        handleIRCondExp(condExp, basicBlocks_if);

        if (function.getBasicBlocks().isEmpty())
        {
            basicBlocks_if.get(0).setName("%0");
        }

        Stmt stmt_if = stmtIf.getStmt1();
        Stmt stmt_else = stmtIf.getStmt2();

        //if-true语句
        IrBasicBlock basicBlock_stmt_if = new IrBasicBlock(symbolTable, stmt_if.getStmt());
        basicBlock_stmt_if.setFunction(function);
        basicBlock_stmt_if.setBasicBlocks_for(basicBlocks_for);
        IrBlockInfo blockInfo_if = new IrBlockInfo(IrType.i32, basicBlock_stmt_if);
        basicBlock_stmt_if.setName("if");
        basicBlock_stmt_if.setNeedName(true);
        basicBlock_stmt_if.setIrBlockInfo(blockInfo_if);
        basicBlocks_if.add(basicBlock_stmt_if);

        //else-false语句
        IrBasicBlock basicBlock_stmt_else = null;
        IrBlockInfo blockInfo_else = null;
        if (stmt_else != null)
        {
            basicBlock_stmt_else = new IrBasicBlock(symbolTable, stmt_else.getStmt());
            basicBlock_stmt_else.setFunction(function);
            basicBlock_stmt_else.setBasicBlocks_for(basicBlocks_for);
            basicBlock_stmt_else.setName("else");
            basicBlock_stmt_else.setNeedName(true);
            blockInfo_else = new IrBlockInfo(IrType.i32, basicBlock_stmt_else);
            basicBlock_stmt_else.setIrBlockInfo(blockInfo_else);
            basicBlocks_if.add(basicBlock_stmt_else);
        }

        //结束

        IrBasicBlock basicBlock_end = new IrBasicBlock("if-end");
        IrBlockInfo blockInfo = new IrBlockInfo(IrType.i32, basicBlock_end);
        basicBlock_end.setIrBlockInfo(blockInfo);
        basicBlock_end.setNeedName(true);
        basicBlock_end.setFunction(function);
        basicBlocks_if.add(basicBlock_end);

        //basicBlocks_if.remove(0);


        //所有块都准备好后在处理cond
        handleIRCond(basicBlocks_if, condExp, true);

        //处理if的stmt
        basicBlock_stmt_if.setName(getAValue());
        basicBlock_stmt_if.addIrInstruction(blockInfo_if);
        StmtAll stmtAll_if = stmt_if.getStmt();
        IrBasicBlock irBasicBlock_if = new IrBasicBlock(symbolTable, stmtAll_if);
        irBasicBlock_if.setFunction(function);
        irBasicBlock_if.setBasicBlocks_for(basicBlocks_for);

        //把else,end块拿出来，放入代码块后再放回
        basicBlock_end = basicBlocks_if.remove(basicBlocks_if.size() - 1);
        if (stmt_else != null)
        {
            basicBlock_stmt_else = basicBlocks_if.remove(basicBlocks_if.size() - 1);
        }

        if (!(stmtAll_if instanceof StmtBlock) && !(stmtAll_if instanceof StmtIf) && !(stmtAll_if instanceof StmtFor))
        {
            Stmt stmt_stmt_if = new Stmt(stmtAll_if);
            IrInstruction irInstruction = new IrInstruction(basicBlock_stmt_if, symbolTable, stmt_stmt_if);
            ArrayList<IrInstruction> irInstructions = irInstruction.buildIrInstruction();
            if (irInstructions != null && !irInstructions.isEmpty())
            {
                basicBlock_stmt_if.addAllIrInstruction(irInstructions);
            }
        }
        else
        {
            ArrayList<IrBasicBlock> blocks_if = irBasicBlock_if.buildIrBasicBlock();
            if (blocks_if != null)
            {
                basicBlocks_if.addAll(blocks_if);
            }
        }

        IrBranchInst irBranchInst = new IrBranchInst(IrType.i32, basicBlock_end);
        ArrayList<IrInstruction> instructions1 = basicBlocks_if.get(basicBlocks_if.size() - 1).getInstructions();
        if (!instructions1.isEmpty() && !(instructions1.get(instructions1.size() - 1) instanceof IrBranchInst))
        {
            basicBlocks_if.get(basicBlocks_if.size() - 1).addIrInstruction(irBranchInst);
        }
        if (instructions1.isEmpty())
        {
            basicBlock_stmt_if.addIrInstruction(irBranchInst);
        }

//        IrInstruction irInstruction_stmt_if = new IrInstruction(basicBlock_stmt_if, symbolTable, stmt_if);
//        basicBlock_stmt_if.addAllIrInstruction(irInstruction_stmt_if.buildIrInstruction());

        //处理else的stmt
        if (stmt_else != null)
        {
            basicBlock_stmt_else.setName(getAValue());
            basicBlock_stmt_else.addIrInstruction(blockInfo_else);
            IrBasicBlock irBasicBlock_else = new IrBasicBlock(symbolTable, stmt_else.getStmt());
            irBasicBlock_else.setFunction(function);
            irBasicBlock_else.setBasicBlocks_for(basicBlocks_for);

            basicBlocks_if.add(basicBlock_stmt_else);

            StmtAll stmtAll_else = stmt_else.getStmt();

            if (!(stmtAll_else instanceof StmtBlock) && !(stmtAll_else instanceof StmtIf) && !(stmtAll_else instanceof StmtFor))
            {
                Stmt stmt_stmt_else = new Stmt(stmtAll_else);
                IrInstruction irInstruction = new IrInstruction(basicBlock_stmt_else, symbolTable, stmt_stmt_else);
                ArrayList<IrInstruction> irInstructions = irInstruction.buildIrInstruction();
                if (irInstructions != null && !irInstructions.isEmpty())
                {
                    basicBlock_stmt_else.addAllIrInstruction(irInstructions);
                }
            }
            else
            {
                ArrayList<IrBasicBlock> blocks_else = irBasicBlock_else.buildIrBasicBlock();
                if (blocks_else != null)
                {
                    basicBlocks_if.addAll(blocks_else);
                }
            }

            instructions1 = basicBlocks_if.get(basicBlocks_if.size() - 1).getInstructions();
            if (!instructions1.isEmpty() && !(instructions1.get(instructions1.size() - 1) instanceof IrBranchInst))
            {
                basicBlocks_if.get(basicBlocks_if.size() - 1).addIrInstruction(irBranchInst);
            }
            if (instructions1.isEmpty())
            {
                basicBlock_stmt_else.addIrInstruction(irBranchInst);
            }
//            IrInstruction irInstruction_stmt_else = new IrInstruction(basicBlock_stmt_else, symbolTable, stmt_else);
//            basicBlock_stmt_else.addAllIrInstruction(irInstruction_stmt_else.buildIrInstruction());
        }
        //放回
        basicBlocks_if.add(basicBlock_end);

        //处理结束块
        basicBlock_end.setName(getAValue());
        basicBlock_end.addIrInstruction(blockInfo);

        return basicBlocks_if;
        //return basicBlocks;
    }

    private ArrayList<IrBasicBlock> handleIRFor()
    {
        //todo
        StmtFor stmtFor = (StmtFor) stmtAll;
        ArrayList<IrBasicBlock> basicBlocks_for = new ArrayList<>();

        ForStmt forStmt1 = stmtFor.getForStmt1();
        ForStmt forStmt2 = stmtFor.getForStmt2();
        Stmt stmt = stmtFor.getStmt();

        //forstmt1语句
        StmtAssign stmtAssign_forstmt1 = null;
        if (forStmt1 != null)
        {
            stmtAssign_forstmt1 = new StmtAssign(forStmt1.getlVal(), null, null, forStmt1.getExp());
        }
        IrBasicBlock basicBlock_forstmt1 = new IrBasicBlock(symbolTable, stmtAssign_forstmt1);
        basicBlock_forstmt1.setName("forstmt1");
        basicBlock_forstmt1.setNeedName(true);
        basicBlock_forstmt1.setFunction(function);

        IrBlockInfo blockInfo_forstmt1 = new IrBlockInfo(IrType.i32, basicBlock_forstmt1);
        basicBlock_forstmt1.setIrBlockInfo(blockInfo_forstmt1);
        basicBlocks_for.add(basicBlock_forstmt1);

        //Cond

        IrBasicBlock basicBlock_cond = null;
        CondExp condExp = stmtFor.getCondExp();
        IrBlockInfo blockInfo_cond = null;
        if (condExp != null)
        {
            handleIRCondExp(condExp, basicBlocks_for);
            basicBlock_cond = basicBlocks_for.get(1);
            blockInfo_cond = new IrBlockInfo(IrType.i32, basicBlock_cond);
            basicBlock_cond.setIrBlockInfo(blockInfo_cond);
        }
        else
        {
            basicBlock_cond = new IrBasicBlock(symbolTable, condExp);
            basicBlock_cond.setIrBlockInfo(blockInfo_cond);
            basicBlock_cond.setNeedName(true);
            basicBlock_cond.setName("1");
            basicBlocks_for.add(1, basicBlock_cond);
            blockInfo_cond = new IrBlockInfo(IrType.i32, basicBlock_cond);
            basicBlock_cond.setIrBlockInfo(blockInfo_cond);
        }


//        basicBlock_cond.setName(getAValue());
//        basicBlock_cond.addIrInstruction(blockInfo_cond);

        if (function.getBasicBlocks().isEmpty())
        {
            basicBlocks_for.get(0).setName("%0");
        }


        //forstmt2语句
        StmtAssign stmtAssign_forstmt2 = null;
        if (forStmt2 != null)
        {
            stmtAssign_forstmt2 = new StmtAssign(forStmt2.getlVal(), null, null, forStmt2.getExp());
        }

        IrBasicBlock basicBlock_forstmt2 = new IrBasicBlock(symbolTable, stmtAssign_forstmt2);
        basicBlock_forstmt2.setFunction(function);
        basicBlock_forstmt2.setName("forstmt2");
        basicBlock_forstmt2.setNeedName(true);

        IrBlockInfo blockInfo_forstmt2 = new IrBlockInfo(IrType.i32, basicBlock_forstmt2);
        basicBlock_forstmt2.setIrBlockInfo(blockInfo_forstmt2);
        basicBlocks_for.add(basicBlock_forstmt2);


        //stmt语句
        IrBasicBlock basicBlock_stmt = new IrBasicBlock(symbolTable, stmt.getStmt());
        basicBlock_stmt.setFunction(function);
        basicBlock_stmt.setBasicBlocks_for(basicBlocks_for);
        basicBlock_stmt.setName("stmt");
        basicBlock_stmt.setNeedName(true);

        IrBlockInfo blockInfo_stmt = new IrBlockInfo(IrType.i32, basicBlock_stmt);
        basicBlock_stmt.setIrBlockInfo(blockInfo_stmt);
        basicBlocks_for.add(basicBlock_stmt);


        //结束
        IrBasicBlock basicBlock_end = new IrBasicBlock("for-end");
        IrBlockInfo blockInfo = new IrBlockInfo(IrType.i32, basicBlock_end);
        basicBlock_end.setIrBlockInfo(blockInfo);
        basicBlock_end.setNeedName(true);
        basicBlock_end.setFunction(function);
        basicBlocks_for.add(basicBlock_end);


        //所有块都准备好后先处理forstmt1
        //basicBlocks_for.addAll(1, basicBlock_forstmt1.buildIrBasicBlock());
        Stmt stmt_forstmt1 = new Stmt(stmtAssign_forstmt1);
        IrInstruction irInstruction = new IrInstruction(basicBlock_forstmt1, symbolTable, stmt_forstmt1);
        ArrayList<IrInstruction> irInstructions = irInstruction.buildIrInstruction();
        if (irInstructions != null && !irInstructions.isEmpty())
        {
            basicBlock_forstmt1.addAllIrInstruction(irInstructions);
        }
        IrBranchInst irBranchInst = new IrBranchInst(IrType.i32, basicBlock_cond);
        basicBlock_forstmt1.addIrInstruction(irBranchInst);

        // 处理cond
        //todo
        //没有实现
        if (condExp != null)
        {
            handleIRCond(basicBlocks_for, condExp, false);
        }
        else
        {
            basicBlock_cond.setName(getAValue());
            basicBlock_cond.addIrInstruction(blockInfo_cond);
            irBranchInst = new IrBranchInst(IrType.i32, basicBlock_stmt);
            basicBlock_cond.addIrInstruction(irBranchInst);
        }

        //处理forstmt2
        basicBlock_forstmt2.setName(getAValue());
        basicBlock_forstmt2.addIrInstruction(blockInfo_forstmt2);

        //把stmt,end块拿出来，放入代码块后再放回
        basicBlock_end = basicBlocks_for.remove(basicBlocks_for.size() - 1);
        basicBlock_stmt = basicBlocks_for.remove(basicBlocks_for.size() - 1);

        //basicBlocks_for.addAll(1, basicBlock_forstmt2.buildIrBasicBlock());
        Stmt stmt_forstmt2 = new Stmt(stmtAssign_forstmt2);
        irInstruction = new IrInstruction(basicBlock_forstmt2, symbolTable, stmt_forstmt2);
        irInstructions = irInstruction.buildIrInstruction();
        if (irInstructions != null && !irInstructions.isEmpty())
        {
            basicBlock_forstmt2.addAllIrInstruction(irInstructions);
        }

        IrBranchInst irBranchInst_cond = new IrBranchInst(IrType.i32, basicBlocks_for.get(1));
        basicBlock_forstmt2.addIrInstruction(irBranchInst_cond);

        //处理stmt
        //todo
        //break和continue
        basicBlock_stmt.setName(getAValue());
        basicBlock_stmt.addIrInstruction(blockInfo_stmt);
        IrBasicBlock irBasicBlock_stmt = new IrBasicBlock(symbolTable, stmt.getStmt());
        irBasicBlock_stmt.setFunction(function);
        irBasicBlock_stmt.setBasicBlocks_for(basicBlocks_for);

        basicBlocks_for.add(basicBlock_stmt);
        basicBlocks_for.add(basicBlock_end);

        StmtAll stmtAll = stmt.getStmt();

        if (!(stmtAll instanceof StmtBlock) && !(stmtAll instanceof StmtIf) && !(stmtAll instanceof StmtFor))
        {
            Stmt stmt1 = new Stmt(stmtAll);
            IrInstruction irInstruction1 = new IrInstruction(basicBlock_stmt, symbolTable, stmt1);
            ArrayList<IrInstruction> irInstructions1 = irInstruction1.buildIrInstruction();
            if (irInstructions1 != null && !irInstructions1.isEmpty())
            {
                basicBlock_stmt.addAllIrInstruction(irInstructions1);
            }
            basicBlock_end = basicBlocks_for.remove(basicBlocks_for.size() - 1);
        }
        else
        {
            ArrayList<IrBasicBlock> basicBlocks_stmt = irBasicBlock_stmt.buildIrBasicBlock();
            basicBlock_end = basicBlocks_for.remove(basicBlocks_for.size() - 1);
            if (basicBlocks_stmt != null)
            {
                basicBlocks_for.addAll(basicBlocks_stmt);
            }
        }


        irBranchInst = new IrBranchInst(IrType.i32, basicBlock_forstmt2);
        ArrayList<IrInstruction> instructions1 = basicBlocks_for.get(basicBlocks_for.size() - 1).getInstructions();
        if (!(instructions1.get(instructions1.size() - 1) instanceof IrBranchInst))
        {
            basicBlocks_for.get(basicBlocks_for.size() - 1).addIrInstruction(irBranchInst);
        }

        //放回
        basicBlocks_for.add(basicBlock_end);

        //处理结束块
        basicBlock_end.setName(getAValue());
        basicBlock_end.addIrInstruction(blockInfo);

        return basicBlocks_for;
    }

    private void handleIRCond(ArrayList<IrBasicBlock> basicBlocks, CondExp condExp, boolean isIf)
    {
        IrInstruction irInstruction_cond = new IrInstruction(symbolTable, condExp);
        irInstruction_cond.handleCondExp(basicBlocks, isIf);
    }

    public void handleIRCondExp(CondExp condExp, ArrayList<IrBasicBlock> basicBlocks)
    {
        LOrExp lOrExp = condExp.getExp();
        ArrayList<LAndExp> lAndExps = lOrExp.getExps();
        for (int i = 0; i < lAndExps.size(); i++)
        {
            ArrayList<EqExp> eqExps = lAndExps.get(i).getExps();
            for (int j = 0; j < eqExps.size(); j++)
            {
                IrBasicBlock basicBlock = new IrBasicBlock(i + "-" + j);
                IrBlockInfo blockInfo = new IrBlockInfo(IrType.i32, basicBlock);
                basicBlock.setNeedName(true);
                basicBlock.setIrBlockInfo(blockInfo);
                basicBlock.setFunction(function);
                basicBlock.setBasicBlocks_for(basicBlocks_for);
                basicBlocks.add(basicBlock);
            }
        }
    }

    @Override
    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        for (IrInstruction instruction : this.instructions)
        {
            /*
            if (instruction instanceof IrAllocaInst)
            {
                IrAllocaInst irAllocaInst = (IrAllocaInst) instruction;
                if (irAllocaInst.isParam())
                {
                    ret.addAll(irAllocaInst.irOutput());
                }
            }
            if (instruction instanceof IrStoreInst)
            {
                IrStoreInst irStoreInst = (IrStoreInst) instruction;
                if (irStoreInst.isParam())
                {
                    ret.addAll(irStoreInst.irOutput());
                }
            }
        }
        for (IrInstruction instruction : this.instructions)
        {
            if (instruction instanceof IrAllocaInst)
            {
                IrAllocaInst irAllocaInst = (IrAllocaInst) instruction;
                if (irAllocaInst.isParam())
                {
                    continue;
                }
            }
            if (instruction instanceof IrStoreInst)
            {
                IrStoreInst irStoreInst = (IrStoreInst) instruction;
                if (irStoreInst.isParam())
                {
                    continue;
                }
            }

             */
            ArrayList<String> temp = instruction.irOutput();
            if (temp != null && temp.size() != 0)
            {
                ret.addAll(temp);
            }
        }
        return ret;
    }

    public String getAValue()
    {
        if (function.isMainFunc())
        {
            return IrArgument.getAValue();
        }
        else
        {
            return function.getAValue();
        }
    }

    public ArrayList<IrInstruction> getInstructions()
    {
        return this.instructions;
    }

    private void addAllIrBasicBlocks(ArrayList<IrBasicBlock> blocks)
    {
        this.basicBlocks.addAll(blocks);
    }

    public IrFunction getFunction()
    {
        return function;
    }

    public void setFunction(IrFunction irFunction)
    {
        this.function = irFunction;
    }

    public IrBlockInfo getIrBlockInfo()
    {
        return irBlockInfo;
    }

    public void setIrBlockInfo(IrBlockInfo irBlockInfo)
    {
        this.irBlockInfo = irBlockInfo;
    }

    public void addPreds(IrValue pred)
    {
        irBlockInfo.addPreds(pred);
    }

    public void BasicBlockInit(IrBasicBlock basicBlock)
    {
        basicBlock.setFunction(function);
        basicBlock.setBasicBlocks_for(basicBlocks_for);
    }

}
