
import java.util.*

//TODO add this
//      * @throws IllegalArgumentException If [str] is not a valid metainfo file.
class Parser public constructor( str : String) {

    private val torrentFileText = str

    private var id : Int = 0

    public val metaInfoMap  = read() as LinkedHashMap< String , Any?>;//has keys: info


    //private val str = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readText()


// TODO add this @throws IllegalArgumentException If [torrentFileText] is not a valid metainfo file.
    fun read(): Any? {
        if (id >= torrentFileText.length)
            return null



        val type: Char = torrentFileText[id]
        ++id

        //integer i5e
        if (type == 'i') {
            var out: Long = 0
            val start: Int = id
            val limit: Int = id + 22
            var neg = false
            while (id <= limit) {
                val c: Char = torrentFileText[id]
                if (id === start && c == '-') {
                    neg = true
                    ++id
                    continue
                }
                if (c == 'e') return if (neg) -out else out
                out = out * 10 + (c.toInt() - 48)
                ++id
            }
        }



        //list:  l element element element e
        else if (type == 'l') {
            val out = ArrayList<Any?>()
            while (true) {
                if (torrentFileText[id] == 'e') return out
                out.add(read())
                ++id
            }
        }



        // dictinary d key value key value e
        else if (type == 'd') {

            val out = LinkedHashMap<Any?, Any?>()
            while (true) {
                if (torrentFileText[id] == 'e') return out
                val key = read()
                ++id
                val value = read()
                out[key] = value
                ++id
            }

        }

        //string 5:hihih
        else if (type >= '0' && type <= '9') {
            var len = type.toInt() - 48
            val limit: Int = id + 11
            while (id <= limit) {
                val c: Char = torrentFileText[id]
                if (c == ':') {
                    val out: String = torrentFileText.substring(id + 1, id + len + 1)
                    id += len
                    return out
                }
                len = len * 10 + (c.toInt() - 48)
                ++id
            }
        }
        return null
    }

}