package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import Parser
import java.nio.charset.Charset


class CourseTorrentStaffTest {
    private val torrent = CourseTorrent()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()

    @Test
    fun `after load, infohash calculated correctly`() {
        println(debian)
       val metainfomap = Parser(debian).metaInfoMap



      //  val infohash = torrent.load(debian)
    //  println(metainfomap)

    }

    @Test
    fun `after load, announce is correct`() {
       // val infohash = torrent.load(debian)

       // val announces = torrent.announces(infohash)

       // assertThat(announces, allElements(hasSize(equalTo(1))))
       // assertThat(announces, hasSize(equalTo(1)))
       // assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }
}