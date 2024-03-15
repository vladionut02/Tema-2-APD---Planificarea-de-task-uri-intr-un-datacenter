/* Implement this class. */

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    private final BlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>(1, Comparator.comparing(Task::getPriority).reversed().thenComparing(Task::getStart));
    private boolean running = true;
    private boolean flag;
    private boolean preempted = false;
    Task currentTask;
    public boolean hasRunningTask() {
        return flag;
    }
    private void executeTask(Task task) {
        // Simulate task execution
        while (task.getLeft() > 0) {
            for (Task taskIndex : taskQueue) {
                if (task.isPreemptible() && taskIndex.getPriority() > task.getPriority()){
                    preempted = true;
                    taskQueue.add(task);
                    currentTask = null;
                    break;
                }
            }

            if (preempted)
                break;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Decrement the remaining time of the task
            task.setLeft(task.getLeft() - 500);

            if (task.getLeft() == 0){
                task.finish();
                currentTask = null;
                flag = false;

            }
        }

    }

    @Override
    public void run() {
        while (running) {
            preempted = false;
            // Check if there are tasks in the queue
            if (!taskQueue.isEmpty()) {
                // Get the next task from the queue
                currentTask = taskQueue.poll();
                flag = true;
                // Execute the task
                executeTask(currentTask);

            }

        }
    }

    @Override
    public void addTask(Task task) {
        taskQueue.add(task);
    }

    @Override
    public int getQueueSize() {
        return taskQueue.size();
    }

    @Override
    public long getWorkLeft() {
        long totalWorkLeft = 0;

        for (Task task : taskQueue) {
            totalWorkLeft += task.getLeft();
        }

        if (currentTask!= null) {
            totalWorkLeft += currentTask.getLeft();
        }

        return totalWorkLeft;
    }

    @Override
    public void shutdown() {
        running = false;
    }
}
