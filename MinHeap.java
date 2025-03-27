import java.util.Arrays;

public class MinHeap {
    private int[] heap;
    private int[] priorities;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new int[capacity];
        this.priorities = new int[capacity];
        Arrays.fill(priorities, Integer.MAX_VALUE);
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        int tempPriority = priorities[i];
        priorities[i] = priorities[j];
        priorities[j] = tempPriority;
    }

    private void heapifyUp(int index) {
        while (index > 0 && priorities[heap[index]] < priorities[heap[(index - 1) / 2]]) {
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;

        if (leftChild < size && priorities[heap[leftChild]] < priorities[heap[smallest]]) {
            smallest = leftChild;
        }
        if (rightChild < size && priorities[heap[rightChild]] < priorities[heap[smallest]]) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    public void insert(int node, int priority) {
        if (size >= capacity) {
            throw new IllegalStateException("Heap is full");
        }

        heap[size] = node;
        priorities[node] = priority;
        heapifyUp(size);
        size++;
    }

    public int[] extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        int root = heap[0];
        int[] result = new int[] { root, priorities[root] };

        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);

        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

}
