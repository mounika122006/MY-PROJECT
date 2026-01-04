import java.io.*;
import java.util.*;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n===== Expense Tracker =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Monthly Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    addExpense(sc);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    monthlySummary();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }


    private static void addExpense(Scanner sc) {
        try {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            System.out.print("Enter Category: ");
            String category = sc.nextLine();

            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println("Amount must be greater than zero!");
                return;
            }

    
            try (FileWriter fw = new FileWriter("expenses.txt", true)) {
                fw.write(date + "," + category + "," + amount + "\n");
            }

            System.out.println("Expense added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving expense: " + e.getMessage());
        }
    }


    private static void viewExpenses() {
        try (BufferedReader br = new BufferedReader(new FileReader("expenses.txt"))) {
            String line;
            System.out.println("\n===== All Expenses =====");
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    System.out.println("Date: " + parts[0] + " | Category: " + parts[1] + " | Amount: ₹" + parts[2]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No expenses found!");
            }
        } catch (IOException e) {
            System.out.println("No expenses recorded yet.");
        }
    }

    
    private static void monthlySummary() {
        Map<String, Double> monthlyTotal = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("expenses.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String date = parts[0];
                double amount = Double.parseDouble(parts[2]);
                String month = date.substring(0, 7);

                monthlyTotal.put(month, monthlyTotal.getOrDefault(month, 0.0) + amount);
            }

            System.out.println("\n===== Monthly Summary =====");
            for (String month : monthlyTotal.keySet()) {
                System.out.println(month + " : ₹" + monthlyTotal.get(month));
            }

            if (monthlyTotal.isEmpty()) {
                System.out.println("No data to summarize.");
            }

        } catch (IOException e) {
            System.out.println("No expenses recorded yet.");
        }
    }
}
