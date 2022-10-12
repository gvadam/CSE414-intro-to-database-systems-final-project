import java.io.*;
import java.util.*;
import java.util.Scanner; 
import java.util.ArrayList;
import java.lang.Math;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class questionFour 
{ 
  public static void main(String[] args) throws Exception 
  { 
      int N = 327;
      int C = 22;

      List<String> citiesL = new ArrayList<String>();
      List<String> carriersL = new ArrayList<String>();
      
      File cityF = new File("C:\\Users\\adamg\\OneDrive\\Desktop\\CSE414\\cse414-hw8-adamg28\\citiesList.txt");
      File carrierF = new File("C:\\Users\\adamg\\OneDrive\\Desktop\\CSE414\\cse414-hw8-adamg28\\carriersList.txt");
      
      // Populate citiesL and carriersL arraylists
      Scanner line1 = new Scanner(cityF);
      while(line1.hasNextLine()){
         String city = line1.nextLine();
         citiesL.add(city);
      }
      Scanner line2 = new Scanner(carrierF);
      while(line2.hasNextLine()){
         String carrier = line2.nextLine();
         carriersL.add(carrier);
      }
      // "X"
      double [][] matrix = new double [N][C]; 
      File file = new File("C:\\Users\\adamg\\OneDrive\\Desktop\\CSE414\\cse414-hw8-adamg28\\data.txt");
      Scanner sc = new Scanner(file);
      
      // scan through each line in the data file
      while(sc.hasNextLine()){
         // scan through one line at a time
         String line = sc.nextLine();      
         Scanner lineScan = new Scanner(line);
         lineScan.useDelimiter(",");
         
         // get origin city, carrier id, and number of flights per line
         String city = lineScan.next();
         String carrier = lineScan.next();
         int flights = lineScan.nextInt();
         
         // get index of city and id and use it as the indexes for the matrix
         int row = citiesL.indexOf(city);
         int col = carriersL.indexOf(carrier);
         
         matrix[row][col] = flights;
      } // end of while loop

      /*
      Let M be the vector of means: M := colSums(X) / N.
      The operation colSums takes the sum of each column of a matrix and returns the result as a vector.
      Each row of M is the average number of flights offered by each carrier across all origin cities.
      M is a size C row vector.
      */
      double [][] M = new double [1][C]; // row vector
      // Add up all the row values for each column and put them in M 
      int pos = 0;
      for(int col = 0; col < matrix[0].length; col++){      
         for(int row = 0; row < matrix.length; row++){
            M[0][pos] += matrix[row][col];
         } 
         pos++;
      }

      // Divide each element in M by N
      for(int i = 0; i < C; i++){
         M[0][i] = M[0][i] / N;
      }
      
      for(int i = 0; i < 1; i ++){
         for(int j = 0; j < C; j++){
            System.out.println(M[i][j] + " ");
         }
         
      }

      /*
      Calculate the covariance matrix as Cov := X'X / N - M'M.
      The ' symbol indicates matrix transpose.
      Writing X'X indicates the matrix multiply of X' and X.
      The result is a size CxC matrix.
      */
      // Get X'
      double[][] transposeX = new double[C][N]; 
      for (int i = 0; i < N; i++) { 
         for (int j = 0; j < C; j++) { 
            transposeX[j][i] = matrix[i][j]; 
         } 
      } 
      // Get M'
      double[][] transposeM = new double[C][1]; 
      for (int i = 0; i < 1; i++) { 
         for (int j = 0; j < C; j++) { 
            transposeM[j][i] = M[i][j]; 
         } 
      } 
      // X'X
      double[][] xx = new double[C][C];
      xx = multiplyMatrices(transposeX, matrix);  
      //System.out.println(xx[0][0] + " " + xx[0][1]);
         
      // X'X / N
      for (int i = 0; i < xx.length; i++) {         
         for (int j = 0; j < xx[i].length; j++) {   
            xx[i][j] = xx[i][j] / N;
         }
      }
      // M'M
      double[][] mm = new double[C][C];
      mm = multiplyMatrices(transposeM, M);
      // X'X / N - M'M
      double cov[][] = new double[C][C];  
      for(int i = 0; i < C; i++){  
         for(int j = 0; j < C; j++){  
             cov[i][j] = xx[i][j] - mm[i][j];  
         }  
      }
      
      /*
      Calculate the correlation matrix as Corr := Cov / sqrt( diag(Cov)'diag(Cov) ).
      The operation diag returns a row vector consisting of the entries on the diagonal of its input.
      The operation sqrt takes the element-wise square root of a matrix.
      The operation / here means element-wise division.
      The result is a size CxC matrix.
      */
      // diag(Cov)
      double[][] diagCov = new double[1][C];
      for (int i = 0; i < C; i++) { 
         for (int j = 0; j < C; j++) { 
             if (i == j) { 
                diagCov[0][i] = cov[i][j]; 
             } 
          } 
      }
      // 'diag(Cov)
      double[][] transposeDiagCov = new double[C][1]; 
      for (int i = 0; i < 1; i++) { 
         for (int j = 0; j < C; j++) { 
            transposeDiagCov[j][i] = diagCov[i][j]; 
         } 
      } 
      // diag(Cov)'diag(Cov)
      double[][] dd = new double[C][C];
      dd = multiplyMatrices(transposeDiagCov, diagCov); 
      // sqrt( diag(Cov)'diag(Cov) )
      for (int i = 0; i < C; i++) { 
         for (int j = 0; j < C; j++) { 
             dd[i][j] = Math.sqrt(dd[i][j]); 
          } 
      }
      // Cov / sqrt( diag(Cov)'diag(Cov) )
      double[][] corr = new double[C][C];
      for (int i = 0; i < C; i++) { 
         for (int j = 0; j < C; j++) { 
            corr[i][j] = cov[i][j] / dd[i][j];
          } 
      }
      
      // Round by 5 decimals
      for (int i = 0; i < C; i++) { 
         for (int j = 0; j < C; j++) { 
            double num = corr[i][j];
            DecimalFormat df = new DecimalFormat("#.#####");
            df.setRoundingMode(RoundingMode.CEILING);
            corr[i][j] = Double.valueOf(df.format(num));
          } 
      }    
      
      /* Print
      for (int i = 0; i < C; i++) { 
         for (int j = 0; j < C; j++) { 
            System.out.println(carriersL.get(i) + ", " + carriersL.get(j) + ", " + corr[i][j]);
          } 
          
      }  */
            
  } 
  public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
    double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
 
    for (int row = 0; row < result.length; row++) {
        for (int col = 0; col < result[row].length; col++) {
            result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
        }
    }
 
    return result;
   }
   public static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
    double cell = 0;
    for (int i = 0; i < secondMatrix.length; i++) {
        cell += firstMatrix[row][i] * secondMatrix[i][col];
    }
    return cell;
   }
} 