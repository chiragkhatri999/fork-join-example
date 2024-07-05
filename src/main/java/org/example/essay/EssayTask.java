package org.example.essay;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class EssayTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10;
    private int[] tasks;
    private int start;
    private int end;

    public EssayTask(int[] tasks, int start, int end){
        this.tasks = tasks;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long finishedSubTopics = 0;
        if(end-start < THRESHOLD){
            for(int i=start; i<end; i++){
                // Work on sub-section
                processing();
                finishedSubTopics++;
                System.out.println("Sub-section: " + (i+1) + " Completed by: "
                        + Thread.currentThread().getName());
            }
            return finishedSubTopics;
        } else {
            int mid = (start + end) >>> 1;
            EssayTask leftTask = new EssayTask(tasks, start, mid);
            EssayTask rightTask = new EssayTask(tasks, mid, end);
            leftTask.fork();
            rightTask.fork();
            return leftTask.join() + rightTask.join();
        }
    }

    private void processing() {
        try{
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}
