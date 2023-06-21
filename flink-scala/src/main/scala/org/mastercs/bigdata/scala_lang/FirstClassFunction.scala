package org.mastercs.bigdata.scala_lang

/**
 * Author: cyborg
 * Create: 2023/6/20 17:17
 */
object FirstClassFunction {

    private val incr = (x: Int) => x + 1

    private val abridgeFunction = (_: Int) + (_: Int)

    object Increase {
        def one(number: Int): Int = {
            number + 1;
        }

        def ten(number: Int): Int = {
            number + 10
        }
    }


    def main(args: Array[String]): Unit = {
        println(Increase.one(10))
        println(Increase.ten(10))
        println(incr(1))
        println(incr)

        // 一等函数：foreach函数做参数  Int => Unit
        val ints = List.range(1, 9)
        ints.foreach(elem => print(elem))

        // 函数简写形式
        ints.filter(number => number > 0)
        ints.filter((number) => number > 0)

        // 占位符
        println(ints.exists(_ % 2 == 0))

        // 函数简写
        println(abridgeFunction(5, 10))
    }
}
