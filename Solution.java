import java.util.*;

public class Solution {
    static final int MAX_TRACES = 365;

    static String[] traceDiseases(String[] initialStates) {
        List<String> outputStates = new LinkedList<>();
        TravelerManager tm = new TravelerManager();
        for(String s : initialStates) {
            String[] info = s.split(" ");
            tm.addTraveler(
                    new Traveler(
                            info[0],
                            Health.valueOf(info[1]),
                            Arrays.copyOfRange(info, 2, info.length)
                    )
            );
        }
        outputStates.add(tm.getNames());
        outputStates.add(tm.getStatuses());
        while (!tm.isEveryoneHealthy() && outputStates.size() - 1 < MAX_TRACES) {
            tm.updateStates();
            outputStates.add(tm.getStatuses());
        }
        outputStates.add(String.valueOf(outputStates.size() - 1));
        return outputStates.toArray(new String[outputStates.size()]);
    }

    enum Health {
        HEALTHY("HEALTHY"),
        SICK("SICK"),
        RECOVERING("RECOVERING");

        private final String status;
        Health(String status) {
            this.status = status;
        }
    }

    static class Traveler {
        String name;
        Health status;
        String[] travelSchedule;
        int scheduleIterator;

        Traveler(String name, Health status, String[] travelSchedule) {
            this.name = name;
            this.status = status;
            this.travelSchedule = travelSchedule;
            this.scheduleIterator = 0;
        }

        String getLocation() {
            return travelSchedule[scheduleIterator];
        }

        void nextState(boolean isExposed) {
            switch(status) {
                case HEALTHY:
                    status = isExposed ? Health.SICK : Health.HEALTHY;
                    break;
                case SICK:
                    status = Health.RECOVERING;
                    break;
                case RECOVERING:
                    status = Health.HEALTHY;
                    break;
            }
            scheduleIterator = (scheduleIterator + 1) % travelSchedule.length;
        }
    }

    static class TravelerManager {
        List<Traveler> travelers = new ArrayList<>();

        void addTraveler(Traveler traveler) {
            travelers.add(traveler);
        }

        void updateStates() {
            for(Traveler t : travelers) {
                boolean isExposed = t.status.equals(Health.HEALTHY) && isExposedAtLocation(t, t.getLocation());
                t.nextState(isExposed);
            }
        }

        boolean isExposedAtLocation(Traveler affected, String location) {
            for(Traveler t : travelers) {
                if(!t.equals(affected) && t.getLocation().equals(location) && !t.status.equals(Health.HEALTHY))
                    return true;
            }
            return false;
        }

        boolean isEveryoneHealthy() {
            for(Traveler t : travelers) {
                if(!t.status.equals(Health.HEALTHY))
                    return false;
            }
            return true;
        }

        String getNames() {
            StringBuilder names = new StringBuilder();
            for(int i = 0; i < travelers.size(); i++) {
                Traveler t = travelers.get(i);
                names.append(t.name);
                if (i < travelers.size() - 1)
                    names.append(' ');
            }
            return names.toString();
        }

        String getStatuses() {
            StringBuilder statuses = new StringBuilder();
            for(int i = 0; i < travelers.size(); i++) {
                Traveler t = travelers.get(i);
                statuses.append(t.status);
                if (i < travelers.size() - 1)
                    statuses.append(' ');
            }
            return statuses.toString();
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.valueOf(sc.nextLine());
        String[] input = new String[n];
        for(int i = 0; i < n; i++)
            input[i] = sc.nextLine();

        String[] output = traceDiseases(input);
        for(String s : output)
            System.out.println(s);
    }
}
