import java.util.Scanner;
class Fcfs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];
        int[] bt = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];
        int[] pid = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.println("\nFor Process " + pid[i] + ":");
            System.out.print("Enter Arrival Time: ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time: ");
            bt[i] = sc.nextInt();
        }

        int currTime = 0;
        int totalTAT = 0, totalWT = 0;

        for (int i = 0; i < n; i++) {
            if (currTime < at[i]) {
                currTime = at[i];
            }

            ct[i] = currTime + bt[i];
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];

            currTime = ct[i];

            totalTAT += tat[i];
            totalWT += wt[i];
        }

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" +
                               ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / (double)n);
        System.out.printf("Average Waiting Time: %.2f\n", totalWT / (double)n);
    }
}
