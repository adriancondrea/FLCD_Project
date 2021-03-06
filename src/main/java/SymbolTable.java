import java.util.Formatter;

public class SymbolTable {
    private final String[] hashTable;
    private final int size;

    public SymbolTable(int size) {
        this.size = size;
        this.hashTable = new String[size];
    }

    /**
     * compute the hash value for the given key as the sum of ascii characters, modulo size
     *
     * @param key - the string to hash
     * @return the hash value for given key
     */
    private int hashFunction(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            hash += c;
        }
        return hash % size;
    }


    /**
     * if the element already belongs to the symbol table, return its position. Otherwise, add it and then return its position
     * collisions (two keys having the same hash value) are handled by open addressing (all keys are stored in the same hash table)
     * with linear probing (search for the next available location) with probing interval = 1.
     *
     * @param key - the element to add to the hash table
     * @return the position on which the element is in hash table
     */
    public int addElement(String key) {
        int hash = hashFunction(key);

        while (hashTable[hash] != null) {
            if (hashTable[hash].equals(key)) {
                return hash;
            }
            hash = (hash + 1) % size;
        }
        hashTable[hash] = key;
        return hash;
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        formatter.format("%-32s%-32s\n", "Key:", "Value:");
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                formatter.format("%-32s%-32s\n", i, hashTable[i]);
            }
        }
        return formatter.toString();
    }
}
