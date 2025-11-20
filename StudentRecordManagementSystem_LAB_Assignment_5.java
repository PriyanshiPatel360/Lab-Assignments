import java.io.*;
import java.util.*;

abstract class Person {
    protected String name;
    protected String email;

    public Person() {}
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student() {}

    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public double getMarks() { return marks; }
    public char getGrade() { return grade; }

    public void setCourse(String course) { this.course = course; }
    public void setMarks(double marks) { this.marks = marks; calculateGrade(); }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    @Override
    public void displayInfo() {
        System.out.println("Roll No : " + rollNo);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Course  : " + course);
        System.out.println("Marks   : " + marks);
        System.out.println("Grade   : " + grade);
        System.out.println("-----------------------------------");
    }

    public String toCSV() {
        return rollNo + "," + name.replace(",", "") + "," + email.replace(",", "") + "," + course.replace(",", "") + "," + marks;
    }

    public static Student fromCSV(String line) {
        String[] p = line.split(",", 5);
        if (p.length < 5) return null;
        return new Student(Integer.parseInt(p[0]), p[1], p[2], p[3], Double.parseDouble(p[4]));
    }
}

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

class Loader implements Runnable {
    private String message;

    public Loader(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            System.out.print(message);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(220);
                System.out.print(".");
            }
            System.out.println();
        } catch (Exception e) {}
    }
}

interface RecordActions {
    void addStudent();
    void deleteStudent() throws StudentNotFoundException;
    void updateStudent() throws StudentNotFoundException;
    void searchStudent() throws StudentNotFoundException;
    void viewAllStudents();
    void sortByMarksDescending();
    void saveAndExit();
}

class StudentManager implements RecordActions {

    private final String dataFile = "students.txt";
    private Map<Integer, Student> recordMap = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public StudentManager() {
        loadFromFile();
    }

    private void loadFromFile() {
        File f = new File(dataFile);
        try {
            if (!f.exists()) {
                f.createNewFile();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromCSV(line);
                if (s != null) recordMap.put(s.getRollNo(), s);
            }
            br.close();

            System.out.println("Loaded " + recordMap.size() + " records.");

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            int roll = Integer.parseInt(scanner.nextLine().trim());

            if (recordMap.containsKey(roll)) {
                System.out.println("Roll No already exists.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            System.out.print("Enter Course: ");
            String course = scanner.nextLine();

            System.out.print("Enter Marks: ");
            double marks = Double.parseDouble(scanner.nextLine());

            Thread t = new Thread(new Loader("Saving"));
            t.start();
            t.join();

            Student s = new Student(roll, name, email, course, marks);
            recordMap.put(roll, s);

            System.out.println("Student added.");

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    @Override
    public void deleteStudent() throws StudentNotFoundException {
        try {
            System.out.print("Enter Roll No to delete: ");
            int roll = Integer.parseInt(scanner.nextLine());

            if (!recordMap.containsKey(roll))
                throw new StudentNotFoundException("Record not found.");

            Thread t = new Thread(new Loader("Deleting"));
            t.start();
            t.join();

            recordMap.remove(roll);
            System.out.println("Record deleted.");

        } catch (NumberFormatException e) {
            throw new StudentNotFoundException("Invalid roll number.");
        } catch (InterruptedException e) {}
    }

    @Override
    public void updateStudent() throws StudentNotFoundException {
        try {
            System.out.print("Enter Roll No to update: ");
            int roll = Integer.parseInt(scanner.nextLine());

            Student s = recordMap.get(roll);
            if (s == null) throw new StudentNotFoundException("Record not found.");

            System.out.print("New Name (" + s.getName() + "): ");
            String n = scanner.nextLine();
            if (!n.isEmpty()) s.name = n;

            System.out.print("New Email (" + s.getEmail() + "): ");
            String e = scanner.nextLine();
            if (!e.isEmpty()) s.email = e;

            System.out.print("New Course (" + s.getCourse() + "): ");
            String c = scanner.nextLine();
            if (!c.isEmpty()) s.setCourse(c);

            System.out.print("New Marks (" + s.getMarks() + "): ");
            String m = scanner.nextLine();
            if (!m.isEmpty()) s.setMarks(Double.parseDouble(m));

            Thread t = new Thread(new Loader("Updating"));
            t.start();
            t.join();

            System.out.println("Record updated.");

        } catch (NumberFormatException e) {
            throw new StudentNotFoundException("Invalid number.");
        } catch (InterruptedException e) {}
    }

    @Override
    public void searchStudent() throws StudentNotFoundException {
        System.out.print("Search by (1) Roll or (2) Name: ");
        String c = scanner.nextLine();

        if (c.equals("1")) {
            System.out.print("Enter Roll No: ");
            int roll = Integer.parseInt(scanner.nextLine());
            Student s = recordMap.get(roll);
            if (s == null) throw new StudentNotFoundException("Not found.");
            s.displayInfo();

        } else if (c.equals("2")) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().toLowerCase();
            boolean found = false;

            for (Student s : recordMap.values()) {
                if (s.getName().toLowerCase().contains(name)) {
                    s.displayInfo();
                    found = true;
                }
            }

            if (!found) throw new StudentNotFoundException("No match.");
        }
    }

    @Override
    public void viewAllStudents() {
        if (recordMap.isEmpty()) {
            System.out.println("No records.");
            return;
        }

        Iterator<Student> it = recordMap.values().iterator();
        while (it.hasNext()) {
            it.next().displayInfo();
        }
    }

    @Override
    public void sortByMarksDescending() {
        List<Student> list = new ArrayList<>(recordMap.values());
        list.sort((a, b) -> Double.compare(b.getMarks(), a.getMarks()));

        for (Student s : list) s.displayInfo();
    }

    @Override
    public void saveAndExit() {
        try {
            Thread t = new Thread(new Loader("Saving"));
            t.start();
            t.join();

            BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
            for (Student s : recordMap.values()) {
                bw.write(s.toCSV());
                bw.newLine();
            }
            bw.close();

            RandomAccessFile raf = new RandomAccessFile(dataFile, "r");
            int read = (int) Math.min(80, raf.length());
            if (read > 0) {
                byte[] arr = new byte[read];
                raf.read(arr);
                System.out.println("\nFile Preview:");
                System.out.println(new String(arr));
            }
            raf.close();

            System.out.println("Done.");
        } catch (Exception e) {
            System.out.println("Error saving.");
        }
    }
}

public class StudentRecordManagementSystem_LAB_Assignment_5 {

    public static void main(String[] args) {
        StudentManager m = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All");
            System.out.println("3. Search");
            System.out.println("4. Delete");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Update");
            System.out.println("7. Save & Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();
            sc.close();

            try {
                switch (choice) {
                    case "1": m.addStudent(); break;
                    case "2": m.viewAllStudents(); break;
                    case "3": m.searchStudent(); break;
                    case "4": m.deleteStudent(); break;
                    case "5": m.sortByMarksDescending(); break;
                    case "6": m.updateStudent(); break;
                    case "7": m.saveAndExit(); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
