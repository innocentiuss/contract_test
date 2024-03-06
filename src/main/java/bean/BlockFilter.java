package bean;

import lombok.Data;

@Data
public class BlockFilter<T> {
    private CountingBloomFilter<T> validFilter;
    private CountingBloomFilter<T> invalidFilter;
}
