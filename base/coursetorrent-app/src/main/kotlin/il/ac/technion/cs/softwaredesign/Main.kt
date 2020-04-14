package il.ac.technion.cs.softwaredesign

import Parser


fun main() {
    println("hello, world")


   // val debian = getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readText()

    val parser = Parser(0)

    val infoKey = parser.read()

    println(infoKey)
}