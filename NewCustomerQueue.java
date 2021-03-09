import java.security.NoSuchAlgorithmException;

public class NewCustomerQueue {
    private final Customer[] array;
    private final CBlockChain table;

    public NewCustomerQueue(int capacity) {
        this.array = new Customer[capacity];
        this.table = new CBlockChain(capacity);
    }

    /**
     * Insert Customer c into queue.
     * Return the final index at which the customer is stored.
     * Return -1 if the customer could not be inserted.
     */
    public int insert(Customer c) throws NoSuchAlgorithmException {
        if (size() == array.length) {
            return -1;
        }
        table.put(c);
        c.setPosInQueue(size());
        array[size()] = c;
        while (true) {
            if (c.compareTo(parent(c)) > 0) {
                swap(parent(c), c);
            } else {
                break;
            }
        }
        return c.posInQueue();
    }

    /**
     * Remove and return the customer with the highest investment value.
     * If there are multiple customers with the same investment value,
     * return the one who arrived first.
     */
    public Customer delMax() throws NoSuchAlgorithmException {
        if (isEmpty()) {
            return null;
        } else if (size() == 1) {
            Customer removed = getMax();
            removed.setPosInQueue(-1);
            table.remove(removed.name());
            array[0] = null;
            return removed;
        }
        Customer removed = getMax();
        swap(array[0], array[size() - 1]);
        array[size() - 1] = null;
        Customer swim = array[0];
        while (true) {
            Customer left = left_child(swim);
            Customer right = right_child(swim);
            if (right == null && left == null) {
                break;
            } else if (right == null && left.compareTo(swim) > 0) {
                swap(swim, left);
            } else if (right != null && left != null) {
                if (left.compareTo(right) > 0) {
                    if (left.compareTo(swim) > 0) {
                        swap(swim, left);
                    } else {
                        break;
                    }
                } else {
                    if (right.compareTo(swim) > 0) {
                        swap(swim, right);
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        removed.setPosInQueue(-1);
        table.remove(removed.name());
        return removed;
    }

    /**
     * Return but do not remove the first customer in the queue.
     */
    public Customer getMax() {
        return array[0];
    }

    /**
     * Return the number of customers currently in the queue.
     */
    public int size() {
        int customers = 0;
        for (Customer customer : array) {
            if (customer != null) {
                customers++;
            }
        }
        return customers;
    }

    /**
     * Return true if the queue is empty; false else.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Swaps the two given customers.
     */
    private void swap(Customer parent, Customer child) {
        int index = child.posInQueue();
        child.setPosInQueue(parent.posInQueue());
        parent.setPosInQueue(index);
        array[child.posInQueue()] = child;
        array[parent.posInQueue()] = parent;
    }

    /**
     * Returns the parent of the given customer index.
     */
    private Customer parent(Customer c) {
        return array[(c.posInQueue() - 1) / 2];
    }

    /**
     * Returns the left child of the given customer index.
     */
    private Customer left_child(Customer c) {
        int index = 2 * c.posInQueue() + 1;
        if (index >= size()) {
            return null;
        }
        return array[index];
    }

    /**
     * Returns the right child of the given customer index.
     */
    private Customer right_child(Customer c) {
        int index = 2 * c.posInQueue() + 2;
        if (index >= size()) {
            return null;
        }
        return array[index];
    }

    /**
     * Used for testing underlying data structure
     */
    public Customer[] getArray() {
        return array;
    }

    /**
     * Remove and return the Customer with name s from the queue.
     * Return null if the Customer isn't in the queue.
     */
    public Customer remove(String s) throws NoSuchAlgorithmException {
        Customer removed = table.get(s);
        if (removed == null) {
            return null;
        }
        int index = removed.posInQueue();
        if (size() == 1) {
            removed.setPosInQueue(-1);
            table.remove(removed.name());
            array[0] = null;
            return removed;
        } else if (index == size() - 1) {
            removed.setPosInQueue(-1);
            table.remove(removed.name());
            array[size() - 1] = null;
            return removed;
        }
        swap(array[index], array[size() - 1]);
        array[size() - 1] = null;
        Customer swim = array[index];
        while (true) {
            if (swim.compareTo(parent(swim)) > 0) {
                swap(parent(swim), swim);
            } else {
                break;
            }
        }
        while (true) {
            Customer left = left_child(swim);
            Customer right = right_child(swim);
            if (right == null && left == null) {
                break;
            } else if (right == null && left.compareTo(swim) > 0) {
                swap(swim, left);
            } else if (right != null && left != null) {
                if (left.compareTo(right) > 0) {
                    if (left.compareTo(swim) > 0) {
                        swap(swim, left);
                    } else {
                        break;
                    }
                } else {
                    if (right.compareTo(swim) > 0) {
                        swap(swim, right);
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        removed.setPosInQueue(-1);
        table.remove(removed.name());
        return removed;
    }

    /**
     * Update the emergency level of the Customer
     * with name s to investment.
     */
    public void update(String s, int investment) throws NoSuchAlgorithmException {
        Customer c = table.get(s);
        if (c == null) {
            return;
        }
        c.setInvestment(investment);
        remove(s);
        insert(c);
    }

    /**
     * Get Customer object from customer name.
     */
    public Customer getCustomer(String name) throws NoSuchAlgorithmException {
        return table.get(name);
    }

}
