import java.util.*;

class Person {
    protected String name;
}

class Student extends Person {

    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student() {
        // default constructor
    }

    public Student(int rollNo, String name, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    public void inputDetails(Scanner sc) {
        System.out.print("Enter Roll No: ");
        rollNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        name = sc.nextLine();

        System.out.print("Enter Course: ");
        course = sc.nextLine();

        System.out.print("Enter Marks: ");
        marks = sc.nextDouble();

        calculateGrade();
    }

    public void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    public void displayDetails() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("----------------------------------");
    }
}

public class StudentRecordSystem_LAB_Assignment_1 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Student> list = new ArrayList<>();

        int choice = 0;

        while (choice != 3) {

            System.out.println("===== Student Record Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
                continue;
            }

            if (choice == 1) {
                Student s = new Student();
                s.inputDetails(sc);
                list.add(s);
                System.out.println("Student added.\n");
            }
            else if (choice == 2) {
                if (list.isEmpty()) {
                    System.out.println("No records found.\n");
                } else {
                    for (Student s : list) {
                        s.displayDetails();
                    }
                }
            }
            else if (choice == 3) {
                System.out.println("Exiting the application. Goodbye!");
            }
            else {
                System.out.println("Invalid choice.\n");
            }
        }

        sc.close();
    }
}
