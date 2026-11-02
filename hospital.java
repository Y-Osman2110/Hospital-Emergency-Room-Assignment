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

        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms treatingRooms = new TreatmentRooms(2);

        StringBuilder names = new StringBuilder();
        for (int i = 1; i <= numofPatients; i++)
            names.append("P").append(i).append(" ");
        log("Patients arriving:");
        log(names.toString().trim());

        Doctor[] doctors = new Doctor[numofDoctors];
        for (int i = 0; i < numofDoctors; i++) {
            doctors[i] = new Doctor(i + 1, waitingRoom, treatingRooms);
            doctors[i].start();
        }

        Patient[] patients = new Patient[numofPatients];
        for (int i = 0; i < numofPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
            patients[i].start();
        }

        // wait until every patient has left
        for (Patient p : patients)
            p.join();

        // all patients are done, so wake the idle doctors with an interrupt
        for (Doctor d : doctors)
            d.interrupt();

        // wait until every doctor thread has exited
        for (Doctor d : doctors)
            d.join();

        log("All patients have been treated.");
        log("Simulation completed.");
    }
}