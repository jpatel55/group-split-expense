import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager expenseManager = new ExpenseManager();
        
        while (true) {
            System.out.println("\nExpense Splitter Menu:");
            System.out.println("1. Add Friend");
            System.out.println("2. Delete Friend");
            System.out.println("3. Delete All Friends");
            System.out.println("4. Add Expense");
            System.out.println("5. Delete Expense");
            System.out.println("6. Delete All Expenses");
            System.out.println("7. View Expenses");
            System.out.println("8. Settle Up");
            System.out.println("9. Exit");
            System.out.print("\nChoose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter friend's name: ");
                    String friendName = scanner.nextLine();
                    if (expenseManager.addFriend(friendName)) {
                        System.out.println("Friend added successfully.");
                    } else {
                        System.out.println("Friend already exists.");
                    }
                    break;
                
                case 2:
                    System.out.print("Enter friend's name to delete: ");
                    String deleteName = scanner.nextLine();
                    if (expenseManager.deleteFriend(deleteName)) {
                        System.out.println("Friend deleted successfully.");
                    } else {
                        System.out.println("Friend not found.");
                    }
                    break;
                
                case 3:
                    expenseManager.deleteAllFriends();
                    System.out.println("All friends deleted successfully.");
                    break;
                
                case 4:
                    System.out.print("Enter friend's name: ");
                    String expName = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter description (optional, press enter to skip): ");
                    String description = scanner.nextLine();

                    if (description.isEmpty()) {
                        description = null;
                    }
                    
                    if (expenseManager.addExpense(expName, amount, description)) {
                        System.out.println("Expense added successfully.");
                    } else {
                        System.out.println("Failed to add expense. Ensure the friend exists and the amount is valid.");
                    }
                    break;
                
                case 5:
                    System.out.print("Enter friend's name: ");
                    String delExpName = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double delAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter description (optional, press enter to skip): ");
                    String delDescription = scanner.nextLine();

                    if (delDescription.isEmpty()) {
                        delDescription = null;
                    }
                    
                    if (expenseManager.deleteExpense(delExpName, delAmount, delDescription)) {
                        System.out.println("Expense deleted successfully.");
                    } else {
                        System.out.println("Expense not found.");
                    }
                    break;
                
                case 6:
                    expenseManager.deleteAllExpenses();
                    System.out.println("All expenses deleted successfully.");
                    break;
                
                case 7:
                    expenseManager.viewExpenses();
                    break;
                
                case 8:
                    expenseManager.calculateBalances();
                    expenseManager.settleUp();
                    break;
                
                case 9:
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    return;
                
                default:
                    System.out.println("Invalid choice. Please try again.");        
            }

            System.out.println("\nPress enter to continue ...");
            scanner.nextLine();
            System.out.print("\033[H\033[2J");
            System.out.flush();            
        }
    }
}
