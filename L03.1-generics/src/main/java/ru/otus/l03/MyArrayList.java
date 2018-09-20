package ru.otus.l03;

import java.util.*;

public class MyArrayList<T> implements List<T> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROW_RATE = 2;
    private T[] data;
    private int size;

    public MyArrayList() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyArrayList(int initCapacity) {
        data = (T[]) new Object[initCapacity];
        size = 0;
    }

    public MyArrayList(Collection<T> c) {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == data[i]) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(data, 0, arr, 0, size);
        return arr;
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    private void ensureCapacity(int dataSize) {
        if (size + dataSize - data.length > 0) {
            int newCapacity = calcNewCapacity(dataSize);
            data = Arrays.copyOf(data, newCapacity);
        }
    }

    private int calcNewCapacity(int dataSize) {
        int newCapacity = size;
        while (size + dataSize > newCapacity) {
            newCapacity *= GROW_RATE;
        }
        return newCapacity;
    }

    @Override
    public boolean add(T t) {
        ensureCapacity(1);
        data[size] = t;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (Iterator<? extends T> it = c.iterator(); it.hasNext();){
            add(it.next());
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        for (Iterator<? extends T> it = c.iterator(); it.hasNext();){
            add(index, it.next());
            index++;
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public T get(int index) {
        return data[index];
    }

    @Override
    public T set(int index, T element) {
        T newElement = data[index];
        data[index] = element;
        return newElement;
    }

    @Override
    public void add(int index, T element) {
        checkIndex(index);
        ensureCapacity(1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
    }

    private void checkIndex(int index){
        if ((index < 0) || (index >= size())){
            throw new IndexOutOfBoundsException();
        }
    }

    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<T> listIterator() {
        ListIterator<T> it = new ListIterator<T>() {
            private int currentIndex = 0;
            private int lastIndex = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size && data[currentIndex] != null;
            }

            @Override
            public T next() {
                lastIndex = currentIndex;
                currentIndex++;
                return data[lastIndex];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            public T previous() {
                currentIndex--;
                lastIndex = currentIndex;
                return data[lastIndex];
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void set(T t) {
                data[lastIndex] = t;
            }

            @Override
            public void add(T t) {
                add(t);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    public ListIterator<T> listIterator(int index) {
        return null;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}
