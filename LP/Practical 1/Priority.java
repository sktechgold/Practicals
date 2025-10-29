import java.util.Scanner;
class Priority {
    static class Process {
        int id, at, bt, ct, tat, wt, priority;
        boolean completed;

        Process(int id, int at, int bt, int priority) {
            this.id = id;
            this.at = at;
            this.bt = bt;
            this.priority = priority;
            this.completed = false;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Number of Processes:");
        int n = sc.nextInt();
        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("For Process " + (i + 1) + ":");
            System.out.print("Enter Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Enter Burst Time: ");
            int bt = sc.nextInt();
            System.out.print("Enter Priority (lower number = higher priority): ");
            int priority = sc.nextInt();
            processes[i] = new Process(i + 1, at, bt, priority);
        }
        int currTime = 0, completedCount = 0;
        int avg_tat = 0, avg_wt = 0;
        while (completedCount < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!processes[i].completed && processes[i].at <= currTime) {
                    if (processes[i].priority < highestPriority) {
                        highestPriority = processes[i].priority;
                        idx = i;
                    } else if (processes[i].priority == highestPriority) {
                        // Tie-breaking: shorter burst time
                        if (idx == -1 || processes[i].bt < processes[idx].bt) {
                            idx = i;
                        }
                    }
                }
            }
            if (idx == -1) {
                currTime++;
                continue;
            }
            Process p = processes[idx];
            currTime += p.bt;
            p.ct = currTime;
            p.tat = p.ct - p.at;
            p.wt = p.tat - p.bt;
            p.completed = true;
            completedCount++;
            avg_tat += p.tat;
            avg_wt += p.wt;
        }
        System.out.println("\nID\tAT\tBT\tPriority\tCT\tTAT\tWT\n");
        for (int i = 0; i < n; i++) {
            Process p = processes[i];
            System.out.println(
                p.id + "\t" + 
                p.at + "\t" + 
                p.bt + "\t" + 
                p.priority + "\t\t" + 
                p.ct + "\t" + 
                p.tat + "\t" + 
                p.wt
            );
        }
        System.out.printf("\nAverage Turn-Around Time: %.2f\n", (avg_tat / (float) n));
        System.out.printf("Average Waiting Time: %.2f\n", (avg_wt / (float) n));
    }
}
