import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LRUPageReplacement {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int frames, pointer = 0, hit = 0, fault = 0, ref_len;
        int[] buffer;
        int[] reference;
        int[][] mem_layout;
        int[] time; // To track when a page was last used
        int counter = 0;

        System.out.print("Enter the number of Frames: ");
        frames = Integer.parseInt(br.readLine());

        System.out.print("Enter the length of the Reference String: ");
        ref_len = Integer.parseInt(br.readLine());

        reference = new int[ref_len];
        mem_layout = new int[ref_len][frames];
        buffer = new int[frames];
        time = new int[frames];

        for (int j = 0; j < frames; j++) {
            buffer[j] = -1;
            time[j] = 0;
        }

        System.out.println("Enter the Reference String:");
        for (int i = 0; i < ref_len; i++) {
            reference[i] = Integer.parseInt(br.readLine());
        }

        System.out.println("\n--- LRU Page Replacement Simulation ---\n");

        for (int i = 0; i < ref_len; i++) {
            int search = -1;

            // Check if the page is already in buffer (HIT)
            for (int j = 0; j < frames; j++) {
                if (buffer[j] == reference[i]) {
                    search = j;
                    hit++;
                    counter++;
                    time[j] = counter; // Update last used time
                    break;
                }
            }

            // If not found (PAGE FAULT)
            if (search == -1) {
                if (isBufferFull(buffer)) {
                    int pos = findLRU(time); // Find least recently used page index
                    buffer[pos] = reference[i];
                    counter++;
                    time[pos] = counter;
                } else {
                    buffer[pointer] = reference[i];
                    counter++;
                    time[pointer] = counter;
                    pointer++;
                }
                fault++;
            }

            // Store current memory layout
            for (int j = 0; j < frames; j++) {
                mem_layout[i][j] = buffer[j];
            }
        }

        // Print Memory Layout
        System.out.println("Memory Layout (Frame x Time):");
        for (int i = 0; i < frames; i++) {
            for (int j = 0; j < ref_len; j++) {
                if (mem_layout[j][i] == -1)
                    System.out.print(" - ");
                else
                    System.out.printf("%3d ", mem_layout[j][i]);
            }
            System.out.println();
        }

        // Print Stats
        System.out.println("\nTotal Hits   : " + hit);
        System.out.printf("Hit Ratio    : %.2f\n", (float) hit / ref_len);
        System.out.println("Total Faults : " + fault);
    }

    // Function to check if buffer is full
    public static boolean isBufferFull(int[] buffer) {
        for (int j : buffer) {
            if (j == -1)
                return false;
        }
        return true;
    }

    // Function to find index of least recently used page
    public static int findLRU(int[] time) {
        int min = time[0];
        int pos = 0;
        for (int i = 1; i < time.length; i++) {
            if (time[i] < min) {
                min = time[i];
                pos = i;
            }
        }
        return pos;
    }
}
