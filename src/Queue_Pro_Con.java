import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue_Pro_Con {
	private int max; // define the specified maximum created by producer
	private int bufferSize;// define the size of buffer
	private boolean pro_exit = false; // symbol whether producer exits
	private boolean con_exit = false; // symbol whether consumer exits
	private static LinkedBlockingQueue<Integer> buffer = new LinkedBlockingQueue<Integer>();
	private int integer = 0;
	public Queue_Pro_Con() {

		System.out.println("Please set the size of buffer:");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		this.bufferSize = sc.nextInt();

		System.out.println(
				"Please set the specified maximum created by producer:");
		@SuppressWarnings("resource")
		Scanner sc1 = new Scanner(System.in);
		this.max = sc1.nextInt();
	}
	// producer
	class Producer extends Thread {
		public void run() {
			while (!pro_exit) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				synchronized (buffer) // get the locker of buffer
				{
					// if integer reaches specified maximum, then the semaphore should be changed.
					if (integer == max) {  
						System.out.println("producer should exit.");
						pro_exit = true;
					}

					System.out.println("buffer.size():" + buffer.size() + "   " + bufferSize);

					// if buffer is not full, producer put integers into buffer.
					while (buffer.size() < bufferSize && integer < max) {
						System.out.println("producer is adding " + integer);
						try {
							buffer.put(integer++);   //add element into buffer.
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// when integer reaches 'max', remove the last one.
						if (integer == max) {
							buffer.remove(integer);
						}
                        // if buffer is full, producer notifies the consumer to consume.
						if (buffer.size() == bufferSize) {
							buffer.notify();
						}

					}
					// if buffer is full, it should wait for consumers to consume.
					while (buffer.size() == bufferSize) {
						System.out.println(
								"buffer is full, waiting for consumers to remove integers..");
						try {
							buffer.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			System.out.println("producer exits.");
		}
	}
	// consumer
	class Consumer extends Thread {
		String name;
		public Consumer(String name) {
			this.name = name;
		}
		public void run() {
			while (!con_exit) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				synchronized (buffer) // // get the locker of buffer
				{
					// if integer reaches specified maximum, then the semaphore should be changed.
					if (buffer.size() == 0 && pro_exit) {
						con_exit = true;
					} else if (pro_exit) { // after producer exits, consumers
											// notify each other.
						buffer.notify();
						System.out.println("ffuckkk");
					}
					// if buffer is not empty, then call poll() to remove integer.
					if (buffer.size() > 0) {
						System.out.println(
								name + " is removing " + buffer.peek());
						buffer.poll();
						if (buffer.size() == 0) { 
							//when buffer is empty, consumers notify producer, not for notifying 
							//consumers each other.
							buffer.notify();
						}
					}

					// if buffer is empty and producer doesn't exit, consumers wait for producer to add integers.
					while (buffer.size() == 0 && !pro_exit) {
						System.out.println(
								"Buffer is empty, " + name + " is waiting.");
						try {
							buffer.wait(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			System.out.println(name + " exits.");
		}
	}
}
