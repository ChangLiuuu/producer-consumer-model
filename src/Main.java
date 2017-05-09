import java.util.Scanner;

import pro_con.Queue_Pro_Con.Consumer;
import pro_con.Queue_Pro_Con.Producer;

public class Main {
	
	public static void main(String[] args) {
		Queue_Pro_Con test = new Queue_Pro_Con();
		
		System.out.println("Please set the number of consumers: ");
		Scanner sc = new Scanner(System.in);
		int consumer_max = sc.nextInt();
		
		
		Queue_Pro_Con.Consumer[] consumer = new Queue_Pro_Con.Consumer[consumer_max];
		for (int i = 0; i < consumer_max; i++) {
			consumer[i] = test.new Consumer("consumer" + (i + 1));
			consumer[i].start();
		}
		Queue_Pro_Con.Producer pro = test.new Producer();
		pro.start();

	}
}
