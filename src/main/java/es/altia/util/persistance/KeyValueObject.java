package es.altia.util.persistance;

import java.io.Serializable;

/**
 * Representa un objeto clave-valor (con id)
 */
public class KeyValueObject<K, V>  implements Serializable {
    K key;
    V value;

    public KeyValueObject() {
    }

    public KeyValueObject(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
    
}
