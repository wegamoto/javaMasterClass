package dev.lpa;

import dev.lpa.model.LPAStudent;
import dev.lpa.model.Student;
import dev.lpa.util.QueryItem;
import dev.lpa.util.QueryList;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

record Employee(String name) implements QueryItem {

    @Override
    public boolean matchFieldValue(String fieldName, String value) {
        return false;
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        int studentCount = 10;
        List<Student> students = new ArrayList<>();
        for(int i = 0; i < studentCount; i++) {
            students.add(new Student());
        }
        students.add(new LPAStudent());
        printMoreLists(students);

        List<LPAStudent> lpaStudents = new ArrayList<>();
        for(int i = 0; i < studentCount; i++) {
            lpaStudents.add(new LPAStudent());
        }
        printMoreLists(lpaStudents);

        testList(new ArrayList<String>(List.of("Able", "Barry", "Charlie")));
        testList(new ArrayList<Integer>(List.of(1,2,3)));

        var queryList = new QueryList<>(lpaStudents);
        var matches = queryList.getMatches(
                "Course", "Python");
        printMoreLists(matches);

        var students2021 =
                QueryList.getMatches(students, "YearStarted", "2021");
        printMoreLists(students2021);

//        QueryList<Employee> employeeList = new QueryList<>();

    }

    public static void printMoreLists(List<? extends Student> students) {

        for(var student : students) {
            System.out.println(student);
        }
        System.out.println();
    }

    public static void testList(List<?> list) {

        for (var element : list) {
            if (element instanceof String s) {
                System.out.println("String: " + s.toUpperCase());
            } else if (element instanceof Integer i) {
                System.out.println("Integer: " + i.floatValue());
            }
        }
    }
////    public static <T extends Student> void printList(List<T> students) {
////
////        for(var student : students) {
////            System.out.println(student.getYearStarted() + ": " + student);
////        }
////        System.out.println();
////    }
//
//    public static <T extends Student> void printList(List<T> students) {
//
//        for(var student : students) {
//            System.out.println(student.getYearStarted() + ": " + student);
//        }
//        System.out.println();
//    }
//
////    public static <T extends Student> void printList(List<T> students) {
////
////        for(var student : students) {
////            System.out.println(student.getYearStarted() + ": " + student);
////        }
////        System.out.println();
////    }
}