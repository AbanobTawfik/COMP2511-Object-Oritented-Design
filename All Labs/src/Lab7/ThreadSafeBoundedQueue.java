package Lab7;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeBoundedQueue<E>{

    private Object[] elements;
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    private Lock lock = new ReentrantLock();
    private Condition space = lock.newCondition();
    private Condition value = lock.newCondition();

    public ThreadSafeBoundedQueue(int capacity){
        elements = new Object[capacity];
    }

    /**
     * this will clear the queue please note
     * @param capacity the size of queue
     */
    public void resize(int capacity){
        elements = new Object[capacity];
    }

    public boolean isFull(){
        System.out.println(toString());
        return size == elements.length;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public E poll(){
        lock.lock();
        try {
            while (isEmpty())
                value.await();
            E item = (E) elements[head];
            System.out.println(item + " on thread");
            head++;
            size--;
            if(head == elements.length)
                head = 0;
            space.signalAll();
            return item;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
        return null;
    }

    public void addItem(E item){
        lock.lock();
        try{
            while(isFull())
                space.await();
            System.out.println(item + " on thread");
            elements[tail] = item;
            tail++;
            size++;
            if(tail == elements.length)
                tail = 0;
            value.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "ThreadSafeBoundedQueue{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }
}
