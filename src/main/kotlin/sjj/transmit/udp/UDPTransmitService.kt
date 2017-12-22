package sjj.transmit.udp

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

class UDPTransmitService {
    val publish = PublishProcessor.create<ByteArray>().toSerialized()
    private val conn: ConnectInterface = UDPConnection()
    private var status = AtomicReference<ConnectState>(ConnectState.DISCONNECT)
    private val queue = LinkedBlockingQueue<ByteArray>()
    private var connect: Thread? = null
    fun connect() {
        if (status.compareAndSet(ConnectState.DISCONNECT, ConnectState.CONNECTING)) {
            connect?.interrupt()
            connect = thread {
                var sendThread: Thread? = null
                try {
                    conn.openConnection()
                    sendThread = thread {
                        while (true) {
                            try {
                                conn.sendBuffer(queue.take())
                                Thread.sleep(10)
                            } catch (e: Exception) {
                            }
                        }
                    }
                    val readBuf = ByteArray(4096)
                    while (true) {
                        val len = conn.readDataBlock(readBuf)
                        if (len > 0) {
                            publish.onNext(Arrays.copyOf(readBuf, len))
                        }
                    }
                } catch (e: Exception) {

                } finally {
                    sendThread?.interrupt()
                    disconnect()
                }
            }
        }
    }

    fun disconnect() {
        if (status.get() == ConnectState.DISCONNECT) {
            return
        }
        connect?.interrupt()
        connect = null
    }

    fun push(byteArray: ByteArray) {
        if (status.get() == ConnectState.DISCONNECT) {
            queue.offer(byteArray)
        }
    }
}