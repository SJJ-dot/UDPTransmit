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
                    status.lazySet(ConnectState.CONNECT)
                    sendThread = thread {
                        while (status.get() != ConnectState.DISCONNECT) {
                            try {
                                conn.sendBuffer(queue.take())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    val readBuf = ByteArray(4096)
                    while (status.get() != ConnectState.DISCONNECT) {
                        val len = conn.readDataBlock(readBuf)
                        if (len > 0) {
                            publish.onNext(Arrays.copyOf(readBuf, len))
                        } else {
                            Thread.sleep(10)
                        }
                    }
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
        status.lazySet(ConnectState.DISCONNECT)
        connect?.interrupt()
        connect = null
    }

    fun push(byteArray: ByteArray) {
        if (status.get() != ConnectState.DISCONNECT) {
            queue.offer(byteArray)
        }
    }
}