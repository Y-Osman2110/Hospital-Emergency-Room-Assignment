public class Semaphore {
    private int value;

    // Set how many permits we start with.
    public Semaphore(int initial) {
        if (initial < 0) {
            throw new IllegalArgumentException("Initial permits cannot be negative");
        }
        value = initial;
    }

    // Take a permit, or wait if none is free.
    public synchronized void P() throws InterruptedException {
        while (value == 0) {
            wait();
        }
        value--;
    }

    // Give back a permit and wake up waiting threads.
    public synchronized void V() {
        value++;
        notifyAll();
    }

    // Try to get a permit before the time runs out.
    public synchronized boolean tryAcquire(long timeoutMillis)
            throws InterruptedException {
        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }

        long remaining = timeoutMillis;
        long deadline = System.nanoTime() + timeoutMillis * 1000000L;

        while (value == 0) {
            if (remaining <= 0) {
                return false;
            }

            wait(remaining);

            // Check how much time is left.
            remaining = (deadline - System.nanoTime()) / 1000000L;
        }

        value--;
        return true;
    }
}
