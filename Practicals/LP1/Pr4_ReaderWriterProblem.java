import java.util.concurrent.Semaphore;

class Pr4_ReaderWriterProblem {
    static Semaphore mutex = new Semaphore(1);    // For mutual exclusion
    static Semaphore wrt = new Semaphore(1);      // Controls access to the shared resource
    static int readCount = 0;                     // Number of active readers
    static int sharedData = 0;                    // Shared resource (example data)

    // -------- Reader Class --------
    static class Reader extends Thread {
        int readerId;

        Reader(int id) {
            this.readerId = id;
        }

        public void run() {
            try {
                // Entry Section
                mutex.acquire();
                readCount++;
                if (readCount == 1)
                    wrt.acquire();  // First reader locks the writer
                mutex.release();

                // Critical Section
                System.out.println("Reader " + readerId + " is reading data: " + sharedData);

                // Exit Section
                mutex.acquire();
                readCount--;
                if (readCount == 0)
                    wrt.release();  // Last reader releases the writer
                mutex.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // -------- Writer Class --------
    static class Writer extends Thread {
        int writerId;

        Writer(int id) {
            this.writerId = id;
        }

        public void run() {
            try {
                wrt.acquire();  // Writer wants exclusive access
                sharedData++;
                System.out.println("Writer " + writerId + " wrote data: " + sharedData);
                wrt.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // -------- Main Method --------
    public static void main(String[] args) {
        Reader r1 = new Reader(1);
        Reader r2 = new Reader(2);
        Writer w1 = new Writer(1);
        Writer w2 = new Writer(2);
        Reader r3 = new Reader(3);

        r1.start();
        w1.start();
        r2.start();
        w2.start();
        r3.start();
    }
}