public class TreatmentRooms {

    private Semaphore semaphore;

    public TreatmentRooms() {
        semaphore = new Semaphore(2);
    }

    public boolean tryAcquire(long timeoutMillis)
            throws InterruptedException {

        return semaphore.tryAcquire(timeoutMillis);
    }

    public void release() {
        semaphore.V();
    }
}