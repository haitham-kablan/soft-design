import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

//TODO add this
//      * @throws IllegalArgumentException If [str] is not a valid metainfo file.

class Parser public constructor(str : ByteArray) {

    private var pieces_flag: Boolean = false
    private val torrentFileText = str
    private var info_start = 0
    private var info_end = 0
    private var id : Int = 0

    public val metaInfoMap  = read() as LinkedHashMap< String , Any?>


//    public val info_str = (torrentFileText.copyOfRange(info_start,info_end))
//        .toString(Charsets.UTF_8)
//
//    public val infohash = SHA1hash(info_str)
//
//    private fun SHA1hash(input : String) : String{
//
//        val HEX_CHARS = "0123456789ABCDEF"
//        val bytes = MessageDigest
//            .getInstance("SHA-1")
//            .digest(input.toByteArray())
//        val result = StringBuilder(bytes.size * 2)
//
//        bytes.forEach {
//            val i = it.toInt()
//            result.append(HEX_CHARS[i shr 4 and 0x0f])
//            result.append(HEX_CHARS[i and 0x0f])
//        }
//
//        return result.toString()
//    }


    // TODO add this @throws IllegalArgumentException If [torrentFileText] is not a valid metainfo file.
    private fun read(): Any? {
        if (id >= torrentFileText.size) {
            return null
        }



        val type: Char = torrentFileText[id].toChar()
        ++id

        //integer example : i5e
        if (type == 'i') {
            var out: Long = 0
            val start: Int = id
            val limit: Int = id + 22
            var neg = false
            while (id <= limit) {
                val c: Char = torrentFileText[id].toChar()
                if (id == start && c == '-') {
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
                if (torrentFileText[id].toChar() == 'e') return out
                out.add(read())
                ++id
            }
        }



        // dictinary d key value key value e
        else if (type == 'd') {

            val out = LinkedHashMap<Any?, Any?>()
            while (true) {
                if (torrentFileText[id].toChar() == 'e') return out
                val key = read()
                ++id
                if(key == "pieces")
                    pieces_flag = true
                if(key == "info")
                    info_start = id
                val value = read()
                if(key  == "pieces")
                    pieces_flag = false
                if(key == "info")
                    info_end = id + 1
                out[key] = value
                ++id
            }

        }

        //string example :  5:hihih
        else if (type >= '0' && type <= '9') {
            var len = type.toInt() - 48
            val limit: Int = id + 11
            while (id <= limit) {
                val c: Char = torrentFileText[id].toChar()
                if (c == ':') {

                    if (pieces_flag)//get out as raw bytes
                    {

                        // val out: String = torrentFileText.toByteArray().decodeToString(id+1,id+len + 1,false)//.substring(id + 1, id  + 11)

                        val out = torrentFileText.copyOfRange(id+1,id+len+1)
                        id +=  len
                        return out
                    }
                    else { //get out as utf8(normal) string

                        val out: String = torrentFileText.copyOfRange(id + 1, id + len + 1).toString(Charsets.UTF_8)
                        id += len
                        return out
                    }
                }
                len = len * 10 + (c.toInt() - 48)
                ++id
            }
        }
        return null
    }

}