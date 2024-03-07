package structure;

public class CountingBloomFilter<T> {
    private final int[] bitSet;
    private final int bitSetSize;
    private final int numberOfHashFunctions;

    public CountingBloomFilter(int bitSetSize, int numberOfHashFunctions) {
        this.bitSetSize = bitSetSize;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.bitSet = new int[bitSetSize];
    }

    public void add(T object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            bitSet[Math.abs(hashCode % bitSetSize)]++;
        }
    }

    public void remove(T object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            bitSet[Math.abs(hashCode % bitSetSize)]--;
        }
    }

    public boolean contains(T object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            if (bitSet[Math.abs(hashCode % bitSetSize)] <= 0) {
                return false;
            }
        }
        return true;
    }

    private int hash(T object, int i) {
        return object.hashCode() + i * i;
    }
}

