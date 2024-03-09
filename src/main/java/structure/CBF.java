package structure;

public interface CBF<T> {
    void clear();
    void add(T t);
    void remove(T t);
    boolean contains(T t);
}
