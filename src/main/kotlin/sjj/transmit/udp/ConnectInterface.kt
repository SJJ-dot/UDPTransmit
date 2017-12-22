package sjj.transmit.udp

/**
 * Created by sjj on 2017/12/8.
 */
interface ConnectInterface {

    fun openConnection()

    fun readDataBlock(buffer: ByteArray): Int

    fun sendBuffer(buffer: ByteArray)

    fun closeConnection()

}