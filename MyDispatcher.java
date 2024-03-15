/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    private int lastAssignedNodeId = -1;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }
    @Override
    public void addTask(Task task) {
        synchronized (this) {
            switch (algorithm) {
                case ROUND_ROBIN -> roundRobin(task);
                case SHORTEST_QUEUE -> shortestQueue(task);
                case SIZE_INTERVAL_TASK_ASSIGNMENT -> sizeInterval(task);
                case LEAST_WORK_LEFT -> leastWorkLeft(task);
            }
        }
    }

    private void roundRobin(Task task) {
        synchronized (this) {
            int totalNodes = this.hosts.size();
            int nextNodeId = (lastAssignedNodeId + 1) % totalNodes;
            lastAssignedNodeId = nextNodeId;

            Host nextNode = this.hosts.get(nextNodeId);
            nextNode.addTask(task);
        }
    }

    private void shortestQueue(Task task) {
        synchronized (this) {
            MyHost selectedHost = null;
            int minQueueSize = Integer.MAX_VALUE;
            int queueSize;
            for (Host host : this.hosts) {
                MyHost myHost = (MyHost) host;
                if (myHost.hasRunningTask()){
                    queueSize = myHost.getQueueSize() + 1;
                } else {
                    queueSize = myHost.getQueueSize();
                }
                if (queueSize < minQueueSize) {
                    minQueueSize = queueSize;
                    selectedHost = myHost;
                }
            }

            if (selectedHost != null) {
                selectedHost.addTask(task);
            }
        }

        }

    private void sizeInterval(Task task) {
        synchronized (this) {
            switch (task.getType()){
                case SHORT -> this.hosts.get(0).addTask(task);
                case MEDIUM ->this.hosts.get(1).addTask(task);
                case LONG -> this.hosts.get(2).addTask(task);
            }
        }
    }

    private void leastWorkLeft(Task task){
        synchronized (this) {
            MyHost selectedHost = null;
            long minWorkLeft = Long.MAX_VALUE;
            for (Host host : this.hosts) {
                MyHost myHost = (MyHost) host;
                if (myHost.getWorkLeft() < minWorkLeft) {
                    minWorkLeft = myHost.getWorkLeft();
                    selectedHost = myHost;
                }
            }
            selectedHost.addTask(task);
        }
    }
}
