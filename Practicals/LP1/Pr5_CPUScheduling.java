import java.util.*;

public class Pr5_CPUScheduling {
    static class Process {
        int pid, at, bt, rt, wt, tat, priority;
        Process(int pid, int at, int bt, int priority) {
            this.pid = pid;
            this.at = at;
            this.bt = bt;
            this.rt = bt;
            this.priority = priority;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process p[] = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter Arrival Time, Burst Time and Priority for P" + (i + 1) + ":");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int pr = sc.nextInt();
            p[i] = new Process(i + 1, at, bt, pr);
        }

        System.out.println("\n1. FCFS\n2. SJF (Preemptive)\n3. Priority (Non-Preemptive)\n4. Round Robin");
        System.out.print("Choose algorithm: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: fcfs(p); break;
            case 2: sjfPreemptive(p); break;
            case 3: priorityNonPreemptive(p); break;
            case 4:
                System.out.print("Enter time quantum: ");
                int tq = sc.nextInt();
                roundRobin(p, tq);
                break;
            default: System.out.println("Invalid choice!");
        }
    }

    // ---------- FCFS ----------
    static void fcfs(Process p[]) {
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));
        int time = 0;
        for (Process pr : p) {
            if (time < pr.at) time = pr.at;
            pr.wt = time - pr.at;
            time += pr.bt;
            pr.tat = pr.wt + pr.bt;
        }
        printResult(p, "FCFS");
    }

    // ---------- SJF (Preemptive) ----------
    static void sjfPreemptive(Process p[]) {
        int n = p.length;
        int completed = 0, time = 0, minRt, shortest = -1;
        boolean check;
        while (completed != n) {
            minRt = Integer.MAX_VALUE;
            check = false;
            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && p[i].rt > 0 && p[i].rt < minRt) {
                    minRt = p[i].rt;
                    shortest = i;
                    check = true;
                }
            }
            if (!check) {
                time++;
                continue;
            }
            p[shortest].rt--;
            if (p[shortest].rt == 0) {
                completed++;
                int finish = time + 1;
                p[shortest].tat = finish - p[shortest].at;
                p[shortest].wt = p[shortest].tat - p[shortest].bt;
            }
            time++;
        }
        printResult(p, "SJF (Preemptive)");
    }

    // ---------- Priority (Non-Preemptive) ----------
    static void priorityNonPreemptive(Process p[]) {
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));
        int time = 0, completed = 0;
        boolean[] done = new boolean[p.length];

        while (completed != p.length) {
            int idx = -1, highest = Integer.MAX_VALUE;
            for (int i = 0; i < p.length; i++) {
                if (!done[i] && p[i].at <= time) {
                    if (p[i].priority < highest) {
                        highest = p[i].priority;
                        idx = i;
                    }
                }
            }
            if (idx == -1) {
                time++;
                continue;
            }
            done[idx] = true;
            p[idx].wt = time - p[idx].at;
            time += p[idx].bt;
            p[idx].tat = p[idx].wt + p[idx].bt;
            completed++;
        }
        printResult(p, "Priority (Non-Preemptive)");
    }

    // ---------- Round Robin (Preemptive) ----------
    static void roundRobin(Process p[], int tq) {
        Queue<Process> q = new LinkedList<>();
        int time = 0, completed = 0;
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));
        q.add(p[0]);
        int idx = 1;

        while (!q.isEmpty()) {
            Process cur = q.poll();
            if (time < cur.at) time = cur.at;

            int exec = Math.min(cur.rt, tq);
            cur.rt -= exec;
            time += exec;

            // Add processes that have arrived
            while (idx < p.length && p[idx].at <= time) q.add(p[idx++]);

            if (cur.rt > 0)
                q.add(cur);
            else {
                completed++;
                cur.tat = time - cur.at;
                cur.wt = cur.tat - cur.bt;
            }
        }
        printResult(p, "Round Robin");
    }

    // ---------- Print ----------
    static void printResult(Process p[], String algo) {
        System.out.println("\n" + algo + " Scheduling Result:");
        System.out.println("PID\tAT\tBT\tPri\tWT\tTAT");
        float totalWT = 0, totalTAT = 0;
        for (Process pr : p) {
            System.out.println(pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.priority + "\t" + pr.wt + "\t" + pr.tat);
            totalWT += pr.wt;
            totalTAT += pr.tat;
        }
        System.out.printf("Average Waiting Time = %.2f\n", totalWT / p.length);
        System.out.printf("Average Turnaround Time = %.2f\n", totalTAT / p.length);
    }
}