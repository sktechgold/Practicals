import java.util.*;
class RR {
    static class Process {
        int id, at, bt, remainingBt, ct, tat, wt;
        boolean completed;

        Process(int id, int at, int bt) {
            this.id = id;
            this.at = at;
            this.bt = bt;
            this.remainingBt = bt;
            this.completed = false;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("For Process " + (i + 1) + ":");
            System.out.print("Enter Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Enter Burst Time: ");
            int bt = sc.nextInt();
            processes[i] = new Process(i + 1, at, bt);
        }
        System.out.print("Enter Time Quantum: ");
        int tq = sc.nextInt();
        Queue<Process> queue = new LinkedList<>();
        int currTime = 0, completedCount = 0;
        int avgTat = 0, avgWt = 0;
        Arrays.sort(processes, Comparator.comparingInt(p -> p.at));
        int index = 0;
        while (index < n && processes[index].at <= currTime) {
            queue.add(processes[index]);
            index++;
        }
        while (!queue.isEmpty()) {
            Process p = queue.poll();
            if (p.remainingBt > tq) {
                currTime += tq;
                p.remainingBt -= tq;
            } else {
                currTime += p.remainingBt;
                p.remainingBt = 0;
                p.ct = currTime;
                p.tat = p.ct - p.at;
                p.wt = p.tat - p.bt;
                p.completed = true;
                completedCount++;
                avgTat += p.tat;
                avgWt += p.wt;
            }
            while (index < n && processes[index].at <= currTime) {
                queue.add(processes[index]);
                index++;
            }
            if (!p.completed) {
                queue.add(p);
            }
            if (queue.isEmpty() && index < n) {
                currTime = processes[index].at;
                queue.add(processes[index]);
                index++;
            }
        }
        System.out.println("\nID\tAT\tBT\tCT\tTAT\tWT\n");
        for (Process p : processes) {
            System.out.println(p.id + "\t" + p.at + "\t" + p.bt + "\t" + p.ct + "\t" + p.tat + "\t" + p.wt);
        }
        System.out.printf("\nAverage Turn-Around Time: %.2f\n", (avgTat / (float) n));
        System.out.printf("Average Waiting Time: %.2f\n", (avgWt / (float) n));
    }
}
