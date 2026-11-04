import java.util.Scanner;

public class hospital {

    public static synchronized void log(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Waiting room capacity: ");
        int capacity = in.nextInt();
        System.out.print("Number of doctors: ");
        int numofDoctors = in.nextInt();
        System.out.print("Number of patients: ");
        int numofPatients = in.nextInt();

        WaitingRoom waitingRoom = new WaitingRoom(capacity, numofPatients);
        TreatmentRooms treatingRooms = new TreatmentRooms();

        StringBuilder names = new StringBuilder();
        for (int i = 1; i <= numofPatients; i++) names.append("P").append(i).append(" ");
        log("Patients arriving:");
        log(names.toString().trim());

        Thread[] doctorThreads = new Thread[numofDoctors];
        for (int i = 0; i < numofDoctors; i++) {
            doctorThreads[i] = new Thread(new Doctor(i + 1, waitingRoom, treatingRooms));
            doctorThreads[i].start();
        }

        Patient[] patients = new Patient[numofPatients];
        for (int i = 0; i < numofPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
            patients[i].start();
        }

        for (Patient p : patients) p.join();
        for (Thread d : doctorThreads) d.join();

        log("All patients have been treated.");
        log("Simulation completed.");
    }
}
