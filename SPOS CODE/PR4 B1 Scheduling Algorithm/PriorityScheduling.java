import java.util.Scanner;

public class PriorityScheduling {

    public static void main(String[] args) {

        System.out.println("*** Priority Scheduling (Preemptive) ***");
        System.out.print("Enter Number of Process: ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        // All arrays are of size n + 1 to prevent out-of-bounds (we use idx 0..n-1 for processes)
        int process[] = new int[n + 1];
        int arrivaltime[] = new int[n + 1];
        int burstTime[] = new int[n + 1];
        int completionTime[] = new int[n + 1];
        int priority[] = new int[n + 1];
        int TAT[] = new int[n + 1];
        int waitingTime[] = new int[n + 1];
        int burstTimecopy[] = new int[n + 1];

        int min = 0, count = 0;
        int temp, time = 0, end;
        double avgWT = 0, avgTAT = 0;

        for (int i = 0; i < n; i++) {
            process[i] = (i + 1);
            System.out.println("");
            System.out.print("Enter Arrival Time for process " + (i + 1) + ": ");
            arrivaltime[i] = sc.nextInt();
            System.out.print("Enter Burst Time for process " + (i + 1) + " : ");
            burstTime[i] = sc.nextInt();
            System.out.print("Enter Priority for process " + (i + 1) + " : ");
            priority[i] = sc.nextInt();
        }

        // Sort based on arrival time and priority (higher priority value = higher priority in original code)
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arrivaltime[i] > arrivaltime[j] ||
                        (arrivaltime[i] == arrivaltime[j] && priority[j] > priority[i])) {

                    // Swap process IDs
                    temp = process[i];
                    process[i] = process[j];
                    process[j] = temp;

                    // Swap arrival time
                    temp = arrivaltime[i];
                    arrivaltime[i] = arrivaltime[j];
                    arrivaltime[j] = temp;

                    // Swap burst time
                    temp = burstTime[i];
                    burstTime[i] = burstTime[j];
                    burstTime[j] = temp;

                    // Swap priority
                    temp = priority[i];
                    priority[i] = priority[j];
                    priority[j] = temp;
                }
            }
        }

        System.arraycopy(burstTime, 0, burstTimecopy, 0, n);
        priority[n] = 999; // Sentinel value for comparison

        for (time = 0; count != n; time++) {
            min = n; // assume no process found
            for (int i = 0; i < n; i++) {
                if (arrivaltime[i] <= time && burstTime[i] > 0 && priority[i] < priority[min]) {
                    // smaller number in priority[] means higher priority if that's your convention
                    // but your original sort used higher value => higher priority,
                    // so keep this condition consistent with the sentinel (999).
                    min = i;
                }
            }

            // If no process is ready at this time, just continue time (idle CPU)
            if (min == n) {
                continue;
            }

            // Execute one unit of the chosen process (preemptive)
            burstTime[min]--;

            if (burstTime[min] == 0) {
                count++;
                end = time + 1;
                completionTime[min] = end;
                waitingTime[min] = end - arrivaltime[min] - burstTimecopy[min];
                TAT[min] = end - arrivaltime[min];
            }
        }

        for (int i = 0; i < n; i++) {
            avgTAT += TAT[i];
            avgWT += waitingTime[i];
        }

        avgWT /= n;
        avgTAT /= n;

        System.out.println("\n*** Priority Scheduling (Preemptive) ***");
        System.out.printf("%-10s %-13s %-11s %-16s %-15s %-12s%n",
                "Process", "ArrivalTime", "BurstTime", "CompletionTime", "TurnaroundTime", "WaitingTime");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (int i = 0; i < n; i++) {
            System.out.printf("P%-9d %-13d %-11d %-16d %-15d %-12d%n",
                    process[i],
                    arrivaltime[i],
                    burstTimecopy[i],
                    completionTime[i],
                    TAT[i],
                    waitingTime[i]);
        }

        System.out.printf("%nAverage Waiting Time: %.2f ms%n", avgWT);
        System.out.printf("Average Turnaround Time: %.2f ms%n", avgTAT);

        sc.close();
    }
}
