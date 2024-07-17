package recursos;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (!key.equals(pair.key)) return false;
        return value.equals(pair.value);
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}

