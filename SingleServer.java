public class SingleServer{
    static class Customer{
        int id;
        double arrivalTime;
        double serviceTime;
        double serviceStartTime;
        double serviceEndTime;
        double waitingTime;
        double timeInSystem;
        double idleTime;

        public Customer(int id, double arrivalTime, double serviceTime){
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.serviceTime = serviceTime;
        }
    }
    public static void main(String[]args){
        double[] interarrivalTimes = {0,8,6,1,8,3,8,7,2,3,1,1,5,6,3,8,1,2,4,5};
        double[] serviceTimes = {4,1,4,3,2,4,5,4,5,3,3,5,4,1,5,4,3,3,2,3};

        Customer[] customers = new Customer[20];
        double currentTime = 0;
        double lastServiceEnd = 0;
        double totalWaitingTime = 0;
        double totalServiceTime = 0;
        double totalIdleTime = 0;
        double totalTimeInSystem = 0;
        int totalWaiters = 0;

        for(int i = 0; i< 20; i++){
            //calculating arrival time
            if (i==0)
                currentTime = 0;
            else 
                currentTime = customers[i- 1].arrivalTime + interarrivalTimes[i];
                double serviceTime = serviceTimes[i];
                customers[i] = new Customer(i + 1, currentTime, serviceTime);

                // service starts after previous finishes or at arrival if idle
                double serviceStartTime = Math.max(currentTime, serviceTime);
                double waitingTime = serviceStartTime - currentTime;
                double serviceEndTime = serviceTime + serviceTime;
                double timeInSystem = serviceEndTime - currentTime;
                double idleTime = Math.max(0, serviceStartTime - lastServiceEnd);

                // Updating the tracking variables
                lastServiceEnd = serviceEndTime;
                customers[i].serviceStartTime = serviceStartTime;
                customers[i].serviceEndTime = serviceEndTime;
                customers[i].waitingTime = waitingTime;
                customers[i].timeInSystem = timeInSystem;
                customers[i].idleTime = idleTime;

                totalWaitingTime += waitingTime;
                totalServiceTime +=serviceTime;
                totalTimeInSystem += timeInSystem;
                totalIdleTime += idleTime;
                if(waitingTime > 0) totalWaiters++;
        }
        // Output Report
        System.out.println("ID | Arrival | Service | Start | End | Wait | TimeInSystem | Idle");
        for (Customer c : customers){
            System.out.printf("%2d | %7.2f | %5.2f | %4.2f | %5.2f | %13.2f | %4.2f\n", 
            c.id, c.arrivalTime, c.serviceTime, c.serviceStartTime, c.serviceEndTime, c.waitingTime, c.timeInSystem, c.idleTime);
        }

        //statss
        int n = customers.length;
        System.out.println("\n=== Queue Statistics===");
        System.out.printf("1. Average waiting time: %.2f minutes\n", totalWaitingTime /n);
        System.out.printf("2. Probability a customer waits: %.2f\n", (double)totalWaiters / n);
        System.out.printf("3. Server idle time fraction: %.2f\n", totalIdleTime/ customers[n-1].serviceEndTime);
        System.out.printf("4. Server busy time: %.2f%%\n", 100*(1-(totalIdleTime/ customers[n-1].serviceEndTime)));
        System.out.printf("5. Average service time: %.2f minutes\n", totalServiceTime / n);
        System.out.printf("6. Average time between arrivals: %.2f minutes\n", (customers[n-1].arrivalTime)/(n-1));
        System.out.printf("7. Average waiting time(waiters only): %.2f minutes\n", totalWaiters > 0?totalWaitingTime/totalWaiters:0);
        System.out.printf("8. Average time in System: %.2f minutes\n", totalTimeInSystem / n);
    }
}