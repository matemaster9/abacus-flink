package org.mastercs.bigdata.scala_lang.function_closure

/**
 * Author: cyborg
 * Create: 2023/6/21 10:15
 */
object ScalaClosure {

    def main(args: Array[String]): Unit = {
        // 定义自由变量：more
        var more = 100
        // 创建内部函数（闭包）：addMore
        val addMore = (arg: Int) => arg + more

        // 修改外部自由变量
        more = 200
        println(addMore(1)) // 输出201

        val incr1 = makeIncrFunction(1)
        val incr9 = makeIncrFunction(9)

        println(incr1(100)) // 101
        println(incr9(100)) // 109

    }


    def makeIncrFunction(more: Int): Int => Int = { (x: Int) => x + more }

}
