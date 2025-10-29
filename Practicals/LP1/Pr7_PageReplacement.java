import java.util.*;
public class Pr7_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();
        int pages[] = new int[n];

        System.out.println("Enter page reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        System.out.println("\n--- Page Replacement Algorithms ---");
        System.out.println("1. FIFO");
        System.out.println("2. LRU");
        System.out.println("3. Optimal");
        System.out.print("Choose algorithm: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                fifo(pages, frames);
                break;
            case 2:
                lru(pages, frames);
                break;
            case 3:
                optimal(pages, frames);
                break;
            default:
                System.out.println("Invalid choice!");
        }

        sc.close();
    }

    // ---------- FIFO ----------
    static void fifo(int pages[], int frames) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        int faults = 0;

        System.out.println("\nFIFO Page Replacement Process:");
        for (int p : pages) {
            if (!set.contains(p)) {
                if (set.size() < frames) {
                    set.add(p);
                    queue.add(p);
                } else {
                    int removed = queue.poll();
                    set.remove(removed);
                    set.add(p);
                    queue.add(p);
                }
                faults++;
            }
            System.out.println("Page: " + p + " -> Frames: " + set);
        }
        System.out.println("Total Page Faults (FIFO): " + faults);
    }

    // ---------- LRU ----------
    static void lru(int pages[], int frames) {
        List<Integer> list = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        int faults = 0;

        System.out.println("\nLRU Page Replacement Process:");
        for (int p : pages) {
            if (!set.contains(p)) {
                if (set.size() < frames) {
                    set.add(p);
                    list.add(p);
                } else {
                    int lru = list.remove(0);
                    set.remove(lru);
                    set.add(p);
                    list.add(p);
                }
                faults++;
            } else {
                list.remove((Integer) p);
                list.add(p);
            }
            System.out.println("Page: " + p + " -> Frames: " + list);
        }
        System.out.println("Total Page Faults (LRU): " + faults);
    }

    // ---------- OPTIMAL ----------
    static void optimal(int pages[], int frames) {
        List<Integer> memory = new ArrayList<>();
        int faults = 0;

        System.out.println("\nOptimal Page Replacement Process:");
        for (int i = 0; i < pages.length; i++) {
            int p = pages[i];

            if (!memory.contains(p)) {
                if (memory.size() < frames) {
                    memory.add(p);
                } else {
                    int farthest = -1, indexToReplace = -1;
                    for (int j = 0; j < memory.size(); j++) {
                        int page = memory.get(j);
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < pages.length; k++) {
                            if (pages[k] == page) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            indexToReplace = j;
                        }
                    }
                    memory.set(indexToReplace, p);
                }
                faults++;
            }
            System.out.println("Page: " + p + " -> Frames: " + memory);
        }
        System.out.println("Total Page Faults (Optimal): " + faults);
    }
}
