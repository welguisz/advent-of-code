package com.dwelguisz.utilities;

import java.util.HashMap;
import java.util.Map;

public class UnionFind<T> {
    int size;
    Map<T,T> parents;
    Map<T,Integer> element_sizes;

    public UnionFind(int size, T[] elements) {
        this.size = size;
        parents = new HashMap<>();
        element_sizes = new HashMap<>();
        for (T element : elements) {
            parents.put(element, element);
            element_sizes.put(element, 1);
        }
    }

    public T find(T element) {
        if (element.equals(parents.get(element))) {
            return element;
        }
        T parent = find(parents.get(element));
        parents.put(element, parent);
        return parent;
    }

    public void union(T a, T b) {
        T f_a = find(a);
        T f_b = find(b);
        if (f_a.equals(f_b)) {
            return;
        }
        if(element_sizes.get(f_a) <= element_sizes.get(f_b)) {
            parents.put(f_a,f_b);
            element_sizes.merge(f_b,element_sizes.get(f_a), Integer::sum);
        } else {
            parents.put(f_b,f_a);
            element_sizes.merge(f_a,element_sizes.get(f_b), Integer::sum);
        }
    }

    public Integer getElementSize(T node) {
        return element_sizes.get(node);
    }

}
