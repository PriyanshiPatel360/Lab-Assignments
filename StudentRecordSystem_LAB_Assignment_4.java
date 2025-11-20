import java.io.*;
import java.util.*;

class Student {

    private int rollNo;
    private String name;
    private String email;
    private String course;
    private double marks;

    public Student(int rollNo, String name, String email, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }
    public double getMarks() { return marks; }

    public String toFileString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    public void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("------------------------------");
    }
}

// Comparator Sorting by Marks
class SortByMarks implements Comparator<Student> {
    public int compare(Student a, Student b) {
        return Double.compare(b.getMarks(), a.getMarks());
    }
}

public class StudentRecordSystem_LAB_Assignment_4 {

    private static final String FILE_NAME = "students.txt";
    private ArrayList<Student> list = new ArrayList<>();

    public StudentRecordSystem_LAB_Assignment_4() {
        loadFromFile();
    }

    // Load student records from file
    private void loadFromFile() {
        File f = new File(FILE_NAME);
        try {
            if (!f.exists()) {
                f.createNewFile();
                return;
            }

            System.out.println("File Size: " + f.length() + " bytes");
            System.out.println("Readable: " + f.canRead());
            System.out.println("Writable: " + f.canWrite());

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 5) {
                    int r = Integer.parseInt(p[0]);
                    String n = p[1];
                    String e = p[2];
                    String c = p[3];
                    double m = Double.parseDouble(p[4]);
                    list.add(new Student(r, n, e, c, m));
                }
            }
            br.close();

            System.out.println("\nLoaded students from file:");
            for (Student s : list) {
                s.display();
            }

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }

    // Save to file
    private void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Student s : list) {
                bw.write(s.toFileString());
                bw.newLine();
            }
            bw.close();
            System.out.println("Records saved to file.");

        } catch (Exception e) {
            System.out.println("Error saving to file.");
        }
    }

    // Add student
    private void addStudent(Scanner sc) {

        try {
            System.out.print("Enter Roll No: ");
            int r = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Name: ");
            String n = sc.nextLine();

            System.out.print("Enter Email: ");
            String e = sc.nextLine();

            System.out.print("Enter Course: ");
            String c = sc.nextLine();

            System.out.print("Enter Marks: ");
            double m = Double.parseDouble(sc.nextLine());

            Student s = new Student(r, n, e, c, m);
            list.add(s);

            System.out.println("Student added.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    // View all students using Iterator
    private void viewAll() {
        if (list.isEmpty()) {
            System.out.println("No records available.");
            return;
        }

        Iterator<Student> it = list.iterator();
        while (it.hasNext()) {
            it.next().display();
        }
    }

    // Search by name
    private void searchByName(Scanner sc) {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine().toLowerCase();

        boolean found = false;
        for (Student s : list) {
            if (s.getName().toLowerCase().contains(name)) {
                s.display();
                found = true;
            }
        }
        if (!found) System.out.println("No student found.");
    }

    // Delete by name
    private void deleteByName(Scanner sc) {
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();

        Iterator<Student> it = list.iterator();
        boolean found = false;

        while (it.hasNext()) {
            Student s = it.next();
            if (s.getName().equalsIgnoreCase(name)) {
                it.remove();
                found = true;
                System.out.println("Record deleted.");
                break;
            }
        }
        if (!found) System.out.println("No such student.");
    }

    // Sort by marks
    private void sortByMarks() {
        Collections.sort(list, new SortByMarks());
        System.out.println("\nSorted Student List by Marks:");
        viewAll();
    }

    // RandomAccessFile demo
    private void randomRead() {
        try {
            RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r");
            System.out.println("Reading first 50 bytes:\n");
            byte[] arr = new byte[50];
            raf.read(arr);
            System.out.println(new String(arr));
            raf.close();
        } catch (Exception e) {
            System.out.println("Random access error.");
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 6) {

            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid option.");
                continue;
            }

            switch (choice) {

                case 1: addStudent(sc); break;
                case 2: viewAll(); break;
                case 3: searchByName(sc); break;
                case 4: deleteByName(sc); break;
                case 5: sortByMarks(); break;
                case 6:
                    saveToFile();
                    randomRead();
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    public static void main(String[] args) {
        StudentRecordSystem_LAB_Assignment_4 obj = new StudentRecordSystem_LAB_Assignment_4();
        obj.menu();
    }
}
