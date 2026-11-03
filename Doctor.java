import java.util.Random;

public class Doctor implements Runnable {
    private int id;
    private String name;
    private boolean idle = true;
    private WaitingRoom waitingRoom;
    private Semaphore treatmentRooms;
    private Random random = new Random();

    public Doctor(int id, WaitingRoom waitingRoom, Semaphore treatmentRooms) {
        this.id = id;
        this.name = "Doctor " + id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    @Override
    public void run() {
        while (!waitingRoom.allPatientsDone()) {

            Patient patient = waitingRoom.take();
            idle = false;

            System.out.println(name + " is assigned to " + patient.getName());

            System.out.println(name + " is trying to acquire a treatment room");
            boolean acquired = treatmentRooms.tryAcquire(3000);

            if (acquired) {
                try {

                    System.out.println(name + " acquired a treatment room");
                    System.out.println(name + " begins treating " + patient.getName());
                    Thread.sleep(random.nextInt(5000) + 1000);
                    System.out.println(name + " finished treating " + patient.getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    treatmentRooms.release();
                    System.out.println(name + " released the treatment room");
                }
            } else {

                System.out.println(name + " could not acquire a treatment room");
                System.out.println(name + " is sending " + patient.getName()
                        + " back to the waiting room");
                waitingRoom.putBack(patient);
            }

            idle = true;
            System.out.println(name + " is now available");
        }
    }

    public boolean isIdle() {
        return idle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}