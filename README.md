## 一个llvm_ir编译器设计文档

### 总体设计

编译器总体分为五个部分，首先是词法分析，接着语法分析、语义分析及生成中间代码，然后中间代码优化，最后生成目标程序。

文件的组织如下图

```
│  Compiler.java
├─ frontend
│  ├─ lexer
│  │  ├─ Errorlist.java
│  │  ├─ Errors.java
│  │  ├─ Lexer.java
│  │  ├─ Token.java
│  │  └─ Tokenlist.java
│  │
│  └─ parser
│      ├─ CompUnit.java
│      ├─ declaration
│      │  ├─ Btype.java
│      │  ├─ Decl.java
│      │  ├─ DeclAll.java
│      │  ├─ constant
│      │  │  ├─ ConstDecl.java
│      │  │  ├─ ConstDef.java
│      │  │  └─ consinitval
│      │  │      ├─ ConIVArray.java
│      │  │      ├─ ConIVConstExp.java
│      │  │      ├─ ConIVStrConst.java
│      │  │      ├─ ConstAllInitVal.java
│      │  │      └─ ConstInitVal.java
│      │  └─ variable
│      │      ├─ VarDecl.java
│      │      ├─ VarDef.java
│      │      └─ variableinitval
│      │          ├─ VarAllInitVal.java
│      │          ├─ VarInitVal.java
│      │          ├─ VarIVArray.java
│      │          ├─ VarIVExp.java
│      │          └─ VarIVStrConst.java
│      │
│      ├─ expression
│      │  ├─ AllExp.java
│      │  ├─ CondExp.java
│      │  ├─ Exp.java
│      │  ├─ FuncRParams.java
│      │  ├─ arithmeticalexp
│      │  │  ├─ AddExp.java
│      │  │  ├─ ArithExpAll.java
│      │  │  ├─ ConstExp.java
│      │  │  ├─ EqExp.java
│      │  │  ├─ LAndExp.java
│      │  │  ├─ LOrExp.java
│      │  │  ├─ MulExp.java
│      │  │  └─ RelExp.java
│      │  ├─ primaryexp
│      │  │  ├─ Character.java
│      │  │  ├─ ExpPrim.java
│      │  │  ├─ LVal.java
│      │  │  ├─ Number.java
│      │  │  ├─ PrimaryAllExp.java
│      │  │  └─ PrimaryExp.java
│      │  └─ unaryexp
│      │      ├─ UnaryAll.java
│      │      ├─ UnaryExp.java
│      │      ├─ UnaryFunc.java
│      │      ├─ UnaryOp.java
│      │      └─ UnaryPriExp.java
│      │
│      ├─ function
│      │  ├─ FuncDef.java
│      │  ├─ FuncFParam.java
│      │  ├─ FuncType.java
│      │  └─ MainFuncDef.java
│      │
│      └─ statement
│          ├─ Stmt.java
│          ├─ StmtAll.java
│          ├─ StmtAssign.java
│          ├─ StmtBreak.java
│          ├─ StmtContinue.java
│          ├─ StmtExp.java
│          ├─ StmtGet.java
│          ├─ StmtIf.java
│          └─ StmtPrintf.java
│          ├─ block
│          │  ├─ BlockDecl.java
│          │  ├─ BlockItem.java
│          │  ├─ BlockStmt.java
│          │  └─ StmtBlock.java
│          ├─ stmtfor
│          │  ├─ ForStmt.java
│          │  └─ StmtFor.java
│          └─ stmtreturn
│             ├─ CheckReturn.java
│             └─ StmtReturn.java
│
└─ middle
   ├─ llvmir
   │  └─ Value
   │      ├─ IrArgument.java
   │      ├─ IrBasicBlock.java
   │      ├─ IrBuilder.java
   │      ├─ IrConstant.java
   │      ├─ IrModule.java
   │      ├─ IrType.java
   │      └─ IrValue.java
   │  ├─ GlobalValue
   │  │  ├─ IrFunction.java
   │  │  ├─ IrGlobalVar.java
   │  │  ├─ IrParam.java
   │  │  └─ IrString.java
   │  └─ IrInstruction
   │      ├─ IrAllocaInst.java
   │      ├─ IrBinaryOperator.java
   │      ├─ IrBlockInfo.java
   │      ├─ IrBranchInst.java
   │      ├─ IrCallInst.java
   │      ├─ IrCompareInst.java
   │      ├─ IrGetelement.java
   │      ├─ IrInstruction.java
   │      ├─ IrLoadInst.java
   │      ├─ IrReturnInst.java
   │      ├─ IrStoreInst.java
   │      ├─ IrTrunc.java
   │      └─ IrZext.java
   └─ semantic
      ├─ Symbol.java
      ├─ SymbolConst.java
      ├─ SymbolFunc.java
      ├─ SymbolTable.java
      └─ SymbolVar.java

```



### 接口设计

#### 前端

- Lexer

  - Tokenlist：即Token序列，存放着词法分析处理完的Token
  - Toke：定义Token的基本属性，以及在之后的分析中可能需要的内容，包括名字、种类、行号等
  - Errorlist：即Error序列，存放着在处理过程中发现的error
  - Error：定义error的基本属性，方便输出，包括行号、类型

- Parser

  - CompUnit：整个编译单元，语法分析的开始

  - 以及下属的每一个语法成分，包括declaration,expression,function,statement四大类。

    每一类主要包含三大部分。

    - 基础属性
    - Parse方法
    - toString方法

#### 中端

- Syntax
  - Symbol：符号，定义基础属性和后面可能需要的内容
  - Symboltable：符号表，存储符号，帮助错误处理

#### 后端

### 词法分析

词法分析（Lexical Analysis）是编译器的前端阶段，承担着将源代码转换为标记（token）序列的重要任务。

这部分主要分为两个方面。一是对于正确的源程序，需要从源程序中识别出单词，记录其单词类别和单词值，输出单词类别码和单词的字符/字符串形式。另一反面，对于错误的源程序，需要识别出错误，并输出错误所在的行号和错误的类别码。

对于一个程序来说，词法分析阶段需要识别的内容有标识符、整数常量，字符串常量，字符常量，关键字以及一些符号。对于这些部分需要做出对应的识别，避免漏判误判。值得注意的点是标识符在识别后需要再判断是否为关键字；同时具有两个字符的符号在出现第一个不能着急下判断，需要进一步确认。

为了下一步对分析后所得到的处理序列能够进行更好的分析，我使用了Token类来进行关键信息的记录，包括类型，值等等。

在编写完成后，发现在对标识符的识别中还存在问题，遗漏了'_'这一个字符存在的可能性，从而导致了错误。还有在对转义字符的处理上，需要能够准确判断哪些字符属于转义字符，并且在处理后的存储上面不能犯错。还有一个方面是忽略了对注释的处理，需要注意。

在词法分析阶段，只可能出现a类错误，即非法符号错误，出现了 '&' 和 '|' 这两个符号，应该将其当做 '&&' 与 '||' 进行处理，但是在记录单词名称的时候仍记录 '&'和 '|'。这个处理较为容易，但是需要注意，即使是一个错误的源程序，也应该完成词法分析的全部任务，这样才能进行语法分析和语义分析部分的错误处理。



### 语法分析

语法分析（Syntax Analysis）是编译器的前端阶段，其主要任务是根据语法规则（即语言的文法），分析并识别出各种语法成分，如表达式、各种说明、各种语句、过程、函数等，并进行语法正确性检查。

因此首先需要将所以语法成分先确认清楚，我将其大致分为四类：`function`、`declaration`、`statement`和`expression`，分别对应函数、声明、语句和表达式四个类别，这些也是语法成分的主要所属。接下来在实现的时候，也按照这四个类的依赖关系，首先实现`expression`中的类，接着实现`declration`中的类，然后实现`statement`类，最后实现`function`中的类。

对于每一个小的语法成分，其所在的类中不仅要包含该语法成分的基本属性，包括名字，类型等等，最重要的是每一个语法类都要有一个解析器（Parser），用来识别该语法成分。

对于每种语法成分的解析，应该严格按照给出的语法规则进行判断，要做好对Tokenlist的维护，对所以语法成分的解析都源于此。同时要注意当多种语法的First集有相交时的处理，要继续往下判断，直到能够准确识别出是哪一种语法成分。其中比较困难的是对于`stmt`中`lval =`与`exp`几大类的处理，需要较为深入的判断。

最终我们经过语法分析阶段，得到的是一棵语法树，其从`CompUnit`，一直往下延伸。在最后输出结果时也应该按照顺序层层输出。

在语法分析阶段，我们要求处理i,j,k三类错误，分别为缺少分号,缺少右小括号’)’，缺少右中括号’]’。这几类错误都只有在语法解析的同时处理。当当前语法块需要哪一个符号但是却没有时，便向Errorlist中添加相应错误。值得注意的是，在处理完错误后程序还要能正常进行，因此在错误处理时需要增添相关操作。



### 语义分析

语义分析（Semantic Analysis），顾名思义，即对识别出的各种语法成分进行“语义分析”。这里的语义分析主要是对错误的处理。包括

- 1、上下文有关分析：即标识符的作用域 (Scope)
- 2、类型的一致性检查 (Type Checking)
- 3、语义处理：
  - 声明语句：其语义是声明变量的类型等，并不要求做其他的操作。需要填符号表，登录名字的特征信息，分配存储。
  - 执行语句：语义是要做某种操作。需要检查该操作的正确性。

所以总体来说，这一部分的工作有两个。一是填符号表，二是进行错误处理。

先说说符号表。这次作业中，符号一共分为11种：

| 类型           | 类型名称       | 类型           | 类型名称  | 类型       | 类型名称 |
| -------------- | -------------- | -------------- | --------- | ---------- | -------- |
| char型常量     | ConstChar      | char型变量     | Char      | void型函数 | VoidFunc |
| int型常量      | ConstInt       | int型变量      | Int       | char型函数 | CharFunc |
| char型常量数组 | ConstCharArray | char型变量数组 | CharArray | int型函数  | IntFunc  |
| int型常量数组  | ConstIntArray  | int型变量数组  | IntArray  |            |          |

我们需要对这些内容进行正确识别。

纵观整个文法，需要我们填符号表的地方有常量声明、定义；变量声明、定义；函数定义；函数形参定义。需要我们查表的地方有常量声明、定义；变量声明、定义；函数定义；函数形参定义；以及各个可能使用到这些常量、变量、函数的地方。

符号表也应该提供添加和查询方法，方便对符合的添加，以及错误处理时的操作。

### 代码生成一

这次作业为了让同学们尽快实现一个完整的编译器，测试程序中仅涉及**常量声明、变量声明、读语句、写语句、赋值语句，加减乘除模除等运算语句、函数定义及调用语句**。即if和for语句两个打大头没有实现。

我选择的是LLVM IR 代码，其基本架构如下

![图片#50% #center](https://judge.buaa.edu.cn/cguserImages?_img=30639449344bc202b2e4ac7ba5b5ab1a.png)

我也是大致按照这个架构来完成了这次作业的开发

对于模块中不同粒度的所有语法结构，将都作为 `Value` 类的子类。

在 LLVM IR 中，一个 `Module` 由若干 *`GlobalValue`* 组成，而一个 *`GlobalValue`* 可以是全局变量（`GlobalVariable`），也可以是函数（`Function`）。一个函数由若干基本块（`BasicBlock`）组成，与大家在理论课上学习的基本块是一样的。在基本块内部，则是由若干指令（`Instruction`）组成，也是 LLVM IR 的基本组成。这四个层次构成了整个架构，每个层次分别作LLVM IR代码的构建。一点点往下面深入，逐步完成

在本次作业中实现的 Instructions如下（包括下次作业的，写到一起感觉比较好）

| LLVM IR         | 使用方法                                                     | 简介                                                   |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------ |
| `add`           | `<result> = add <ty> <op1>, <op2>`                           | /                                                      |
| `sub`           | `<result> = sub <ty> <op1>, <op2>`                           | /                                                      |
| `mul`           | `<result> = mul <ty> <op1>, <op2>`                           | /                                                      |
| `sdiv`          | `<result> = sdiv <ty> <op1>, <op2>`                          | 有符号除法                                             |
| `srem`          | `<result> = srem <type> <op1>, <op2>`                        | 有符号取余                                             |
| `icmp`          | `<result> = icmp <cond> <ty> <op1>, <op2>`                   | 比较指令                                               |
| `and`           | `<result> = and <ty> <op1>, <op2>`                           | 按位与                                                 |
| `or`            | `<result> = or <ty> <op1>, <op2>`                            | 按位或                                                 |
| `call`          | `<result> = call [ret attrs] <ty> <name>(<...args>)`         | 函数调用                                               |
| `alloca`        | `<result> = alloca <type>`                                   | 分配内存                                               |
| `load`          | `<result> = load <ty>, ptr <pointer>`                        | 读取内存                                               |
| `store`         | `store <ty> <value>, ptr <pointer>`                          | 写内存                                                 |
| `getelementptr` | `<result> = getelementptr <ty>, ptr <ptrval>{, <ty> <idx>}*` | 计算目标元素的位置（数组部分会单独详细说明）           |
| `phi`           | `<result> = phi [fast-math-flags] <ty> [<val0>, <label0>], ...` | /                                                      |
| `zext..to`      | `<result> = zext <ty> <value> to <ty2>`                      | 将 `ty` 的 `value` 的 type 扩充为 `ty2`（zero extend） |
| `trunc..to`     | `<result> = trunc <ty> <value> to <ty2>`                     | 将 `ty` 的 `value` 的 type 缩减为 `ty2`（truncate）    |
| `br`            | `br i1 <cond>, label <iftrue>, label <iffalse>` `br label <dest>` | 改变控制流                                             |
| `ret`           | `ret <type> <value> `, `ret void`                            | 退出当前函数，并返回值                                 |

在实现这次作业的过程中，比较麻烦的是两个东西的处理，一个是数组类型，一个是函数。

对于数组来说，主要是`getelementptr`语句

`getelementptr` 指令的工作是计算地址。其本身不对数据做任何访问与修改。其语法如下：

```javascript
<result> = getelementptr <ty>, <ty>* <ptrval>{, [inrange] <ty> <idx>}*
```

第一个 `<ty>` 表示的是第一个索引所指向的类型，有时也是返回值的类型。第二个 `<ty>` 表示的是后面的指针基地址 `<ptrval>` 的类型， `<ty> <index>` 表示的是一组索引的类型和值，在本实验中索引的类型为 `i32`。索引指向的基本类型确定的是增加索引值时指针的偏移量。

主要注意的是`<ptrval>`和`<idx>`的值

对于函数，比较麻烦的是对其形参的使用。

形参在使用时，需要先对其进行取值，这个过程需要考虑形参类型，从而确定如何进行取值。

在整个过程中，类型转换也伴随始终，同时这也是一个易错点，很容易忘记考虑。需要注意每一个可能有类型转换的地方。

### 代码生成二

这次作业完成后我们则完成了一个简单的编译器。任务就是剩下的if和for语句。

这两个的处理有类似的地方，需要注意的点有下面几个。

一是条件判断，主要设计到短路求值。LLVM IR 不能够单纯的把 `Cond` 计算完后再进行跳转。如果出现短路求值，这时候就需要对 `Cond` 的跳转逻辑进行改写。

据短路求值，只要条件判断出现“短路”，即不需要考虑后续与或参数的情况下就已经能确定值的时候，就可以进行跳转。或者更简单的来说，当 **LOrExp 值为 1** 或者 **LAndExp 值为 0** 的时候，就已经没有必要再进行计算了。

二是跳转问题。一方面是条件语句的跳转，一方面是后面stmt语句的跳转。

条件语句要根据是否短路求值来确定下一个跳转的目标。同时需要提前准备好所有的基本块，用于跳转时指定。

同时if和for语句的跳转顺序如下：

![图片#60%#center](https://judge.buaa.edu.cn/cguserImages?_img=0c2b698ced9b9abd332157e6d5e76e8b.png)

<img src="https://judge.buaa.edu.cn/cguserImages?_img=dac656925c1ce6f1b74cdcee999262b9.png" alt="图片#40% #center" style="zoom: 50%;" />

### 感想

完成编译大作业让我深刻感受到了编译器设计的复杂性与挑战，同时也让我对编译原理的重要性有了更为直观的认识。编译器不仅仅是将高级语言转换为机器代码的工具，它涉及的知识面非常广泛，涵盖了语法、语义、程序优化、错误处理等多个方面，而每个环节都需要高度的精确性和高效性。

在完成这次作业的过程中，我才真正意识到编译器的工作远比我想象的要复杂得多。编写一个功能完整的编译器不仅需要投入大量的时间和精力，还需要扎实的编程基础和深入的计算机原理知识。通过实现词法分析、语法分析、语义分析等功能，我不仅学到了如何将编程语言的规则转化为计算机可以理解的指令，还学会了如何设计高效的数据结构来支持这些操作。每一步的实现都要求我从底层出发，了解计算机如何解析和执行代码，这让我对计算机的工作原理有了更深刻的认识。

这次作业不仅提升了我的编程能力，还锻炼了我的问题解决能力和调试技巧。在编译器的开发过程中，我遇到了许多难以预料的错误和复杂的难题，调试成为日常工作的一部分。这些经历要求我保持耐心，注重细节，同时也让我认识到编写清晰、可维护代码的重要性。除此之外，编写编译器是一个系统性的工作，需要各个模块的紧密配合，这让我深刻理解了模块化设计的重要性。编译器不仅是计算机语言的翻译器，它实际上是程序和硬件之间的桥梁。在设计和实现编译器的过程中，我更加理解了编程语言的设计理念，以及计算机如何通过抽象层次逐步执行程序。这个过程让我对计算机科学的魅力有了更深的体会，也让我更加坚定了未来深入研究这一领域的决心。

总的来说，这次编译大作业不仅让我掌握了编译原理的核心知识，也培养了我面对复杂问题时的思维方式和解决问题的能力。这段经历不仅提升了我的编程水平，还增强了我对计算机底层原理的兴趣，让我更加自信地面对未来的学习与挑战。
