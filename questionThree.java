import java.io.*;
import java.util.*;
import java.util.Scanner; 
import java.util.ArrayList;
import java.lang.Math;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class questionThree { 
  
  public static void main(String[] args) throws Exception { 
   int N = 327;
   double[][] A = new double[N][N];
   double[] D = new double[N];
   double[][] v_ = new double[30][N]; // size set at (30, N) because the algorithm only iterates 21 times. gives enough space just in case
   
   File citiesF = new File("C:\\Users\\adamg\\OneDrive\\Desktop\\CSE414\\cse414-hw8-adamg28\\citiesList.txt");
   List<String> citiesL = new ArrayList<String>();
   
   // Populate citiesL arraylist
   Scanner line1 = new Scanner(citiesF);
   while(line1.hasNextLine()){
      String city = line1.nextLine();
      citiesL.add(city);
   }
   
   File flightsF = new File("C:\\Users\\adamg\\OneDrive\\Desktop\\CSE414\\cse414-hw8-adamg28\\flightsList.txt");
   List<String> flightsL = new ArrayList<String>();
   Scanner sc = new Scanner(flightsF);
   
   // Populate A with 1s
   while(sc.hasNextLine()){
      String line = sc.nextLine();      
      Scanner lineScan = new Scanner(line);
      lineScan.useDelimiter(",");
      
      String origin = lineScan.next();
      String dest = lineScan.next();
      
      int row = citiesL.indexOf(origin);
      int col = citiesL.indexOf(dest);
      
      A[row][col] = 1;
   }
 
   // Populate empty indexes in A with 0s
   for(int i = 0; i < N; i++){
      for(int j = 0; j < N; j++){
         if(A[i][j] != 1){
            A[i][j] = 0;
         }
      }
   }  
   
   // Populate D using contents from A
   for(int i = 0; i < N; i++){
      for(int j = 0; j < N; j++){
         D[i] += A[i][j];
      }
   }
   
   // Algorithm from hw spec
   for (int c = 0; c < N; c++){ // initializing pageRank
      v_[0][c] = 1.0 / N; 
      v_[1][c] = 1.0 / N;
   }
   
   double[] current = new double[N];
   double[] prev = new double[N];
   
   int i = 0;
   do {
       i = i + 1;      
       for (int c = 0; c < N; c++) {      
           v_[i][c] = 0.1 / N;      
           for (int d = 0; d < N; d++){
               v_[i][c] = v_[i][c] + 0.9 * A[c][d] * v_[i-1][d] / D[d];                            
           }          
       }    
       // gets only the current row in v 
       for(int x = 0; x < N; x++){
         current[x] = v_[i][x];
       }
       // gets only the previous row in v
       for(int x = 0; x < N; x++){
         prev[x] = v_[i-1][x];
       }
   } while (sum(subtract(current,prev)) > 0.0001);
   
    System.out.println(sum(current)); //converges to 0.9981608921932528
    System.out.println(i); //takes 21 iterations
   
   // Round by 5 decimals
   for (int y = 0; y < N; y++) { 
      double num = current[y];
      DecimalFormat df = new DecimalFormat("#.#####");
      df.setRoundingMode(RoundingMode.CEILING);
      current[y] = Double.valueOf(df.format(num));
   }  
   
   // HashMap containing city name as key and its pagerank as the value
   Map<String, Double> unSortedMap = new HashMap<String, Double>();
   
   // Put data in unSortedMap 
   for(int z = 0; z < N; z++){      
      unSortedMap.put(citiesL.get(z), current[z]);
   }
   
   // New HashMap to contain sorted data
   LinkedHashMap<String, Double> reverseSortedMap = new LinkedHashMap<>();
   
   // Get data from unSortedMap, sort it by decreasing values, and put them in reverseSortedMap
   unSortedMap.entrySet()
    .stream()
    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
    
   // Print city and pagerank in reverseSortedMap
   reverseSortedMap.entrySet().forEach(entry->{
      System.out.println(entry.getKey() + ", " + entry.getValue());  
   });
    
  }
  
  // Subtracts two arrays containing doubles, returns one array containing doubles
  public static double[] subtract(double[] first, double[] second){
   double[] result = new double[first.length];
   for(int i = 0; i < first.length; i++){
      result[i] = first[i] - second[i];
   }
   return result;
  }
  
  // Sums up all the contents in an array containing doubles, returns one double
  public static double sum(double[] v){
   double result = 0;
   for(int i = 0; i < v.length; i++){
      result += Math.abs(v[i]);
   }
   return result;
  }
  
}
