import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;
    private boolean isComplete;

    public Task(String description) {
        this.description = description;
        this.isComplete = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void markComplete() {
        isComplete = true;
    }
}

public class TaskManager {
    private ArrayList<Task> tasks;
    private Scanner scanner;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadTasksFromFile(); // Load tasks from file when the program starts
    }

    public void addTask(String description) {
        tasks.add(new Task(description));
        System.out.println("Task added successfully!");
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to display.");
        } else {
            System.out.println("Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". " + task.getDescription() + " - " +
                        (task.isComplete() ? "Complete" : "Incomplete"));
            }
        }
    }

    public void markTaskComplete(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);
            task.markComplete();
            System.out.println("Task marked as complete!");
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            System.out.println("Task deleted successfully!");
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nTask Manager Menu:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task as Complete");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("Enter task description: ");
                    scanner.nextLine(); // Consume newline
                    String description = scanner.nextLine();
                    addTask(description);
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    markTaskComplete(getTaskIndex("Enter task index to mark as complete: "));
                    break;
                case 4:
                    deleteTask(getTaskIndex("Enter task index to delete: "));
                    break;
                case 5:
                    running = false;
                    saveTasksToFile(); // Save tasks to file when the program exits
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private int getTaskIndex(String message) {
        int index;
        while (true) {
            try {
                System.out.print(message);
                index = scanner.nextInt() - 1;
                if (index < 0 || index >= tasks.size()) {
                    System.out.println("Invalid task index. Please try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
        return index;
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tasks.ser"))) {
            outputStream.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void loadTasksFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("tasks.ser"))) {
            tasks = (ArrayList<Task>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing tasks file found. Starting with an empty task list.");
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.start();
    }
}
