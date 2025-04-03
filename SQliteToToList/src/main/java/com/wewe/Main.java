package com.wewe;

import java.util.List;
import com.wewe.sqlitetodolist.model.Task;
import com.wewe.sqlitetodolist.service.TaskService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskService taskService = new TaskService("tasks.db");

        while (true) {
            System.out.println("\n1. Add Task\n2. View Tasks\n3. Mark Task as Completed\n4. Delete Task\n5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Task Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Task Description: ");
                    String description = scanner.nextLine();
                    taskService.addTask(title, description);
                    System.out.println("Task added.");
                    break;
                case 2:
                    List<Task> tasks = taskService.getTasks();
                    for (Task task : tasks) {
                        System.out.println(task);
                    }
                    break;
                case 3:
                    System.out.print("Enter Task ID to mark as completed: ");
                    int idToComplete = scanner.nextInt();
                    taskService.markTaskAsCompleted(idToComplete);
                    System.out.println("Task marked as completed.");
                    break;
                case 4:
                    System.out.print("Enter Task ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    taskService.deleteTask(idToDelete);
                    System.out.println("Task deleted.");
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

