#EasyLC
一种简单的Lambda Calculus解释器Java实现。在支持Lambda Calculus的符号规则的前提下，有着易调试，易编写的特性。

##运行
该工程可以使用Eclipse或是MyEclipse打开，并通过该IDE运行(后期会引入jar包，并通过jar包运行)。整个lc的代码编写位置在./src/路径下，为了标识清楚，请将lc代码文件用.lc作为后缀名。默认以./src/Main.lc作为程序入口。

##语法
EasyLC本质是Lambda Calculus，并在其基础上做了丰富，方便研究人员调试。

##附录
###Lambda Calculus
```
<exp> :=	Identify |
			λ.<exp> <exp> |
			(<exp> <exp>)
```
###BNF
BNF即巴科斯范式，以一种形式化的符号来描述给定语言的语法。