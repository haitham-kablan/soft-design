import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

/**
 * @throws IllegalArgumentException If [str] is not a valid metainfo file.
 */
class Parser public constructor(str : ByteArray) {


    private val torrentFileText = str

    private var pieces_flag: Boolean = false

    private var info_start = 0
    private var info_end = 0
    private var pieces_start = 0
    private var pieces_end = 0
    private var id : Int = 0

    public val metaInfoMap  = read() as LinkedHashMap< String , Any?>




    public val infohash :String

    init {
        val str1 = (torrentFileText.copyOfRange(info_start,pieces_start))
            .toString(kotlin.text.Charsets.UTF_8)

        val str2 = (torrentFileText.copyOfRange(pieces_start,pieces_end+1 ))


        val str3 = (torrentFileText.copyOfRange(pieces_end + 1, info_end))
            .toString(kotlin.text.Charsets.UTF_8)

        infohash = SHA1hash(str1,str2,str3)
    }



    private fun SHA1hash(str1 : String, str2 :ByteArray , str3 :String) : String{

        val HEX_CHARS = "0123456789abcdef"
        val bytes = MessageDigest
            .getInstance("SHA-1")
            .digest(str1.toByteArray() + str2 + str3.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }


    /**
     * @throws IllegalArgumentException If [torrentFileText] is not a valid metainfo file.
     */
    private fun read(): Any? {
        if (id >= torrentFileText.size) {
            throw IllegalArgumentException()
        }



        val type: Char = torrentFileText[id].toChar()
        ++id

        //integer example : i5e
        //returns Int
        if (type == 'i') {
            var out: Int = 0
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
        //returns ArrayList
        else if (type == 'l') {
            val out = ArrayList<Any?>()
            while (true) {
                if (torrentFileText[id].toChar() == 'e') return out
                out.add(read())
                ++id
            }
        }



        // dictinary d key value key value e
        //Returns LinkedHashMap
        else if (type == 'd') {

            val out = LinkedHashMap<Any?, Any?>()
            while (true) {
                if (torrentFileText[id].toChar() == 'e') return out
                val key = read()
                ++id

                if(key == "pieces")
                    pieces_start = id
                if(key == "info")
                    info_start = id

                val value = read()

                if(key  == "pieces")
                    pieces_end = id
                if(key == "info")
                    info_end = id + 1

                out[key] = value
                ++id
            }

        }

        //string example :  5:hihih
        //returns String or ByteArray
        else if (type in '0'..'9') {
            var len = type.toInt() - 48
            val limit: Int = id + 11
            while (id <= limit) {
                val c: Char = torrentFileText[id].toChar()
                if (c == ':') {

                    if (id in pieces_start..pieces_end)//get out as raw bytes
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
        throw IllegalArgumentException()
    }

}