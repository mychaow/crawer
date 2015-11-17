package one;

import java.util.LinkedList;

public class Queue<T> {
	private LinkedList<T> queue = new LinkedList<T>();
	
	public void enQueue(T t){
		queue.addLast(t);
	}
	
	public T deQueue(){
		return queue.removeFirst();
	}
	
	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	
	public boolean contains(T t){
		return queue.contains(t);
	}
	
	public void empty(){
		queue.clear();
	}
}
