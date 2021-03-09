public class CustomerQueue {

    private final Customer[] array;

    public CustomerQueue(int capacity) {
        this.array = new Customer[capacity];
    }

    /**
     * Insert Customer c into queue.
     * Return the final index at which the customer is stored.
     * Return -1 if the customer could not be inserted.
     */
    public int insert(Customer c) {
        if (size() == array.length) {
            return -1;
        }
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
    public Customer delMax() {
        if (isEmpty()) {
            return null;
        } else if (size() == 1) {
            Customer removed = getMax();
            removed.setPosInQueue(-1);
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

}
