import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write


public fun listOfListOfStringToByteArray( list : List<List<String>>) : ByteArray {

    var size = 2 + 2*list.size //2 for l and e of the main list, and another 2 for every secondary list in it

    list.forEach {
        it.forEach {
            val len = it.length
            size += len.toString().length + 1 // space it takes to save the length and then the ':'

            size += len  //the length of the string, and 1 for :

        }
    }

    val out = ByteArray(size)
    var i : Int = 0

    out[0] = 'l'.toByte()
    ++i
    list.forEach {

        out[i] = 'l'.toByte()
        ++i
        it.forEach {

            val len = it.length

            len.toString().toByteArray().copyInto(out , i )
            i+=len.toString().length

            out[i] = ':'.toByte()
            ++i

            it.toByteArray().copyInto(out,i)
            i+=len
        }
        out[i] = 'e'.toByte()
        ++i
    }
    out[i] = 'e'.toByte()
    ++i

    return out
}



//assumes the form of list is := "ll4:name5helloel5:name26hello2ee   =>
//                                               <
//                                                  <name,hello>
//                                                  <name2,hello2>
//if not, @throws IllegalArgumentException                                                                  >
public fun byteArrayToListOfListOfString(byteArray : ByteArray) : List<List<String>> {

    if(byteArray[0] != 'l'.toByte())
        throw IllegalArgumentException()

    var outerList = ArrayList<ArrayList<String>>()
    var i = 1


    while (byteArray[i] != 'e'.toByte()){

        if(byteArray[i] != 'l'.toByte())
            throw IllegalArgumentException()

        var innerList = ArrayList<String>()
        ++i

        while (byteArray[i] != 'e'.toByte()){

            var j = i
            while (byteArray[j] != ':'.toByte()){
                j++
            }

            val lengthOfString = ByteArray(j-i).toString(Charsets.UTF_8).toInt()
            i=j+1
            val strAsBytes = ByteArray(lengthOfString)
            byteArray.copyInto(strAsBytes,0, i ,i+lengthOfString)

            innerList.add(strAsBytes.toString(Charsets.UTF_8))
        }
        outerList.add(innerList)
    }

    return outerList
}



class my_lib() {

    companion object{

        public fun lib_read(key: ByteArray ) :ByteArray?{
            return read(key);

        }

        public fun lib_write(key: ByteArray, value: ByteArray) : Unit{

            write(key,value);
        }



        // @throws IllegalArgumentException If [infohash] is not loaded.
        public fun lib_delete(key: ByteArray) : Unit {

            write(key, 0 as ByteArray)

        }





    }


}