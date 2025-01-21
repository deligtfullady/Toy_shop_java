public class Toys {
    protected String id;
    protected String name;
    protected int balance;
    protected float chance;

    public Toys(String id, String name, int balance, float chance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getChance() {
        return chance;
    }

    public int getBalance() {
        return balance;
    }


    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return (id + ", " + name + ", " + balance + ", " + chance);
    }
}