class LinkedList<T> {
    private Node<T> head; // Points to the first node
    private Node<T> tail; // Points to the last node
    private int size; // Tracks the size of the list

    public LinkedList() {
        head = tail = null;
        size = 0;
    }

    // Add a new value at the end
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) { // Empty list
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Add a new value at the beginning
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) { // Empty list
            head = tail = newNode;
        } else {
            newNode.next = head; // Point new node to current head
            head = newNode; // Move head to point to new node
        }
        size++;
    }

    // Get a value by index (0-based)
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index");

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    // Remove the first occurrence of a value
    public boolean remove(T value) {
        if (head == null)
            return false; // Empty list

        if (head.value.equals(value)) { // Value is at the head
            head = head.next;
            if (head == null)
                tail = null;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.value.equals(value)) {
            current = current.next;
        }

        if (current.next == null)
            return false; // Value not found

        // Remove the node
        current.next = current.next.next;
        if (current.next == null)
            tail = current; // Removed tail
        size--;
        return true;
    }

    // Get the size of the list
    public int size() {
        return size;
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    public T[] toArray(T[] array) {
        if (array.length < size) {
            // Create a new array of type T if the input array is too small
            array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }

        Node<T> current = head;
        int index = 0;

        while (current != null) {
            array[index++] = current.value;
            current = current.next;
        }

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

}
