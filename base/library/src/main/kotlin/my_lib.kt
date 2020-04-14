import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write

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


        //if(readVal == null || readVal.equals(0))

        write(key, 0 as ByteArray)

    }
}


}