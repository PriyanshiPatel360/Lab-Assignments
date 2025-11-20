import java.util.*;

// Custom Exception Class
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// Thread class
class Loader implements Runnable {
    @Override
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Thread interrupted.");
        }
    }
}

// Main Manager Class
class StudentManager {

    private Map<Integer, Student> records = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    // Add Student
    public void addStudent() {

        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer roll = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            if (course.trim().isEmpty()) {
                throw new IllegalArgumentException("Course cannot be empty.");
            }

            System.out.print("Enter Marks (Double): ");
            Double marks = Double.parseDouble(sc.nextLine());

            if (marks < 0 || marks > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100.");
            }

            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            Student s = new Student(roll, name, email, course, marks);
            records.put(roll, s);

            System.out.println("\nStudent added successfully.\n");
            s.display();

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error.");
        } finally {
            System.out.println("Operation finished.\n");
        }
    }

    // Search Student
    public void searchStudent() {
        try {
            System.out.print("Enter Roll No to search: ");
            Integer r = Integer.parseInt(sc.nextLine());

            if (!records.containsKey(r)) {
                throw new StudentNotFoundException("Student not found with Roll No: " + r);
            }

            Student s = records.get(r);
            s.display();

        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number.");
        }
    }

    // Delete Student
    public void deleteStudent() {
        try {
            System.out.print("Enter Roll No to delete: ");
            Integer r = Integer.parseInt(sc.nextLine());

            if (!records.containsKey(r)) {
                throw new StudentNotFoundException("Student not found to delete.");
            }

            records.remove(r);
            System.out.println("Student deleted successfully.");

        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // View All Students
    public void viewAll() {
        if (records.isEmpty()) {
            System.out.println("No student records found.\n");
            return;
        }

        for (Student s : records.values()) {
            s.display();
        }
    }

    // Update Student Course
    public void updateStudent() {
        try {
            System.out.print("Enter Roll No to update: ");
            Integer r = Integer.parseInt(sc.nextLine());

            if (!records.containsKey(r)) {
                throw new StudentNotFoundException("Student not found.");
            }

            System.out.print("Enter new Course: ");
            String newCourse = sc.nextLine();

            Student s = records.get(r);
            s.updateCourse(newCourse);

            System.out.println("Record updated.\n");

        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Student Class
class Student {

    private Integer rollNo;
    private String name;
    private String email;
    private String course;
    private Double marks;
    private char grade;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    public void updateCourse(String newCourse) {
        this.course = newCourse;
    }

    public void display() {
        System.out.println("\nRoll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("-----------------------------");
    }
}

// MAIN CLASS
public class StudentManagementSystem_LAB_Assignment_3 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        int choice = 0;

        while (choice != 6) {

            System.out.println("===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice.\n");
                continue;
            }

            switch (choice) {

                case 1:
                    manager.addStudent();
                    break;

                case 2:
                    manager.searchStudent();
                    break;

                case 3:
                    manager.updateStudent();
                    break;

                case 4:
                    manager.deleteStudent();
                    break;

                case 5:
                    manager.viewAll();
                    break;

                case 6:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid option.\n");
            }
        }

        sc.close();
    }
}
