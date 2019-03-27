import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter {
    private final double maxPermits;
    private final double refillRate;
    private double storedPermits;
    private long lastRequest;

    public SlidingWindowRateLimiter(double maxPermits, long window, TimeUnit timeUnit) {
        this.lastRequest = System.nanoTime();
        this.maxPermits = maxPermits;
        this.refillRate = timeUnit.toNanos(window)/maxPermits;
        this.storedPermits = maxPermits;
    }

    public void acquire() {
        acquire(1);
    }

    public synchronized void acquire(double permits) {

        long elapsedNanos = System.nanoTime() - lastRequest;
        double newPermits = Math.floor(elapsedNanos/refillRate);

        if (newPermits + storedPermits < maxPermits) {
            storedPermits += newPermits;
        } else {
            storedPermits = maxPermits;
        }

        double missingPermits = permits - storedPermits;

        if (missingPermits <= 0D) {
            storedPermits -= permits;
        } else {
            sleepNanos((long) (refillRate*missingPermits));
            storedPermits = 0D;
            lastRequest = System.nanoTime();
        }
    }

    private void sleepNanos(long nanos) {
        if (nanos > 0L) {
            boolean interrupted = false;

            try {
                long end = System.nanoTime() + nanos;

                while(true) {
                    try {
                        TimeUnit.NANOSECONDS.sleep(nanos);
                        return;
                    } catch (InterruptedException var12) {
                        interrupted = true;
                        nanos = end - System.nanoTime();
                    }
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
