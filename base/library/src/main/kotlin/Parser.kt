
import java.util.*


class Parser public constructor(var id : Int) {



    private val str = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readText()



    fun read(): Any? {
        if (id >= str.length)
            return null



        val type: Char = str[id]
        ++id

        //integer i5e
        if (type == 'i') {
            var out: Long = 0
            val start: Int = id
            val limit: Int = id + 22
            var neg = false
            while (id <= limit) {
                val c: Char = str[id]
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
                if (str[id] == 'e') return out
                out.add(read())
                ++id
            }
        }



        // dictinary d key value key value e
        else if (type == 'd') {
            val out = LinkedHashMap<Any?, Any?>()
            while (true) {
                if (str[id] == 'e') return out
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
                val c: Char = str[id]
                if (c == ':') {
                    val out: String = str.substring(id + 1, id + len + 1)
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