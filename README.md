#EasyLC
一种简单的Lambda Calculus解释器Java实现。在支持Lambda Calculus的符号规则的前提下，有着易调试，易编写的特性。

##附录：Lambda Calcus的BNF
```
<exp> :=	Identify |
			λ.<exp> <exp> |
			(<exp> <exp>)
```