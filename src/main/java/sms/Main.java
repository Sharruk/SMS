package sms;

import sms.exceptions.RecordNotFoundException;
import sms.exceptions.ValidationException;
import sms.managers.*;
import sms.models.*;
import sms.utils.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Main entry point of the Learning Management System (LMS).
 *
 * Responsibilities:
 *   - Initialize all manager objects (which load data from .txt files)
 *   - Show a role-based login screen
 *   - Route each user to their own menu (Admin / Teacher / Student)
 *
 * OOP demonstrated here:
 *   - POLYMORPHISM    : user.login(), user.displayMenu(), user.logout()
 *                       called on a User reference — actual subclass method runs
 *   - METHOD OVERLOADING : assignCourse() works for both Teacher and Student
 */
public class Main {

    // ── Managers (each one handles one data file) ──────────────────────────
    private static StudentManager    studentManager;
    private static TeacherManager    teacherManager;
    private static CourseManager     courseManager;
    private static GradeManager      gradeManager;
    private static AssignmentManager assignmentManager;
    private static AdminManager      adminManager;

    private static final Scanner scanner = new Scanner(System.in);

    // ═══════════════════════════════════════════════════════════════════════
    //  APPLICATION ENTRY POINT
    // ═══════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        printBanner();
        initializeManagers();
        runLoginLoop();
        System.out.println("\nGoodbye! Thank you for using LMS.");
        scanner.close();
    }

    private static void printBanner() {
        System.out.println("==================================================");
        System.out.println("       Learning Management System (LMS)");
        System.out.println("     Java OOP Project — 5th Semester");
        System.out.println("==================================================");
    }

    // Create all manager objects; each manager reads its .txt file automatically
    private static void initializeManagers() {
        System.out.println("\nLoading data...");
        adminManager      = new AdminManager();
        teacherManager    = new TeacherManager();
        studentManager    = new StudentManager();
        courseManager     = new CourseManager();
        gradeManager      = new GradeManager();
        assignmentManager = new AssignmentManager();
        System.out.println("All data loaded successfully.\n");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  LOGIN SYSTEM
    // ═══════════════════════════════════════════════════════════════════════

    private static void runLoginLoop() {
        while (true) {
            System.out.println("\n══════════════ LOGIN ══════════════");
            System.out.print("Username (or 'quit' to exit): ");
            String username = scanner.nextLine().trim();
            if (username.equalsIgnoreCase("quit")) break;

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            // Check Admin first
            Admin admin = adminManager.findByLogin(username, password);
            if (admin != null) {
                admin.login();           // POLYMORPHISM — User reference calls Admin.login()
                runAdminMenu(admin);
                continue;
            }

            // Check Teacher
            Teacher teacher = teacherManager.findByLogin(username, password);
            if (teacher != null) {
                teacher.login();         // POLYMORPHISM — User reference calls Teacher.login()
                runTeacherMenu(teacher);
                continue;
            }

            // Check Student
            Student student = studentManager.findByLogin(username, password);
            if (student != null) {
                student.login();         // POLYMORPHISM — User reference calls Student.login()
                runStudentMenu(student);
                continue;
            }

            System.out.println("Incorrect username or password. Please try again.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ADMIN MENU
    // ═══════════════════════════════════════════════════════════════════════

    private static void runAdminMenu(Admin admin) {
        while (true) {
            admin.displayMenu();                     // POLYMORPHISM — overridden in Admin
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": manageStudents();           break;
                case "2": manageTeachers();           break;
                case "3": manageCourses();            break;
                case "4": manageGrades();             break;
                case "5": manageAssignments();        break;
                case "6": showReports();              break;
                case "0": admin.logout(); return;
                default:  System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ─── Student Management ────────────────────────────────────────────────

    private static void manageStudents() {
        while (true) {
            System.out.println("\n──── Student Management ────");
            System.out.println("1. View All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student by Name");
            System.out.println("6. Sort Students");
            System.out.println("7. Enroll Student in Course");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAllStudents();        break;
                case "2": addStudent();             break;
                case "3": updateStudent();          break;
                case "4": deleteStudent();          break;
                case "5": searchStudents();         break;
                case "6": sortStudents();           break;
                case "7": enrollStudentInCourse();  break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllStudents() {
        List<Student> list = studentManager.getAllStudents();
        if (list.isEmpty()) { System.out.println("No students registered yet."); return; }
        System.out.println("\n──── All Students (" + list.size() + ") ────");
        for (Student s : list) System.out.println(s);
    }

    private static void addStudent() {
        System.out.println("\n──── Add New Student ────");
        try {
            System.out.print("Full Name    : ");
            String name = scanner.nextLine().trim();
            InputValidator.validateName(name);

            System.out.print("Email        : ");
            String email = scanner.nextLine().trim();
            InputValidator.validateEmail(email);

            System.out.print("Username     : ");
            String username = scanner.nextLine().trim();
            InputValidator.validateUsername(username);

            System.out.print("Password     : ");
            String password = scanner.nextLine().trim();
            InputValidator.validatePassword(password);

            System.out.print("Department   : ");
            String dept = scanner.nextLine().trim();

            System.out.print("Batch (e.g. 2022-2026): ");
            String batch = scanner.nextLine().trim();

            int id = studentManager.getNextId();
            studentManager.addStudent(new Student(id, name, email, username, password, dept, batch));
            System.out.println("Assigned Student ID: " + id);

        } catch (ValidationException e) {
            System.out.println("Validation Error — " + e.getMessage());
        }
    }

    private static void updateStudent() {
        System.out.print("\nEnter Student ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student s = studentManager.findById(id);
            System.out.println("Current record: " + s);

            System.out.print("New Name       (Enter to keep '" + s.getName()       + "'): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = s.getName();

            System.out.print("New Department (Enter to keep '" + s.getDepartment() + "'): ");
            String dept = scanner.nextLine().trim();
            if (dept.isEmpty()) dept = s.getDepartment();

            System.out.print("New Batch      (Enter to keep '" + s.getBatch()      + "'): ");
            String batch = scanner.nextLine().trim();
            if (batch.isEmpty()) batch = s.getBatch();

            Student updated = new Student(id, name, s.getEmail(),
                                          s.getUsername(), s.getPassword(), dept, batch);
            updated.setEnrolledCourseIds(s.getEnrolledCourseIds());
            studentManager.updateStudent(updated);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        System.out.print("\nEnter Student ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student s = studentManager.findById(id);
            System.out.println("Student found: " + s);
            System.out.print("Are you sure you want to delete this student? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                studentManager.deleteStudent(id);
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchStudents() {
        System.out.print("\nEnter name to search: ");
        String keyword = scanner.nextLine().trim();
        List<Student> results = studentManager.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("No students found matching '" + keyword + "'.");
        } else {
            System.out.println("Found " + results.size() + " result(s):");
            for (Student s : results) System.out.println(s);
        }
    }

    private static void sortStudents() {
        System.out.println("Sort by:  1. Name   2. ID");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();
        List<Student> sorted;
        if (choice.equals("2")) {
            sorted = studentManager.sortById();
            System.out.println("\nStudents sorted by ID:");
        } else {
            sorted = studentManager.sortByName();
            System.out.println("\nStudents sorted by Name:");
        }
        for (Student s : sorted) System.out.println(s);
    }

    // Demonstrates METHOD OVERLOADING — same action for Student vs Teacher
    private static void enrollStudentInCourse() {
        System.out.print("\nEnter Student ID: ");
        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentManager.findById(studentId);
            System.out.println("Student: " + student.getName());

            viewAllCourses();
            System.out.print("Enter Course ID to enroll in: ");
            String courseId = scanner.nextLine().trim();
            Course course = courseManager.findById(courseId);

            student.enrollInCourse(courseId);           // domain method on Student
            studentManager.updateStudent(student);      // persist the change
            System.out.println(student.getName() + " enrolled in " + course.getCourseName());

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── Teacher Management ────────────────────────────────────────────────

    private static void manageTeachers() {
        while (true) {
            System.out.println("\n──── Teacher Management ────");
            System.out.println("1. View All Teachers");
            System.out.println("2. Add Teacher");
            System.out.println("3. Delete Teacher");
            System.out.println("4. Assign Course to Teacher");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAllTeachers();          break;
                case "2": addTeacher();               break;
                case "3": deleteTeacher();            break;
                case "4": assignCourseToTeacher();    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllTeachers() {
        List<Teacher> list = teacherManager.getAllTeachers();
        if (list.isEmpty()) { System.out.println("No teachers registered yet."); return; }
        System.out.println("\n──── All Teachers (" + list.size() + ") ────");
        for (Teacher t : list) System.out.println(t);
    }

    private static void addTeacher() {
        System.out.println("\n──── Add New Teacher ────");
        try {
            System.out.print("Full Name    : ");
            String name = scanner.nextLine().trim();
            InputValidator.validateName(name);

            System.out.print("Email        : ");
            String email = scanner.nextLine().trim();
            InputValidator.validateEmail(email);

            System.out.print("Username     : ");
            String username = scanner.nextLine().trim();
            InputValidator.validateUsername(username);

            System.out.print("Password     : ");
            String password = scanner.nextLine().trim();
            InputValidator.validatePassword(password);

            System.out.print("Department   : ");
            String dept = scanner.nextLine().trim();

            int id = teacherManager.getNextId();
            teacherManager.addTeacher(new Teacher(id, name, email, username, password, dept));
            System.out.println("Assigned Teacher ID: " + id);

        } catch (ValidationException e) {
            System.out.println("Validation Error — " + e.getMessage());
        }
    }

    private static void deleteTeacher() {
        System.out.print("\nEnter Teacher ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            teacherManager.deleteTeacher(id);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // METHOD OVERLOADING counterpart — assigns course to Teacher (not Student)
    private static void assignCourseToTeacher() {
        System.out.print("\nEnter Teacher ID: ");
        try {
            int teacherId = Integer.parseInt(scanner.nextLine().trim());
            Teacher teacher = teacherManager.findById(teacherId);
            System.out.println("Teacher: " + teacher.getName());

            viewAllCourses();
            System.out.print("Enter Course ID to assign: ");
            String courseId = scanner.nextLine().trim();
            Course course = courseManager.findById(courseId);

            courseManager.assignTeacher(courseId, teacher.getName());
            System.out.println("Course '" + course.getCourseName() +
                               "' assigned to " + teacher.getName());

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── Course Management ─────────────────────────────────────────────────

    private static void manageCourses() {
        while (true) {
            System.out.println("\n──── Course Management ────");
            System.out.println("1. View All Courses");
            System.out.println("2. Add Course");
            System.out.println("3. Delete Course");
            System.out.println("4. Sort Courses by Name");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAllCourses();   break;
                case "2": addCourse();        break;
                case "3": deleteCourse();     break;
                case "4": sortCourses();      break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllCourses() {
        List<Course> list = courseManager.getAllCourses();
        if (list.isEmpty()) { System.out.println("No courses available yet."); return; }
        System.out.println("\n──── All Courses (" + list.size() + ") ────");
        for (Course c : list) System.out.println(c);
    }

    private static void addCourse() {
        System.out.println("\n──── Add New Course ────");
        System.out.print("Course ID   (e.g. CS101): ");
        String courseId = scanner.nextLine().trim();
        System.out.print("Course Name : ");
        String name = scanner.nextLine().trim();
        System.out.print("Credit Hours: ");
        try {
            int credits = Integer.parseInt(scanner.nextLine().trim());
            // Uses the 2-argument constructor (CONSTRUCTOR OVERLOADING)
            courseManager.addCourse(new Course(courseId, name, credits));
        } catch (NumberFormatException e) {
            System.out.println("Invalid credit hours — must be a number.");
        }
    }

    private static void deleteCourse() {
        System.out.print("\nEnter Course ID to delete: ");
        try {
            courseManager.deleteCourse(scanner.nextLine().trim());
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void sortCourses() {
        List<Course> sorted = courseManager.sortByName();
        System.out.println("\nCourses sorted alphabetically:");
        for (Course c : sorted) System.out.println(c);
    }

    // ─── Grade Management ──────────────────────────────────────────────────

    private static void manageGrades() {
        while (true) {
            System.out.println("\n──── Grade Management ────");
            System.out.println("1. View All Grades");
            System.out.println("2. Assign / Update Grade");
            System.out.println("3. View Grades for a Student");
            System.out.println("4. View Grades for a Course");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAllGrades();          break;
                case "2": assignGrade();            break;
                case "3": viewGradesByStudent();    break;
                case "4": viewGradesByCourse();     break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllGrades() {
        List<Grade> list = gradeManager.getAllGrades();
        if (list.isEmpty()) { System.out.println("No grades recorded yet."); return; }
        System.out.println("\n──── All Grades (" + list.size() + ") ────");
        for (Grade g : list) System.out.println(g);
    }

    private static void assignGrade() {
        System.out.print("\nEnter Student ID: ");
        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentManager.findById(studentId);

            viewAllCourses();
            System.out.print("Enter Course ID  : ");
            String courseId = scanner.nextLine().trim();
            Course course = courseManager.findById(courseId);

            System.out.print("Enter Grade (A/B+/B/C+/C/D/F): ");
            String gradeValue = scanner.nextLine().trim().toUpperCase();

            gradeManager.addGrade(new Grade(
                studentId, courseId,
                student.getName(), course.getCourseName(), gradeValue
            ));

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewGradesByStudent() {
        System.out.print("\nEnter Student ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student s = studentManager.findById(id);
            List<Grade> grades = gradeManager.getGradesByStudent(id);
            System.out.println("\nGrades for " + s.getName() + ":");
            if (grades.isEmpty()) System.out.println("No grades yet.");
            for (Grade g : grades) System.out.println("  " + g);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewGradesByCourse() {
        viewAllCourses();
        System.out.print("\nEnter Course ID: ");
        String courseId = scanner.nextLine().trim();
        try {
            Course c = courseManager.findById(courseId);
            List<Grade> grades = gradeManager.getGradesByCourse(courseId);
            System.out.println("\nGrades for course: " + c.getCourseName());
            if (grades.isEmpty()) System.out.println("No grades yet.");
            for (Grade g : grades) System.out.println("  " + g);
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── Assignment Management ─────────────────────────────────────────────

    private static void manageAssignments() {
        while (true) {
            System.out.println("\n──── Assignment Management ────");
            System.out.println("1. View All Assignments");
            System.out.println("2. Create Assignment");
            System.out.println("3. Delete Assignment");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAllAssignments();   break;
                case "2": createAssignment();     break;
                case "3": deleteAssignment();     break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllAssignments() {
        List<Assignment> list = assignmentManager.getAllAssignments();
        if (list.isEmpty()) { System.out.println("No assignments yet."); return; }
        System.out.println("\n──── All Assignments (" + list.size() + ") ────");
        for (Assignment a : list) System.out.println(a);
    }

    private static void createAssignment() {
        System.out.println("\n──── Create Assignment ────");
        viewAllCourses();
        System.out.print("Enter Course ID  : ");
        String courseId = scanner.nextLine().trim();
        try {
            Course course = courseManager.findById(courseId);
            System.out.print("Assignment Title : ");
            String title = scanner.nextLine().trim();
            System.out.print("Due Date         : ");
            String dueDate = scanner.nextLine().trim();
            int id = assignmentManager.getNextId();
            assignmentManager.addAssignment(
                new Assignment(id, courseId, course.getCourseName(), title, dueDate)
            );
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteAssignment() {
        System.out.print("\nEnter Assignment ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            assignmentManager.deleteAssignment(id);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    // ─── Reports ───────────────────────────────────────────────────────────

    private static void showReports() {
        System.out.println("\n══════════════ SYSTEM REPORT ══════════════");
        System.out.println("Total Students   : " + studentManager.getCount());
        System.out.println("Total Teachers   : " + teacherManager.getCount());
        System.out.println("Total Courses    : " + courseManager.getCount());
        System.out.println("Total Grades     : " + gradeManager.getCount());
        System.out.println("Total Assignments: " + assignmentManager.getCount());
        System.out.println("═══════════════════════════════════════════");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TEACHER MENU
    // ═══════════════════════════════════════════════════════════════════════

    private static void runTeacherMenu(Teacher teacher) {
        while (true) {
            teacher.displayMenu();                       // POLYMORPHISM
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewTeacherCourses(teacher);               break;
                case "2": teacherManageGrades(teacher);              break;
                case "3": teacherManageAssignments(teacher);         break;
                case "4": viewAllStudents();                         break;
                case "0": teacher.logout(); return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewTeacherCourses(Teacher teacher) {
        List<Course> courses = courseManager.getCoursesByTeacher(teacher.getName());
        System.out.println("\nYour assigned courses:");
        if (courses.isEmpty()) System.out.println("  No courses assigned yet. Contact admin.");
        for (Course c : courses) System.out.println("  " + c);
    }

    private static void teacherManageGrades(Teacher teacher) {
        while (true) {
            System.out.println("\n──── Grades ────");
            System.out.println("1. Assign / Update Grade");
            System.out.println("2. View Grades for My Courses");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": assignGrade();                         break;
                case "2": viewGradesForTeacher(teacher);        break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewGradesForTeacher(Teacher teacher) {
        List<Course> courses = courseManager.getCoursesByTeacher(teacher.getName());
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        for (Course c : courses) {
            System.out.println("\nCourse: " + c.getCourseName());
            List<Grade> grades = gradeManager.getGradesByCourse(c.getCourseId());
            if (grades.isEmpty()) System.out.println("  No grades recorded.");
            for (Grade g : grades) System.out.println("  " + g);
        }
    }

    private static void teacherManageAssignments(Teacher teacher) {
        while (true) {
            System.out.println("\n──── Assignments ────");
            System.out.println("1. View My Assignments");
            System.out.println("2. Create Assignment");
            System.out.println("3. Delete Assignment");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewAssignmentsForTeacher(teacher);  break;
                case "2": createAssignmentForTeacher(teacher); break;
                case "3": deleteAssignment();                  break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAssignmentsForTeacher(Teacher teacher) {
        List<Course> courses = courseManager.getCoursesByTeacher(teacher.getName());
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        for (Course c : courses) {
            System.out.println("\nCourse: " + c.getCourseName());
            List<Assignment> list = assignmentManager.getAssignmentsByCourse(c.getCourseId());
            if (list.isEmpty()) System.out.println("  No assignments yet.");
            for (Assignment a : list) System.out.println("  " + a);
        }
    }

    private static void createAssignmentForTeacher(Teacher teacher) {
        List<Course> courses = courseManager.getCoursesByTeacher(teacher.getName());
        if (courses.isEmpty()) {
            System.out.println("You have no assigned courses. Ask admin to assign one first.");
            return;
        }
        System.out.println("\nYour courses:");
        for (Course c : courses) System.out.println("  " + c);
        System.out.print("Enter Course ID  : ");
        String courseId = scanner.nextLine().trim();
        try {
            Course course = courseManager.findById(courseId);
            System.out.print("Assignment Title : ");
            String title = scanner.nextLine().trim();
            System.out.print("Due Date         : ");
            String dueDate = scanner.nextLine().trim();
            int id = assignmentManager.getNextId();
            assignmentManager.addAssignment(
                new Assignment(id, courseId, course.getCourseName(), title, dueDate)
            );
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  STUDENT MENU
    // ═══════════════════════════════════════════════════════════════════════

    private static void runStudentMenu(Student student) {
        while (true) {
            student.displayMenu();                       // POLYMORPHISM
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewStudentCourses(student);       break;
                case "2": viewStudentGrades(student);        break;
                case "3": viewStudentAssignments(student);   break;
                case "0": student.logout(); return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private static void viewStudentCourses(Student student) {
        List<String> ids = student.getEnrolledCourseIds();
        System.out.println("\nYour enrolled courses:");
        if (ids.isEmpty()) { System.out.println("  Not enrolled in any courses yet."); return; }
        for (String courseId : ids) {
            try {
                System.out.println("  " + courseManager.findById(courseId));
            } catch (RecordNotFoundException e) {
                System.out.println("  Course '" + courseId + "' not found.");
            }
        }
    }

    private static void viewStudentGrades(Student student) {
        List<Grade> grades = gradeManager.getGradesByStudent(student.getId());
        System.out.println("\nYour grades:");
        if (grades.isEmpty()) System.out.println("  No grades available yet.");
        for (Grade g : grades) System.out.println("  " + g);
    }

    private static void viewStudentAssignments(Student student) {
        List<String> ids = student.getEnrolledCourseIds();
        System.out.println("\nYour assignments:");
        if (ids.isEmpty()) { System.out.println("  Not enrolled in any courses yet."); return; }
        for (String courseId : ids) {
            try {
                Course c = courseManager.findById(courseId);
                List<Assignment> list = assignmentManager.getAssignmentsByCourse(courseId);
                System.out.println("\n  Course: " + c.getCourseName());
                if (list.isEmpty()) System.out.println("    No assignments.");
                for (Assignment a : list) System.out.println("    " + a);
            } catch (RecordNotFoundException ignored) { }
        }
    }
}
