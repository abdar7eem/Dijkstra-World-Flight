import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DijkstraAirTraffic {
    private static final int INF = Integer.MAX_VALUE;
    static City[] cities;
    static LinkedList<City>[] path;
    static LinkedList<Edge>[] graph;
    private static int cityCount;

    public DijkstraAirTraffic(int maxCities) {
        cities = new City[maxCities];
        graph = new LinkedList[maxCities];
        for (int i = 0; i < maxCities; i++) {
            graph[i] = new LinkedList<>();
        }
        cityCount = 0;
    }

    public void addCity(String name, double x, double y) {
        cities[cityCount] = new City(name, x, y, cityCount);
        cityCount++;
    }

    // Haversine distance
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    //        المسافة المركزية =>المسافة الزاوية 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void addEdge(int from, int to, int cost, int time) {
        double distance = calculateDistance(cities[from].x.doubleValue(), cities[from].y.doubleValue(),
                cities[to].x.doubleValue(), cities[to].y.doubleValue());

        graph[from].add(new Edge(to, cost, time, (int) distance));
    }

    public static int findIndex(String city) {
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].name.equals(city)) {
                return i;
            }
        }
        return -1;
    }

    public static LinkedList<City> dijkstra(int source, int destination, int filterOption) {
        int[] dist = new int[cityCount];
        int[] prev = new int[cityCount];
        boolean[] visited = new boolean[cityCount];
    
        for (int i = 0; i < cityCount; i++) {
            dist[i] = INF;
            prev[i] = -1;
        }
        dist[source] = 0;
    
        MinHeap heap = new MinHeap(cityCount);
        heap.insert(source, 0); 
    
        while (!heap.isEmpty()) {
            int[] current = heap.extractMin(); 
            int u = current[0];
    
            if (visited[u]) {
                continue;
            }
            visited[u] = true;
    
            for (int i = 0; i < graph[u].size(); i++) {
                Edge edge = graph[u].get(i);
    
                int weight = 0;
                if (filterOption == 1) {
                    weight = edge.cost;
                } else if (filterOption == 2) {
                    weight = edge.time;
                } else if (filterOption == 3) {
                    weight = edge.distance;
                }
    
                if (dist[u] + weight < dist[edge.to]) {
                    dist[edge.to] = dist[u] + weight;
                    prev[edge.to] = u;
                    heap.insert(edge.to, dist[edge.to]);
                }
            }
        }
    
        return printPath(source, destination, prev, dist, filterOption);
    }
    

    private static LinkedList<City> printPath(int source, int destination, int[] prev, int[] dist, int filterOption) {
        if (dist[destination] == INF) {
            System.out.println("No path found.");
            return null;
        }

        LinkedList<City> path = new LinkedList<>();
        for (int at = destination; at != -1; at = prev[at]) {
            path.addFirst(cities[at]);
        }

        return path;
    }

    public static void readFromFile(File file) {
        try {
            Scanner input = new Scanner(file);
            String[] split = input.nextLine().split(" ");
            int citiesCount = Integer.parseInt(split[0]);
            int edgesCount = Integer.parseInt(split[1]);

            DijkstraAirTraffic dijkstra = new DijkstraAirTraffic(citiesCount);

            for (int i = 0; i < citiesCount; i++) {
                String[] data = input.nextLine().split(" ");
                System.out.println(data[0]);
                dijkstra.addCity(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]));
            }

            if (input.hasNextLine()) {
                String breaker = input.nextLine();
                System.out.println(breaker);
            }

            for (int j = 0; j < edgesCount; j++) {
                String[] data = input.nextLine().split(" ");
                System.out.println(data[0] + " " + data[1] + " ");
                int city1 = dijkstra.findIndex(data[0]);
                int city2 = dijkstra.findIndex(data[1]);
                dijkstra.addEdge(city1, city2, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DijkstraAirTraffic.readFromFile(new File("DATA.txt"));
        // // Example map setup
        // dijkstra.addCity("Country1", 1000, 2400);
        // dijkstra.addCity("Country2", 2800, 3000);
        // dijkstra.addCity("Country3", 2400, 2500);
        // dijkstra.addCity("Country4", 4000, 0);
        // dijkstra.addCity("Country5", 4500, 3800);
        // dijkstra.addCity("Country6", 6000, 1500);

        // dijkstra.addEdge(0, 1, 500, 120, 200);
        // dijkstra.addEdge(0, 3, 350, 90, 150);
        // dijkstra.addEdge(1, 2, 750, 245, 300);
        // dijkstra.addEdge(1, 4, 378, 110, 250);
        // dijkstra.addEdge(2, 4, 1050, 320, 400);
        // dijkstra.addEdge(2, 3, 459, 145, 180);
        // dijkstra.addEdge(2, 5, 528, 186, 320);
        // dijkstra.addEdge(3, 5, 630, 190, 270);
        // dijkstra.addEdge(4, 5, 230, 85, 120);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter source city index: ");
        int source = scanner.nextInt();
        System.out.println("Enter destination city index: ");
        int destination = scanner.nextInt();
        System.out.println("Optimize for (1 = Cost, 2 = Time, 3 = Distance): ");
        int filterOption = scanner.nextInt();

        DijkstraAirTraffic.dijkstra(source, destination, filterOption);
    }
}
