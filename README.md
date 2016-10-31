#EasyLC
一种简单的Lambda Calculus解释器Java实现。在支持Lambda Calculus的符号规则的前提下，有着易调试，易编写的特性。
<p align="center">
  <img src="https://github.com/lsj9383/EasyLC/blob/master/icon/title.png?raw=true" alt="Y"/>
</p>
[这里](https://github.com/lsj9383/EasyLC/blob/master/lcsrc/Demo.lc)有一个简单lc范例。
	
##一、运行
该工程可以使用Eclipse或是MyEclipse打开，并通过该IDE运行(后期会引入jar包，并通过jar包运行)。整个lc的代码编写位置在`./lcsrc/`路径下，为了标识清楚，请将lc代码文件用`.lc`作为后缀名。默认以`./lcsrc/Main.lc`作为程序入口。

##二、配置文件
工程路径下的`lc.conf`为配置文件，其中包含以下配置参数：

项目 | 说明 
-----|------
Main | 程序入口(./lcsrc/Main.lc)
Log  | 日志文件(./tmp/log)
--------------

##三、语法
EasyLC本质是Lambda Calculus，并在其基础上做了丰富，方便研究人员调试。在`附录-Lambda Calculus`中，会给出Lambda演算的的BNF表示，并给出解释。虽然在这里不描述lambda演算语法具体的内容，但也会给出相应的语法形式。这里展现出该解释器的特殊语法:

###1.定义函数
(lambda <param> <body>)
需要注意的是<param>和<body>都应该是lambda演算的表达式，因此只能是一个符号、一个函数调用或是一个函数定义。如:<br>
(lambda x x) 定义了<param>为x，<body>因为x的函数
<br>
(lambda p (lambda x (p x))) 定义了一个<param>为p，<body>为(lambda x (p x))。很明显，p是另一个函数，<body>是另一个函数定义。

###2.函数调用
(<fun> <param>)
需要注意的是<fun>和<param>都应该是lambda演算的表达式，因此只能是一个符号、一个函数调用或是一个函数定义。如:<br>
(p x) 将实现一个函数调用。<br>
lambda演算的函数调用，完全就是通过`符号替换`实现的。符号替换满足alpha和beta约束，将在附录中介绍.

###3.全局环境与绑定约束
按照lambda演算的标准，是没有环境这一概念的，也没有"="这样的语法。若按照lambda演算的标准语法来写，这样调试起来将会相当不便，因此这里引入了全局环境和绑定约束的概念。<br>
<br>
**约束绑定**:<br>
`<KEY> = <LCExpression>`
使用符号`=`, 可以将其左右两侧的数据绑定在一起。在运行该语句后，<KEY>的值将会被记录，当下次使用<KEY>时，会自动替换为<LCExpression>。如:<br>
```LC
1 = (lambda p (lambda x (p x)))
INC = (lambda n (lambda p (lambda x (p ((n p) x)))))
2 = (INC 1)
```
很明显，当约束绑定后，可以在lambda演算的符号中直接使用建立约束的符号。这是非常有利于记录数据的。需要注意的是，绑定是可以更改的，但不能在函数体中进行更改。**只能在最外层中进行约束的更改**，一方面可以保证程序的简单性，另一方面lambda演算本身就不能在函数体中有这样的操作。
<br>
**全局环境**:<br>
解释器维护了一个全局环境，解释器也只有这个单一环境。这个环境记录了所有的约束。

###4.@INT
lambda演算神奇之处是数值都是不存在的，数值需要用函数去定义。若数值大了，或是有了嵌套情况，想直观的知道这个数值是多大是非常困难的，因此提供了`@JAVA <Identify>`的语法，可以将符号输出为数值(若这个符号是邱奇编码的)。

###5.@REDUCE
lambda演算是一系列的符号替换，调试起来相当困难。为了减轻调试难度，并是符号替换的过程更为直观，定义了`@REDUCE <LCExpression>`，这个语法会将LC表达式符号替换的所有步骤清楚的打印在控制台中。

###6.@IMPORT
这个类似于C/C++中的include"..", 因此该解释器可以通过@IMPORT进行简单的模块化操作。当解释器解读到`@IMPORT <FilePath>`时，便会跳到指定的.lc文件中去。比较典型的简单模块化设计方案是：
```LC
/* Main.lc */
@IMPORT "./src/Basic.lc"
...
```

```LC
/* Basic.lc */
@IMPORT "./src/NUMBER.lc"
@IMPORT "./src/OPERATION.lc"
@IMPORT "./src/PREDICATION.lc"
...
```

在主函数入口中，加载Basic.lc中定义的语句，在Basic.lc中又可以额外加载其他一系列语句，包括预定义的数字、数学操作、谓词、逻辑流控制等等语句。在有了丰富的基本语句后，便可以在Main.lc中进行相关的处理了(造轮子很麻烦的)。需要注意的是:<br>
a).<FilePath>中若使用相对路，则"./"所指的当前文件夹就是整个仓库文件夹。<br>
b).文件的@IMPORT顺序在默认情况下关系的，但若是使用@JAVA这样的语句，那么就会和顺序有关系。因为若首先加载A文件，且其中使用了`@JAVA A`，需要保证这个符号在之前已经被绑定了。

###7.@LOG
`@REDUCE`指令会展示出所有的规约过程，有些表达式的规约非常长，控制台无法缓存那么多内容。因此为了方便，提供了将输出保存到"./tmp/log"文件中的命令。只要使用`@TMP`就会将之前的输出，全部保存到该文件中。

##附录

###Lambda Calculus
```
<exp>	:=	Identify
		:=	λ.<exp> <exp>
		:=	(<exp> <exp>)
```

###BNF
BNF即巴科斯范式，以一种形式化的符号来描述给定语言的语法。
