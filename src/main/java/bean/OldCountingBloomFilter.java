package bean;

import java.util.BitSet;

@Deprecated
public class OldCountingBloomFilter<T> {

    private final int size;
    private final int[] counts;
    //    final 表示值bits一旦分配，就无法更改
//    bitSet类提供了设置和获取集合中各个位的方法
    private final BitSet bits;
    private final int numHashFunctions;

    public OldCountingBloomFilter(int size, int numHashFunctions) {
//        设置布隆过滤器的大小
        this.size = size;
//        哈希函数的个数
        this.numHashFunctions = numHashFunctions;
//        比特位对应的数字
        this.counts = new int[size];
//        设置比特位数组
        this.bits = new BitSet(size);
    }

    public void add(T obj) {
//        给传入的参数进行hash编码
        int hash1 = obj.hashCode();
        int hash2 = hash1 >>> 16;
        for (int i = 0; i < numHashFunctions; i++) {

            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
//                组合hash值为负的话，则用按位NOT运算符使其为正
                combinedHash = ~combinedHash;
            }
            int pos = combinedHash % size;
            counts[pos]++;
            bits.set(pos);
        }
    }
    //查看元素是否在cbf中
    public boolean mightContain(T obj) {
        int hash1 = obj.hashCode();
        int hash2 = hash1 >>> 16;
//        它通过添加 hash1 以及循环迭代索引和 hash2 的乘积来计算组合哈希值
        for (int i = 0; i < numHashFunctions; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
//            使用对组合哈希值和布隆过滤器的大小（大小）的模运算来计算位在 BitSet 中的位置。
            int pos = combinedHash % size;
            if (!bits.get(pos)) {
                return false;
            }
        }
        return true;
    }
    //remove方法递减计数，并在计数达到 0 时清除 BitSet 中的相应位置
    public void remove(T obj) {
        int hash1 = obj.hashCode();
//        按位右移 16个位置
        int hash2 = hash1 >>> 16;
        for (int i = 0; i < numHashFunctions; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            int pos = combinedHash % size;
            counts[pos]--;
            if (counts[pos] == 0) {
                bits.clear(pos);
            }
        }
    }
}

