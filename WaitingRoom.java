import java.util.Queue;
import java.util.ArrayDeque;

public class WaitingRoom {

    private Queue<Patient> patients = new ArrayDeque<>();
    private int capacity;
    private int totalPatients;
    private int completedPatients = 0;

    public WaitingRoom(int capacity, int totalPatients) {

        if (capacity <= 0 || totalPatients < 0) {
            throw new IllegalArgumentException();
        }

        this.capacity = capacity;
        this.totalPatients = totalPatients;
    }

    // Patient enters the waiting room
    public synchronized void enter(Patient p)
            throws InterruptedException {

        while (patients.size() >= capacity) {
            wait();
        }

        patients.add(p);
        notifyAll();
    }

    // Doctor takes the next patient
    public synchronized Patient take()
            throws InterruptedException {

        while (patients.isEmpty()
                && completedPatients < totalPatients) {
            wait();
        }

        if (patients.isEmpty()) {
            return null;
        }

        Patient p = patients.remove();

        notifyAll();

        return p;
    }

    // Doctor returns a patient
    public synchronized void putBack(Patient p)
            throws InterruptedException {

        while (patients.size() >= capacity) {
            wait();
        }

        patients.add(p);
        notifyAll();
    }

    // Record a successfully treated patient
    public synchronized void patientDone() {

        completedPatients++;

        notifyAll();
    }

    // Check whether all patients have finished
    public synchronized boolean allPatientsDone() {

        return completedPatients == totalPatients
                && patients.isEmpty();
    }
}