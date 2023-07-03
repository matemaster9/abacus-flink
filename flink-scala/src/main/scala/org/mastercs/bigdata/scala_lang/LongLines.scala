package org.mastercs.bigdata.scala_lang

import java.nio.charset.StandardCharsets
import scala.io.Source

/**
 * Author: cyborg
 * Create: 2023/6/20 16:33
 */
object LongLines {

    def main(args: Array[String]): Unit = {
        processFile("LICENSE", 20)
    }

    def processFile(filename: String, width: Int): Unit = {
        val source = Source.fromFile(filename, StandardCharsets.UTF_8.name())
        for (line <- source.getLines()) {
            processLine(filename, width, line)
        }
    }

    private def processLine(filename: String, width: Int, line: String): Unit = {
        if (line.length > width) {
            println(filename + ": " + line.trim)
        }
    }


    
}
