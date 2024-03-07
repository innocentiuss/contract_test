package structure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {
        CountingBloomFilter bloomFilter = new CountingBloomFilter(10000, 10);
        bloomFilter.add("1");
        System.out.println(bloomFilter.contains("1"));
        bloomFilter.add("2");
        bloomFilter.remove("1");
        System.out.println(bloomFilter.contains("1"));
        System.out.println(bloomFilter.contains("2"));
        System.out.println(bloomFilter.contains("4"));
    }
}
