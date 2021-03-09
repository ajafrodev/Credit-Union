import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CreditUnion {

    private NewCustomerQueue cq;
    private int cr_threshold;
    private int capacity;
    private int processed = 0;
    private int seenByManager = 0;
    private int sentToBank = 0;
    private int walkedOut = 0;

    public CreditUnion(int cap, int cr_threshold) {
        this.capacity = cap;
        this.cr_threshold = cr_threshold;
        cq = new NewCustomerQueue(cap);
    }

    /**
     * Return threshold.
     */
    public int cr_threshold() {
        return this.cr_threshold;
    }

    /**
     * Return capacity.
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Process a new Customer:
     * If investment is higher than the cr_threshold,
     * send them to the bank and return null.
     * Otherwise, try to insert them into the queue
     * if the queue is full, compare their investment to the highest
     * investment currently in the queue; if investment is higher,
     * send them to the bank and return null; if the current max
     * is higher, send the max Customer to the Bank, insert
     * the new Customer into the queue, and return the name
     * of the max Customer
     */
    public String process(String name, int investment) throws NoSuchAlgorithmException {
        processed++;
        Customer c = new Customer(name, investment, processed);
        if (investment > cr_threshold) {
            cq.remove(c.name());
            sendToBank(c);
            return null;
        } else {
            if (cq.size() < capacity) {
                cq.insert(c);
                return name;
            } else {
                Customer max = cq.getMax();
                if (investment > max.investment()) {
                    cq.remove(c.name());
                    sendToBank(c);
                    return null;
                }
                if (processed <= max.time_in()) {
                    cq.remove(max.name());
                    sendToBank(max);
                    cq.insert(c);
                    return max.name();
                }
            }
        }
        return null;
    }

    /**
     * Manager is available--send the Customer with
     * highest investment value to be seen; return the name
     * of the Customer or null if the queue is empty
     */
    public String seeNext() throws NoSuchAlgorithmException {
        if (!cq.isEmpty()) {
            Customer c = cq.delMax();
            seeManager(c);
            return c.name();
        }
        return null;
    }

    /**
     * Customer experiences an emergency, raising their
     * investment value; if the investment value exceeds the
     * cr_threshold, send them directly to the bank;
     * else update their investment value in the queue;
     * return true if the Customer is removed from the queue
     * and false otherwise
     */
    public boolean handle_emergency(String name) throws NoSuchAlgorithmException {
        Customer c = cq.getCustomer(name);
        if (c == null) {
            return false;
        }
        int investment = c.investment();
        Random random = new Random();
        double a = random.nextDouble();
        int b = random.nextInt(10);
        if (a < 0.5) {
            investment -= investment*b*0.01;
        } else {
            investment += investment*b*0.01;
        }
        cq.update(name, investment);
        if (investment > cr_threshold) {
            cq.remove(c.name());
            sendToBank(c);
            return true;
        } else if (investment <= 0) {
            walk_out(c.name());
            return true;
        }
        return false;
    }

    /**
     * Customer decides to walk out remove them from the queue
     */
    public void walk_out(String name) throws NoSuchAlgorithmException {
        walkedOut++;
        cq.remove(name);
    }

    /**
     * Indicates that Customer c has been sent to the Bank
     */
    private void sendToBank(Customer c) {
        System.out.println("Customer " + c + " sent to Bank.");
        sentToBank++;
    }

    /**
     * Indicates that a Customer is being seen by a Manager
     */
    private void seeManager(Customer c) {
        System.out.println("Customer " + c + " is seeing a manager.");
        seenByManager++;
    }

    public int processed() {
        return processed;
    }

    public int sentToBank() {
        return sentToBank;
    }

    public int seenByManager() {
        return seenByManager;
    }

    public int walkedOut() {
        return walkedOut;
    }

}