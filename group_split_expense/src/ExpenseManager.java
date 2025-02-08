import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

public class ExpenseManager {
    private List<Friend> friends; // List of friends participating in the expenses
    private List<Expense> expenses; // List of recorded expenses
    private Map<String, Double> balances; // Tracks each friend's balance

    public ExpenseManager() {
        friends = new ArrayList<>();
        expenses = new ArrayList<>();
        balances = new HashMap<>();
    }

    /**
     * Adds a friend to the group.
     * @param name The name of the friend.
     * @return True if added successfully, false if the friend already exists.
     */
    public boolean addFriend(String name) {
        for (Friend friend : friends) {
            if (friend.getName().equals(name))
                return false;
        }

        friends.add(new Friend(name));
        return true;
    }

    /**
     * Deletes a friend and their associated expenses from the records.
     * @param name The name of the friend to delete.
     * @return True if deleted successfully, false if the friend does not exist.
     */
    public boolean deleteFriend(String name) {
        Friend targetFriend = null;
        for (Friend friend : friends) {
            if (friend.getName().equals(name)) {
                targetFriend = friend;
                break;
            }
        }

        if (targetFriend == null) {
            return false;
        }

        balances.remove(name);
        
        Iterator<Expense> expenseIterator = expenses.iterator();
        while (expenseIterator.hasNext()) {
            Expense expense = expenseIterator.next();
            if (expense.getFriend().getName().equals(name)) {
                expenseIterator.remove();
            }
        }

        Iterator<Friend> friendIterator = friends.iterator();
        while (friendIterator.hasNext()) {
            Friend friend = friendIterator.next();
            if (friend.getName().equals(name)) {
                friendIterator.remove();
                return true;
            }
        }
        
        return false;
    }

    /**
     * Clears all friends, expenses, and balances.
     */
    public void deleteAllFriends() {
        friends.clear();
        expenses.clear();
        balances.clear();
    }

    /**
     * Adds an expense for a given friend.
     * @param name The name of the friend who paid.
     * @param amount The amount paid.
     * @param description Optional description of the expense.
     * @return True if the expense was added successfully, false otherwise.
     */
    public boolean addExpense(String name, double amount, String description) {
        if (amount <= 0) {
            return false;
        }
        
        for (Friend friend : friends) {
            if (friend.getName().equals(name)) {
                expenses.add(new Expense(friend, amount, description));
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes a specific expense for a given friend.
     * @param name The name of the friend.
     * @param amount The amount of the expense.
     * @param description The description of the expense.
     * @return True if deleted successfully, false otherwise.
     */
    public boolean deleteExpense(String name, double amount, String description) {
        for (Expense expense : expenses) {
            if (expense.getFriend().getName().equals(name) && expense.getAmount() == amount && 
                ((description == null && expense.getDescription() == null) || 
                (description != null && description.equals(expense.getDescription())))) {                
                expenses.remove(expense);
                return true;
            }
        }

        return false;
    }

    /**
     * Clears all recorded expenses and balances.
     */
    public void deleteAllExpenses() {
        expenses.clear();
        balances.clear();
    }

    /**
     * Calculates the balances for each friend by evenly distributing the total expenses.
     */
    public void calculateBalances() {
        balances.clear();

        double totalExpense = 0;

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();

            if (!balances.containsKey(expense.getFriend().getName()))
                balances.put(expense.getFriend().getName(), expense.getAmount());
            else
                balances.put(expense.getFriend().getName(), balances.get(expense.getFriend().getName()) + expense.getAmount());
        }

        for (Friend friend : friends) {
            if (!balances.containsKey(friend.getName()))
                balances.put(friend.getName(), 0.0);
        }
        
        double share = totalExpense / friends.size();

        for (String friend : balances.keySet()) {
            balances.put(friend, balances.get(friend) - share);
        }
    }

    /**
     * Determines who owes whom and prints the necessary payments to settle balances.
     */
    public void settleUp() {
        if (expenses.isEmpty() || friends.size() < 2) {
            System.out.println("Nothing to settle.");
            return;
        }

        Map<String, Double> friendsWhoOwe = new HashMap<>();
        Map<String, Double> friendsWhoPaid = new HashMap<>();

        for (String friend : balances.keySet()) {
            if (balances.get(friend) < 0.0)
                friendsWhoOwe.put(friend, balances.get(friend));
            else
                friendsWhoPaid.put(friend, balances.get(friend));
        }

        Map<String, Double> friendsWhoOweSorted = friendsWhoOwe
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                LinkedHashMap::new));

        Map<String, Double> friendsWhoPaidSorted = friendsWhoPaid
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                LinkedHashMap::new));

        Iterator<Map.Entry<String, Double>> itr = friendsWhoOweSorted.entrySet().iterator();
        String currentFriendWhoOwe;
        double currentFriendOweAmount;

        do {            
            currentFriendWhoOwe = itr.next().getKey();
            currentFriendOweAmount = friendsWhoOweSorted.get(currentFriendWhoOwe);

            for (String friendWhoPaid : friendsWhoPaidSorted.keySet()) {
                if (friendsWhoPaidSorted.get(friendWhoPaid) != 0) {                    
                    if ((friendsWhoPaidSorted.get(friendWhoPaid) + currentFriendOweAmount) < 0) {
                        System.out.println(currentFriendWhoOwe + " owes " + friendWhoPaid + " $" + String.format("%.2f", friendsWhoPaidSorted.get(friendWhoPaid)));
                        friendsWhoOweSorted.put(currentFriendWhoOwe, friendsWhoPaidSorted.get(friendWhoPaid) + currentFriendOweAmount);
                        currentFriendOweAmount = friendsWhoPaidSorted.get(friendWhoPaid) + currentFriendOweAmount;
                        friendsWhoPaidSorted.put(friendWhoPaid, 0.0);
                    } else {
                        System.out.println(currentFriendWhoOwe + " owes " + friendWhoPaid + " $" + String.format("%.2f",(currentFriendOweAmount * -1.0)));
                        friendsWhoPaidSorted.put(friendWhoPaid, friendsWhoPaidSorted.get(friendWhoPaid) + currentFriendOweAmount);
                        friendsWhoOweSorted.put(currentFriendWhoOwe, 0.0);
                        break;
                    }
                }                
            }
        } while (itr.hasNext());        
    }

    /**
     * Displays all recorded expenses.
     */
    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense.toString());
            }
        }
    }
}
