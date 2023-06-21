package org.mastercs.bigdata.scala_lang.function_closure

import scala.io.Source

/**
 * Author: cyborg
 * Create: 2023/6/20 19:07
 */
object ScalaFunction {

    // 一等函数的简写形式
    private val abridge: (Int, Int) => Int = _ + _
    private val abridge2 = (_: Int) + (_: Int)


    def sum(x: Int, y: Int): Int = {
        x + y;
    }

    def main(args: Array[String]): Unit = {

        // 部分应用
        val applyParting = sum(_, 2)
        println(applyParting(1))

        // 面向对象使用方法
        OOPLongLines.processFile("LICENSE", 10)

        // 支持一等函数
        FunctionalLongLines.processFile("LICENSE", 10)

        // 自由变量
        var freeVariable = 10

        val freeVariableFunction = (x: Int) => x + freeVariable
        println(freeVariableFunction(12))
        println(freeVariableFunction(11))

        // 重复参数
        printArguments("scala", "lang")

        // 默认参数，带名称参数
        logout(username = "sys")
    }

    /**
     * 支持重复参数
     *
     * @param args \
     */
    def printArguments(args: String*): Unit = {
        args.foreach(print)
    }

    /**
     * 支持默认参数
     *
     * @param username \
     */
    def logout(username: String = "admin"): Unit = {
        // code process logout
    }

    // 面向对象实习
    object OOPLongLines {
        def processFile(filename: String, width: Int): Unit = {
            val source = Source.fromFile(filename)
            for (elem <- source.getLines()) {
                processLines(filename, width, elem)
            }
        }

        private def processLines(filename: String, width: Int, line: String): Unit = {
            if (line.length > width) {
                println(filename + ":" + line.trim)
            }
        }
    }

    // 函数式实现
    object FunctionalLongLines {
        def processFile(filename: String, width: Int): Unit = {
            def processLines(line: String): Unit = {
                if (line.length > width) {
                    println(filename + ":" + line.trim)
                }
            }

            val source = Source.fromFile(filename)
            for (elem <- source.getLines()) {
                processLines(elem)
            }
        }
    }
}
