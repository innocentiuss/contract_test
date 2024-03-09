package structure;

import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

public class AnotherCBF<T> implements CBF<T>{
    private final CountingBloomFilter<T> countingBloomFilter;

    public AnotherCBF(int a, int b) {
        this.countingBloomFilter = new FilterBuilder(10000, 0.00001d).countingBits(8).buildCountingBloomFilter();
    }

    @Override
    public void clear() {
        countingBloomFilter.clear();
    }

    @Override
    public void add(T t) {
        countingBloomFilter.add(t);
    }

    @Override
    public void remove(T t) {
        countingBloomFilter.remove(t);
    }

    @Override
    public boolean contains(T t) {
        return countingBloomFilter.contains(t);
    }
}
