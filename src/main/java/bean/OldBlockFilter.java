package bean;

import lombok.Data;

@Data
@Deprecated
public class OldBlockFilter<T> {
    private OldCountingBloomFilter<T> validFilter;
    private OldCountingBloomFilter<T> invalidFilter;
}
