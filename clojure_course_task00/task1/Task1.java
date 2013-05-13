import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Task1 {

	private static final int START = 2;
	private static final int END = 50;
	private static final int STEP = 15;
	public static final long MAX_VALUE = 4_000_000;

	public static void main(String[] args) {
		try {
			Long sum = 0l;

			List<FutureTask<Long>> tasks = new ArrayList<FutureTask<Long>>();
			ExecutorService executor = Executors.newCachedThreadPool();
			int start = START;
			for (int i = start + 1; i < END; i += STEP) {
				FutureTask<Long> task = new FutureTask<Long>(new Fibonacci(start, i));
				tasks.add(task);

				executor.execute(task);
				start = i;
			}

			for (FutureTask<Long> task : tasks) {
				sum += task.get();
			}

			executor.shutdown();

			System.out.printf("Result is " + sum);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}

class Fibonacci implements Callable<Long> {

	private int start;
	private int end;

	public Fibonacci(int startInterval, int endInterval) {
		this.start = startInterval;
		this.end = endInterval;
	}

	public static long getN(int n) {
		long a = 1, ta, b = 1, tb, c = 1, rc = 0, tc, d = 0, rd = 1;

		while (n > 0) {
			if ((n & 1) == 1) {
				tc = rc;
				rc = rc * a + rd * c;
				rd = tc * b + rd * d;
			}
			ta = a;
			tb = b;
			tc = c;
			a = a * a + b * c;
			b = ta * b + b * d;
			c = c * ta + d * c;
			d = tc * tb + d * d;
			n >>= 1;
		}
		return rc;
	}

	@Override
	public Long call() throws Exception {
		return findSum();
	}

	private Long findSum() {
		long prevNumber = Fibonacci.getN(start);
		long curNumber = Fibonacci.getN(start + 1);
		long next = 0;
		Long sum = 0l;
		for (int i = start; i < end; i++) {
			next = (curNumber + prevNumber);
			if (isValid(i, curNumber)) {
				sum += curNumber;
			}
			prevNumber = curNumber;
			curNumber = next;
		}
		return sum;
	}

	private boolean isValid(int number, long curNumber) {
		if (number % 2 == 0 && curNumber <= Task1.MAX_VALUE) {
			return true;
		}
		return false;
	}

}
