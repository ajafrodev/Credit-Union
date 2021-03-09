import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class CBlockChain {

    private ArrayList[] table;
    private int size;

    /**
     * Set the table size to the first.
     * Prime number p >= capacity.
     */
    public CBlockChain(int capacity) {
        size = 0;
        if (capacity == 2 || capacity == 3 || isPrime(capacity)) {
            this.table = new ArrayList[capacity];
        } else {
            this.table = new ArrayList[getNextPrime(capacity)];
        }
    }

    /**
     * Find the hash code for a given name.
     */
    private int hash(String key) throws NoSuchAlgorithmException {
        MessageDigest message = MessageDigest.getInstance("SHA-256");
        message.update(key.getBytes());
        byte[] bytes = message.digest();
        StringBuilder string = new StringBuilder();
        for (byte b: bytes) {
            string.append(String.format("%02x", b & 0xff));
        }
        int hashCode = string.toString().hashCode() % table.length;
        if (hashCode < 0) {
            hashCode += table.length;
        }
        return hashCode;
    }

    /**
     * Return the Customer with the given name
     * or null if the Customer is not in the table.
     */
    public Customer get(String name) throws NoSuchAlgorithmException {
        if (table[hash(name)] != null) {
            for (Object customer : table[hash(name)]) {
                Customer c = (Customer) customer;
                if (c.name().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Put Customer c into the table.
     */
    public void put(Customer c) throws NoSuchAlgorithmException {
        if (get(c.name()) != null) {
            return;
        }
        int hash_code = hash(c.name());
        if (table[hash_code] == null) {
            table[hash_code] = new ArrayList<Customer>();
        }
        table[hash_code].add(c);
        size++;
    }

    /**
     * Remove and return the Customer with the given name
     * from the table. Return null if Customer doesn't exist.
     */
    public Customer remove(String name) throws NoSuchAlgorithmException {
        if (get(name) != null) {
            for (Object customer : table[hash(name)]) {
                Customer c = (Customer) customer;
                if (c.name().equals(name)) {
                    table[hash(name)].remove(c);
                    size--;
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Return the number of Customers in the table
     */
    public int size() {
        return size;
    }

    /**
     * Returns the underlying structure for testing
     */
    public ArrayList<Customer>[] getArray() {
        return table;
    }

    /**
     * Get the next prime number p >= num
     */
    private int getNextPrime(int num) {
        if (num == 2 || num == 3)
            return num;
        int rem = num % 6;
        switch (rem) {
            case 0, 4 -> num++;
            case 2 -> num += 3;
            case 3 -> num += 2;
        }
        while (!isPrime(num)) {
            if (num % 6 == 5) {
                num += 2;
            } else {
                num += 4;
            }
        }
        return num;
    }

    /**
     * Determines if a number > 3 is prime
     */
    private boolean isPrime(int num) {
        if (num % 2 == 0) {
            return false;
        }
        int x = 3;
        for (int i = x; i < num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

}


