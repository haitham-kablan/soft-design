package il.ac.technion.cs.softwaredesign


import my_lib
import Parser
import java.util.ArrayList
import java.util.LinkedHashMap


/**
 * This is the class implementing CourseTorrent, a BitTorrent client.
 *
 * Currently specified:
 * + Parsing torrent metainfo files (".torrent" files)
 */
class CourseTorrent {








    /**
     * Load in the torrent metainfo file from [torrent]. The specification for these files can be found here:
     * [Metainfo File Structure](https://wiki.theory.org/index.php/BitTorrentSpecification#Metainfo_File_Structure).
     *
     * After loading a torrent, it will be available in the system, and queries on it will succeed.
     *
     * This is a *create* command.
     *
     * @throws IllegalArgumentException If [torrent] is not a valid metainfo file.
     * @throws IllegalStateException If the infohash of [torrent] is already loaded.
     * @return The infohash of the torrent, i.e., the SHA-1 of the `info` key of [torrent].
     */
    fun load(torrent: String): String {


        val metainfomap = Parser(torrent).metaInfoMap

        val infohash = metainfomap.get("info")
        //TODO: convert infohash from info dictinary to infohash

        val readVal = my_lib.lib_read(infohash as ByteArray)
        if (readVal != null && !readVal.equals(0))//already exists and not deleted
                throw IllegalStateException()

        val announce = metainfomap.get("announce-list")

        if(announce == null){
            val list = ArrayList<String>()
            list.add( metainfomap.get("announce") as String)
            val list2 = ArrayList<ArrayList<String>>()
            list2.add(list)
            my_lib.lib_write(infohash, list2 as ByteArray)
        }
        else
            my_lib.lib_write(infohash, announce as ByteArray)

        return infohash as String

    }

    /**
     * Remove the torrent identified by [infohash] from the system.
     *
     * This is a *delete* command.
     *
     * @throws IllegalArgumentException If [infohash] is not loaded.
     */
    fun unload(infohash: String): Unit {

        val readVal = my_lib.lib_read(infohash as ByteArray);
        if(readVal == null || readVal.equals(0))
            throw IllegalArgumentException()

        my_lib.lib_delete(infohash as ByteArray)
    }




    /**
     * Return the announce URLs for the loaded torrent identified by [infohash].
     *
     * See [BEP 12](http://bittorrent.org/beps/bep_0012.html) for more information. This method behaves as follows:
     * * If the "announce-list" key exists, it will be used as the source for announce URLs.
     * * If "announce-list" does not exist, "announce" will be used, and the URL it contains will be in tier 1.
     * * The announce URLs should *not* be shuffled.
     *
     * This is a *read* command.
     *
     * @throws IllegalArgumentException If [infohash] is not loaded.
     * @return Tier lists of announce URLs.
     */
    fun announces(infohash: String): List<List<String>> {

        val readVal = my_lib.lib_read(infohash as ByteArray);
        if(readVal == null || readVal.equals(0))
            throw IllegalArgumentException()
        return readVal as List<List<String>>
    }
}