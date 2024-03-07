package structure;

public class CountingBloomFilter {
    private final int[] bitSet;
    private final int bitSetSize;
    private final int numberOfHashFunctions;

    public CountingBloomFilter(int bitSetSize, int numberOfHashFunctions) {
        this.bitSetSize = bitSetSize;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.bitSet = new int[bitSetSize];
    }

    public void add(Object object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            bitSet[Math.abs(hashCode % bitSetSize)]++;
        }
    }

    public void remove(Object object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            bitSet[Math.abs(hashCode % bitSetSize)]--;
        }
    }

    public boolean contains(Object object) {
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int hashCode = hash(object, i);
            if (bitSet[Math.abs(hashCode % bitSetSize)] <= 0) {
                return false;
            }
        }
        return true;
    }

    private int hash(Object object, int i) {
        return object.hashCode() + i * i;
    }
}

