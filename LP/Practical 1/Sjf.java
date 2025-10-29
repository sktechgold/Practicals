import java.util.Scanner;
class Sjf {
    static class Process {
        int id, at, bt, ct, tat, wt;
        boolean completed;

        Process(int id, int at, int bt) {
            this.id = id;
            this.at = at;
            this.bt = bt;
            this.completed = false;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Number of Processes:");
        int n = sc.nextInt();
        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("For Job " + (i + 1) + ": ");
            System.out.print("Enter Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Enter Burst Time: ");
            int bt = sc.nextInt();
            processes[i] = new Process(i + 1, at, bt);
        }
        int currTime = 0, completedCount = 0;
        int avg_tt = 0, avg_wt = 0;
        while (completedCount < n) {
            int idx = -1;
            int min_bt = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!processes[i].completed && processes[i].at <= currTime) {
                    if (processes[i].bt < min_bt) {
                        min_bt = processes[i].bt;
                        idx = i;
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

            avg_tt += p.tat;
            avg_wt += p.wt;
        }
        System.out.println("\nID\tAT\tBT\tCT\tTAT\tWT\n");

	for (int i = 0; i < n; i++) {
    		System.out.println(
        	processes[i].id + "\t" + 
        	processes[i].at + "\t" + 
        	processes[i].bt + "\t" + 
        	processes[i].ct + "\t" + 
        	processes[i].tat + "\t" + 
        	processes[i].wt
    		);
	}
        System.out.println("\nAverage Turn-Around Time: " + (avg_tt / (float) n));
        System.out.println("Average Waiting Time: " + (avg_wt / (float) n));
    }
}

