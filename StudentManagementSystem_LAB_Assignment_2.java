import java.util.*;

// Abstract class Person
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

// Student class
class Student extends Person {

    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student() {}

    // Overloaded constructor
    public Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    // Overloaded method
    public void displayInfo(String title) {
        System.out.println(title);
        displayInfo();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    @Override
    public void displayInfo() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("---------------------------------------");
    }

    public int getRollNo() {
        return rollNo;
    }

    public void updateCourse(String newCourse) {
        this.course = newCourse;
    }
}

// Interface for CRUD Actions
interface RecordActions {
    void addStudent(Student s);
    void deleteStudent(int rollNo);
    void updateStudent(int rollNo);
    void searchStudent(int rollNo);
    void viewAllStudents();
}

// Manager class for student operations
class StudentManager implements RecordActions {

    private Map<Integer, Student> records = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    @Override
    public void addStudent(Student s) {
        if (records.containsKey(s.getRollNo())) {
            System.out.println("Duplicate roll number. Cannot add.");
            return;
        }
        records.put(s.getRollNo(), s);
        System.out.println("Student added successfully.\n");
    }

    @Override
    public void deleteStudent(int rollNo) {
        if (records.remove(rollNo) != null)
            System.out.println("Student record deleted.\n");
        else
            System.out.println("Roll number not found.\n");
    }

    @Override
    public void updateStudent(int rollNo) {
        Student s = records.get(rollNo);
        if (s == null) {
            System.out.println("Record not found.\n");
            return;
        }
        System.out.print("Enter new course: ");
        String c = sc.nextLine();
        s.updateCourse(c);
        System.out.println("Record updated.\n");
    }

    @Override
       public void searchStudent(int rollNo) {
        Student s = records.get(rollNo);
        if (s != null) s.displayInfo();
        else System.out.println("No record found.\n");
    }

    @Override
    public void viewAllStudents() {
        if (records.isEmpty()) {
            System.out.println("No students to display.\n");
            return;
        }
        for (Student s : records.values()) {
            s.displayInfo();
        }
    }
}

// Main Class
public class StudentManagementSystem_LAB_Assignment_2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        int choice = 0;

        while (choice != 6) {

            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.\n");
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.print("Enter Roll No: ");
                    int roll = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();

                    System.out.print("Enter Marks: ");
                    double marks = Double.parseDouble(sc.nextLine());

                    Student s = new Student(roll, name, email, course, marks);
                    manager.addStudent(s);
                    break;

                case 2:
                    System.out.print("Enter Roll No to delete: ");
                    manager.deleteStudent(Integer.parseInt(sc.nextLine()));
                    break;

                case 3:
                    System.out.print("Enter Roll No to update: ");
                    manager.updateStudent(Integer.parseInt(sc.nextLine()));
                    break;

                case 4:
                    System.out.print("Enter Roll No to search: ");
                    manager.searchStudent(Integer.parseInt(sc.nextLine()));
                    break;

                case 5:
                    manager.viewAllStudents();
                    break;

                case 6:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.\n");
            }
        }

        sc.close();
    }
}
