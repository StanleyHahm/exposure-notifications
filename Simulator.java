/*
 * This file contains the Simulator class for PA6. It runs a simple Exposure
 * Notification System simulation based on the system described here:
 * https://www.google.com/covid19/exposurenotifications/
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 * The Simulator class simulates students' movements and testing using the
 * exposure notification system implemented in Student/Server/ContactInfo.
 * Contains a main method that will run simulation using an input file.
 */
public class Simulator {
    /* INFORMATION PRINTOUT STRINGS */
    private static final String PRINT_DELIMITER = ", ";
    private static final String ONE_DAY = "Simulating day %d:\n";
    private static final String ONE_DAY_RESULT = "\tNotifies %d students.\n";
    private static final String QUARANTINE_PROMPT = "quarantineChoices: ";
    private static final String LOCATION_PROMPT = "\tlocations: ";
    private static final String INFECTION_PROMPT = "\tinfections: ";
    private static final String SERVER_IDS_PROMPT =
                                               "\tInfected IDs in server: ";
    private static final String STUDENT_PROMPT = "\tStudents: ";
    private static final String STUDENT_FORMAT = "(%d, %b, %b)";
    private static final String FINAL_RESULT_STR =
                            "Simulated %d days and notified %d students.\n";

    /* OTHER CONSTANTS */
    private static final int RISK_CHECK_DURATION = 14;
    private static final int BLUETOOTH_DIS_THRESHOLD = 5;
    private static final String FILE_NAME = "sample.txt";
    private static final String DELIMITER_STR = ",";

    /* INSTANCE VARIABLES */
    public ArrayList<Student> students;
    public Server server;

    /**
     * Construct a Simulator object by instantiating {@code server} and
     * {@code students}.
     *
     * @param num the number of students in this simulation
     */
    public Simulator(int num) {
        students = new ArrayList<Student>(num);
        for(int i = 0; i < num; i++) students.add(new Student());
        server = new Server();
    }


    /**
     * You need to figure out what this method does.
     *
     * @param list the list of integer to check validity for
     * @return You need to figure out what this method returns
     */
    private boolean isValidList(ArrayList<Integer> list) {
        return (list != null) && (list.size() == students.size());
    }

    /**
    * Update the random ID for all students in {@code students}
    */
    public void updateIds() {
        for(Student student: students) student.updateId();
    }

    /**
     * You need to figure out what this method does.
     *
     * @param locations a list parallel to {@code students} that denotes each
     *                  student's new location.
     * @return You need to figure out what this method returns
     */
    public boolean updateLocations(ArrayList<Integer> locations) {
        if(!isValidList(locations)) return false;

        for(int i = 0; i < students.size(); i++) {
            students.get(i).setLocation(locations.get(i));
        }
        return true;
    }

    /**
     * You need to figure out what this method does.
     *
     * @param infections a list parallel to {@code students} that denotes each
     *                   student's new infection status.
     * @return You need to figure out what this method returns
     */
    public boolean updateInfectionStatus(ArrayList<Integer> infections) {
        if(!isValidList(infections)) return false;

        for(int i = 0; i < students.size(); i++) {
            if(infections.get(i) == 1 && (!students.get(i).covidPositive)) {
                students.get(i).testPositive(server);
            }
        }
        return true;
    }

    /**
     * You need to figure out what this method does.
     *
     * @param quarantineChoices a list parallel to {@code students} that
     *                          denotes whether each student would quarantine
     *                          if they receive a notification of exposure.
     * @param fromTime the starting time that students should check risk from
     * @return You need to figure out what this method returns
     */
    public int riskCheckAll(ArrayList<Integer> quarantineChoices,
                              int fromTime) {
        if(!isValidList(quarantineChoices)) return -1;
        int count = 0;
        for(int i = 0; i < students.size(); i++) {
            boolean choice = quarantineChoices.get(i) == 1;
            if(students.get(i).riskCheck(server, fromTime, choice) == 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * You need to figure out what this method does.
     *
     * @param student1 the first student to exchange information. If it is
     *                 null, nothing will be exchanged.
     * @param student2 the second student to exchange information. If it is
     *                 null, nothing will be exchanged.
     * @return true if two students exchanged information successfully
    *          false if (you need to figure out the condition)
     */
    public boolean exchangeInfo(Student student1, Student student2,
                                  int currentTime) {
        if(student1 == null || student2 == null) return false;
        if(student1.inQuarantine || student2.inQuarantine) return false;

        int distance = Math.abs(student1.location - student2.location);
        if(distance >= BLUETOOTH_DIS_THRESHOLD) return false;
        ContactInfo student1Info = new ContactInfo(student1.id,
                                                    distance, currentTime);
        ContactInfo student2Info = new ContactInfo(student2.id,
                                                    distance, currentTime);
        return student1.addContactInfo(student2Info)
               && student2.addContactInfo(student1Info);
    }

    /**
     * Simulate a single day of moving, infecting, and notifying.
     *
     * @param locations a list parallel to {@code students} that denotes each
     *                  student's new location.
     * @param infections a list parallel to {@code students} that denotes each
     *                   student's new infection status.
     * @param quarantineChoices a list parallel to {@code students} that
     *                          denotes whether each student would quarantine
     *                          if they receive a notification of exposure.
     * @param currentTime the current day.
     * @return you need to figure this out
     */
    public int simulateOneDay(ArrayList<Integer> locations,
                                ArrayList<Integer> infections,
                                ArrayList<Integer> quarantineChoices,
                                int currentTime) {

        // Update information
        updateIds();
        updateLocations(locations);
        updateInfectionStatus(infections);

        // Simulate information exchange
        for(int i = 0; i < students.size(); i++) {
            for(int j = i+1; j < students.size(); j++) {
                exchangeInfo(students.get(i), students.get(j), currentTime);
            }
        }

        int fromTime = currentTime - RISK_CHECK_DURATION;
        fromTime = fromTime > 0 ? fromTime : 0; // use 0 uf fromTime negative
        return riskCheckAll(quarantineChoices, fromTime);
    }

    /**
     * You need to figure out what this method does.
     *
     * @param args the command line arguments
     * @throws IOException you need to figure out when this will be thrown
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File(FILE_NAME));

        // Try to read the quarantine choices
        if(!sc.hasNext()) return;
        ArrayList<Integer> quarantineChoices = processIntLine(sc.nextLine());
        printList(QUARANTINE_PROMPT, quarantineChoices);

        // Create a simulator
        Simulator simulator = new Simulator(quarantineChoices.size());
        int time = 0;
        int totalNumOfNotifications = 0;

        // Execute simulation until reach the end of the file
        while(sc.hasNext()) {
            // Read the locations line
            ArrayList<Integer> locations = processIntLine(sc.nextLine());
            // Try to read the infections line.
            // If no more lines, end the simulation
            if(!sc.hasNext()) break;
            ArrayList<Integer> infections = processIntLine(sc.nextLine());

            // Simulate and record result
            int num = simulator.simulateOneDay(locations, infections,
                                                quarantineChoices, time);
            if(num != -1) totalNumOfNotifications += num;

            // Printing information for the current day
            System.out.printf(ONE_DAY, time);
            printList(LOCATION_PROMPT, locations);
            printList(INFECTION_PROMPT, infections);
            printList(SERVER_IDS_PROMPT, simulator.server.infectedIds);
            printStudents(STUDENT_PROMPT, simulator.students);
            System.out.printf(ONE_DAY_RESULT, num);

            time++;
        }
        System.out.printf(FINAL_RESULT_STR, time, totalNumOfNotifications);
        sc.close();
    }

     /**
     * Processes a single line of ints from the information file into a list
     * of integers.
     *
     * @param line the comma-delimited list of ints from the information file
     * @return the list of ints
     */
    private static ArrayList<Integer> processIntLine(String line) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String[] tokens = line.split(DELIMITER_STR);
        for(String token: tokens) result.add(Integer.parseInt(token));
        return result;
    }

    /**
     * Prints a list of integers to standard out, prepended with some input
     * information.
     *
     * @param prompt a string to be prepended to the list of integers (e.g.,
     *               as a description).
     * @param list the integers to be printed.
     */
    private static void printList(String prompt, ArrayList<Integer> list) {
        System.out.print(prompt);
        for(Integer num: list) System.out.print(num + PRINT_DELIMITER);
        System.out.println();
    }
    /**
     * Prints a list of students to standard out, prepended with some input
     * information.
     *
     * @param prompt a string to be prepended to the list of students (e.g.,
     *               as a description).
     * @param students the students to be printed.
     */
    private static void printStudents(String prompt,
                                        ArrayList<Student> students) {
        System.out.print(prompt);
        for(Student student: students) {
            System.out.printf(
                String.format(STUDENT_FORMAT, student.location,
                    student.covidPositive, student.inQuarantine)
                + PRINT_DELIMITER
            );
        }
        System.out.println();
    }
}
