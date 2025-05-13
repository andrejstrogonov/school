package ru.hogwarts.school;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 100_000;
    private final int start;
    private final int end;

    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if ((end - start) <= THRESHOLD) {
            int sum = 0;
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            int middle = (start + end) / 2;
            SumTask leftTask = new SumTask(start, middle);
            SumTask rightTask = new SumTask(middle + 1, end);

            leftTask.fork(); // Fork the left half
            int rightResult = rightTask.compute(); // Compute the right half
            int leftResult = leftTask.join(); // Wait for the left half

            return leftResult + rightResult;
        }
    }
}