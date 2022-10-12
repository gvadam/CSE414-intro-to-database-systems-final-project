# CSE 414 Final Homework 8

**Objectives:**  To integrate skills learned over the course of 414 in solving real-world problems.

**What to turn in:**

1. `hw8-q1.csv`, `hw8-q1.{txt,md,pdf}`
2. `hw8-q2.csv`, `hw8-q2.{txt,md,pdf}`. Include the graph in the PDF or separately as `hw8-q2.png`.
3. `hw8-q3.csv`, `hw8-q3.{txt,md,pdf}`
4. `hw8-q4.csv`, `hw8-q4.{txt,md,pdf}`
5. `hw8-q5.{txt,md,pdf}`. Include the path and minimum miles flown along with your explanation.
6. `hw8-q6.{csv,json}`, `hw8-q6.{txt,md,pdf}`

## Assignment Details

This assignment gives you more freedom than other assignments. 
You may use any resource you wish to answer the questions.
For each question, turn in:

1. Your answer to the question in the file format given.
2. A text (`.txt`), markdown (`.md`), or pdf (`.pdf`) file explaining how you found the answer.

You must include your explanation alongside your answer to receive full credit.

Questions 1 through 5 reference the `flights-small` dataset ([link to compressed CSV files](https://courses.cs.washington.edu/courses/cse414/20wi/flights-small-all.tar.gz)) given in [hw2](https://gitlab.cs.washington.edu/cse414-20wi/source/hw2), [hw3](https://gitlab.cs.washington.edu/cse414-20wi/source/hw3), and [hw5](https://gitlab.cs.washington.edu/cse414-20wi/source/hw5).


1. Define a city's *influx* as its number of incoming flights minus its number of outgoing flights.

    Find the cities with the *least influx* among all cities that have an (incoming or outgoing) flight.
    
    Find the cities with the *greatest influx* among all cities that have an (incoming or outgoing) flight.

    Write your answer in a CSV file that includes the city name and the influx of these cities.


2. Define the *out-degree* of a city as the number of flights departing from that city.

    Define the *out-degree distribution* as a function `f(d)`
    where `d` is an out-degree (minimum 1)
    and `f(d)` is the number of cities that have out-degree `d`.

    *Binning* is the process of grouping together values, often to reduce the number of data points in preparation for plotting a histogram. 

    Plot a histogram of the out-degree distribution of cities, binned into buckets of size 100.
    Let the x-axis plot the bins, labeled with their minimum
    (i.e., the bin at 0 corresponds to out-degrees between 0 and 99; the bin at 100 corresponds to out-degrees between 100 and 199; etc.).
    Let the y-axis plot the number of cities that have an out-degree that fall within a given bucket.

    Write the data used to plot your histogram in a CSV file that includes the out-degree bucket minimum and the number of cities that have an out-degree that falls within that bucket.
    In addition, use your favorite plotting software to plot the histogram. Save an image of your histogram in a PDF or PNG file.


3. Consider the *city adjacency graph*, a *directed graph* defined as follows:
The set of vertices is the set of cities that have at least one (incoming or outgoing) flight.
A directed edge exists from city A to city B if there exists a flight departing from A and arriving at B.

    Compute the PageRank of cities based on the city adjacency graph.
    For background on PageRank, you may wish to consult [Wikipedia](https://en.wikipedia.org/wiki/PageRank).

    The following algorithm computes the PageRank with restart rate `0.1`. 
    The `:=` symbol refers to variable assignment.

    ```
    Let N be the number of cities.
    Let A be the adjacency matrix of 0s and 1s of the city adjacency graph (A is a size NxN matrix).
    Let D be the out-degree vector of the city adjacency graph (D is a size N vector).
    Let v_{0} be a vector indexed by cities (v_{0} is a size N vector).
    
    /* Initialize v. */ 
    for (c in 1..N)
        v_{0}(c) := 1 / N;

    /* Iteratively update v until convergence. */
    i := 0;
    do {
        i := i + 1;
        for (c in 1..N) {
            v_{i}(c) := 0.1 / N;
            for (d in 1..N)
                v_{i}(c) := v_{i}(c) + 0.9 * A(c,d) * v_{i-1}(d) / D(d);
        }
    } while (sum(v_{i} - v_{i-1}) > 0.0001);

    return v_{i};
    ```

    Round each PageRank value to 5 decimal places.
    Write the PageRank data into a CSV file thta includes the city name and the PageRank value. Order the rows of the CSV file by decreasing PageRank value (so that the city with the highest PageRank value appears first).

    In your explanation file, please include how many iterations it took for the PageRank values to converge, if you are able to do so. Otherwise, please state why you are unable to do so.


4. An analyst is interested in studying the similarity between airline carriers. The analyst tasks you with computing the correlation matrix between carriers based on the origin city of flights that they operate. Two carriers are highly correlated if the flights they operate strongly overlap in their origin cities.

    You may wish to read about [correlation matrices](https://en.wikipedia.org/wiki/Correlation_and_dependence#Correlation_matrices) for background.

    The analyst offers you a matrix algorithm to compute the correlation matrix.

    * Let `N` be the number of cities.
    * Let `C` be the number of carriers.
    * Let `X` be the data matrix whose rows are origin cities, whose columns are carrier ids (cid), and whose values are the number of flights from that origin city operated by that carrier. 
    `X` is a size `NxC` matrix.
    * Let `M` be the vector of means: `M := colSums(X) / N`. 
    The operation `colSums` takes the sum of each column of a matrix and returns the result as a vector.
    Each row of `M` is the average number of flights offered by each carrier across all origin cities.
    `M` is a size `C` row vector.
    * Calculate the *covariance matrix* as `Cov := X'X / N - M'M`.
    The `'` symbol indicates matrix transpose. 
    Writing `X'X` indicates the matrix multiply of `X'` and `X`.
    The result is a size `CxC` matrix.
    * Calculate the *correlation matrix* as `Corr := Cov / sqrt( diag(Cov)'diag(Cov) )`.
    The operation `diag` returns a row vector consisting of the entries on the diagonal of its input.
    The operation `sqrt` takes the element-wise square root of a matrix.
    The operation `/` here means element-wise division.
    The result is a size `CxC` matrix.
    * As a sanity check, confirm that the entries on the diagonal of `Corr` have value 1. There is a correlation of 1 between a carrier and itself. All other values should have a correlation between -1 and 1.

    Round the correlation values to 5 decimal places.
    Write the correlation matrix in a CSV file in which each entry is a matrix entry. The first value in each row should be the row carrier id; the second value should be the column carrier id; the third value should be the rounded correlation between the two carriers.


5. An environmentalist wishes to minimize the number of miles they fly.

    Find a path of flights from 'Seattle WA' to 'Twin Falls ID' that has the shortest number of miles flown across all flights in the path.

    Write the path and minimum miles flown in a text file.


6. A historian is interested in studying the gender disparity among Nobel Prize categories. Use the data hosted by the Nobel Prize committee at <https://nobelprize.readme.io/> to answer this question.
    
    The data is available as JSON and CSV. Decide which of the three datasets ([Prize](https://nobelprize.readme.io/docs/prize), [Laureate](https://nobelprize.readme.io/docs/laureate), and [Country](https://nobelprize.readme.io/docs/country)) will help you find an answer. The "Try It Out" tool at the bottom of each page is an easy way to explore and retrieve the data, but you can also query the REST API.

    For each nobel prize category, compute the percentage of female laureates out of all human laureates (you can ignore prizes awarded to organizations).

    Round your percentage (between 0 and 100) to the nearest whole number.
    Write your results in a JSON or CSV file. Include the nobel prize category name and the rounded percentage of female laureates in that category.


