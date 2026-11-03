public class Patient extends Thread {

    private int id;
    private WaitingRoom waitingRoom;
    private boolean treated = false;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public int getPatientId() {
        return id;
    }

    @Override
    public void run() {

        hospital.log("P" + id + " arrived.");

        waitingRoom.enter(this);

        synchronized (this) {
            while (!treated) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        hospital.log("P" + id + " left.");
    }

    public synchronized void finishTreatment() {
        treated = true;
        notifyAll();
    }
}