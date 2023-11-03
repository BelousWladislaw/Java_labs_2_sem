import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CalculatorThread implements Runnable {
    private int start;
    private int end;
    private long result;

    public CalculatorThread(int start, int end) {
        this.start = start;
        this.end = end;
        this.result = 1;
    }

    public void run() {
        for (int i = start; i <= end; i++) {
            if (i % 2 == 1) {
                result *= i;
            }
        }
    }

    public long getResult() {
        return result;
    }
}

class ThreadGenerator {
    private int numThreads;
    private String operation;
    private int intervalStart;
    private int intervalEnd;
    private List<CalculatorThread> threads;

    public ThreadGenerator() {
        threads = new ArrayList<>();
    }

    public void execute() {
        int numbersPerThread = (intervalEnd - intervalStart + 1) / numThreads;
        int remainder = (intervalEnd - intervalStart + 1) % numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = intervalStart + i * numbersPerThread;
            int end = start + numbersPerThread - 1;

            if (i == numThreads - 1) {
                end += remainder;
            }

            CalculatorThread calculatorThread = new CalculatorThread(start, end);
            threads.add(calculatorThread);

            Thread thread = new Thread(calculatorThread);
            thread.start();
        }

        for (CalculatorThread thread : threads) {
            try {
                thread.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public long getResult() {
        long result = threads.get(0).getResult();

        for (int i = 1; i < threads.size(); i++) {
            CalculatorThread thread = threads.get(i);

            if (operation.equals("add")) {
                result += thread.getResult();
            } else if (operation.equals("subtract")) {
                result -= thread.getResult();
            } else if (operation.equals("multiply")) {
                result *= thread.getResult();
            }
        }

        return result;
    }

    public void getUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of threads: ");
        numThreads = scanner.nextInt();
        // количество потоков

        System.out.print("Enter the operation (add/subtract/multiply): ");
        operation = scanner.next();
        // выбор операции

        System.out.print("Enter the start of the interval: ");
        intervalStart = scanner.nextInt();
        // число начала интервала

        System.out.print("Enter the end of the interval: ");
        intervalEnd = scanner.nextInt();
        // конец интервала
    }

    public static void main(String[] args) {
        ThreadGenerator threadGenerator = new ThreadGenerator();
        threadGenerator.getUserInput();
        threadGenerator.execute();
        long result = threadGenerator.getResult();
        System.out.println("Result: " + result);
    }
}