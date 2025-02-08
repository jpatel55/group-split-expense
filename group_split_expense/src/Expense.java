public class Expense {
    private Friend friend;
    private double amount;
    private String description;

    public Expense(Friend friend, double amount, String description) {
        this.friend = new Friend(friend.getName());
        this.amount = amount;
        this.description = description;
    }

    public Expense(Friend friend, double amount) {
        this.friend = new Friend(friend.getName());
        this.amount = amount;
        this.description = null;
    }

    public Friend getFriend() {
        return friend;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        if (description != null)
            return friend.getName() + " paid $" + String.format("%.2f", amount) + " (" + description + ")";
        else
            return friend.getName() + " paid $" + String.format("%.2f", amount); 
    }
}
