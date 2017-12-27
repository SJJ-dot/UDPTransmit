package sjj.transmit.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by sjj on 2017/5/25.
 */

public class Timer {
    private final Runnable runnable;
    private final long period;
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> schedule;

    public Timer(Runnable runnable) {
        this(runnable, -1);
    }

    public Timer(Runnable runnable , long period) {
        this.runnable = runnable;
        this.period = period;
    }

    public synchronized void start(long initialDelay) {
        stop();
        if (period > 0) {
            start(initialDelay, period);
        } else {
            schedule = service.schedule(runnable, initialDelay, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void start(long initialDelay, long delay) {
        stop();
        schedule = service.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public synchronized void stop() {
        ScheduledFuture<?> schedule = this.schedule;
        if (schedule != null) {
            schedule.cancel(true);
        }
        this.schedule = null;
    }
}
