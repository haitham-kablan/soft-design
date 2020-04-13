import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write

class my_lib() {


    public fun lib_read(key: ByteArray ) :ByteArray?{
        return read(key);

    }

    public fun lib_write(key: ByteArray, value: ByteArray) : Unit{

        write(key,value);
    }

}