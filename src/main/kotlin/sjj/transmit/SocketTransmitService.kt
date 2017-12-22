package sjj.transmit

import io.reactivex.Flowable

class SocketTransmitService(private val flowable: Flowable<ByteArray>) {
    init {
        flowable.subscribe()
    }
}