package org.mastercs.bigdata.scala_lang

import scala.collection.mutable.ArrayBuffer

/**
 * Author: cyborg
 * Create: 2023/6/21 15:32
 */
object ScalaTrait {

    abstract class AbstractIntQueue {
        def get: Int

        def put(elem: Int): Unit
    }

    class BaseIntQueue extends AbstractIntQueue {

        private val buffer = new ArrayBuffer[Int]

        override def get: Int = buffer.remove(0)

        override def put(elem: Int): Unit = buffer.append(elem)
    }

    trait Doubling extends AbstractIntQueue {
        abstract override def put(elem: Int): Unit = super.put(2 * elem)
    }


    def main(args: Array[String]): Unit = {
        val queue = new BaseIntQueue
        queue.put(1)
        println(queue.get)

        val queue2 = new BaseIntQueue with Doubling
        queue2.put(1)
        println(queue2.get)
    }
}
