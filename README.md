#EasyLC
一种简单的Lambda Calculus解释器Java实现。在支持Lambda Calculus的符号规则的前提下，有着易调试，易编写的特性。
![](https://github.com/lsj9383/EasyLC/blob/master/icon/title.jpg)

##运行
该工程可以使用Eclipse或是MyEclipse打开，并通过该IDE运行(后期会引入jar包，并通过jar包运行)。整个lc的代码编写位置在`./src/`路径下，为了标识清楚，请将lc代码文件用`.lc`作为后缀名。默认以`./src/Main.lc`作为程序入口。

##语法
EasyLC本质是Lambda Calculus，并在其基础上做了丰富，方便研究人员调试。在`附录-Lambda Calculus`中，会给出Lambda演算的的BNF表示，并给出解释。虽然在这里不描述lambda演算语法具体的内容，但也会给出相应的语法形式。这里展现出该解释器的特殊语法:

#####定义函数
(lambda <param> <body>)
需要注意的是<param>和<body>都应该是lambda演算的表达式，因此只能是一个符号、一个函数调用或是一个函数定义。如:<br>
(lambda x x) 定义了<param>为x，<body>因为x的函数
<br>
(lambda p (lambda x (p x))) 定义了一个<param>为p，<body>为(lambda x (p x))。很明显，p是另一个函数，<body>是另一个函数定义。

#####函数调用
(<fun> <param>)
需要注意的是<fun>和<param>都应该是lambda演算的表达式，因此只能是一个符号、一个函数调用或是一个函数定义。如:<br>
(p x) 将实现一个函数调用。<br>
lambda演算的函数调用，完全就是通过`符号替换`实现的。符号替换满足alpha和beta约束，将在附录中介绍.

#####全局环境与绑定约束
按照lambda演算的标准，是没有环境这一概念的，也没有"="这样的语法。若按照lambda演算的标准语法来写，这样调试起来将会相当不便，因此这里引入了全局环境和绑定约束的概念。<br>
**约束绑定**:<br>
<KEY> = <LCExpression>
使用符号`=`, 可以将其左右两侧的数据绑定在一起。在运行该语句后，<KEY>的值将会被记录，当下次使用<KEY>时，会自动替换为<LCExpression>。如:<br>
```
1 = (lambda p (lambda x (p x)))
INC = (lambda n (lambda p (lambda x (p ((n p) x)))))
2 = (INC 1)
```
很明显，当约束绑定后，可以在lambda演算的符号中直接使用建立约束的符号。这是非常有利于记录数据的。需要注意的是，绑定是可以更改的，但不能在函数体中进行更改。**只能在最外层中进行约束的更改**，一方面可以保证程序的简单性，另一方面lambda演算本身就不能在函数体中有这样的操作。

**全局环境**:<br>
解释器维护了一个全局环境，解释器也只有这个单一环境。这个环境记录了所有的约束。

#####@IMPORT

#####@JAVA

#####@REDUCE

##附录

###Lambda Calculus
```
<exp> :=	Identify |
			λ.<exp> <exp> |
			(<exp> <exp>)
```

###BNF
BNF即巴科斯范式，以一种形式化的符号来描述给定语言的语法。