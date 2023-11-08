import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class MaxSideLength{

    public long minSquareLength(long[] P1, long[] P2){
        return Math.max(Math.abs(P1[0] - P2[0]), Math.abs(P1[1] - P2[1]));
    }

    public long closestPair(long[][] X, long[][] Y, int l, int r){
        int n = r - l + 1;

        // base cases
        if(n == 1) return Long.MAX_VALUE;
        if(n == 2) return minSquareLength(X[l], X[r]);
        if(n == 3) return Math.min(Math.min(minSquareLength(X[l], X[l + 1]), minSquareLength(X[l + 1], X[r])), minSquareLength(X[l], X[r]));

        // divide
        int mid = l + n / 2, m = 0;
        for(int i = l; i < mid; i++){
            if(X[i][0] == X[mid][0]){
                m++;
            }
        }
        long[][] YlTemp = new long[Y.length][2];
        long[][] YrTemp = new long[Y.length][2];
        int ylCounter = 0, yrCounter = 0;
        for(long[] longs : Y){
            if(longs[0] < X[mid][0]){
                YlTemp[ylCounter++] = longs;
            }else if(longs[0] > X[mid][0]){
                YrTemp[yrCounter++] = longs;
            }else{
                if(m > 0){
                    m--;
                    YlTemp[ylCounter++] = longs;
                }else{
                    YrTemp[yrCounter++] = longs;
                }
            }
        }
        long[][] Yl = new long[ylCounter][2];
        long[][] Yr = new long[yrCounter][2];
        System.arraycopy(YlTemp, 0, Yl, 0, ylCounter);
        System.arraycopy(YrTemp, 0, Yr, 0, yrCounter);
        long dl = closestPair(X, Yl, l, mid - 1);
        long dr = closestPair(X, Yr, mid, r);
        long d = Math.min(dl, dr);

        // combine
        long[][] S = new long[Y.length][2];
        int index = 0;
        for (long[] longs : Y) {
            if (longs[0] >= X[mid][0] - d && longs[0] <= X[mid][0] + d) {
                S[index][0] = longs[0];
                S[index][1] = longs[1];
                index++;
            }
        }
        for(int i = 0; i < index; i++){
            for(int j = 1; j <= 7; j++){
                if(i + j < index) {
                    long[] t1 = {S[i][0], S[i][1]};
                    long[] t2 = {S[i + j][0], S[i + j][1]};
                    d = Math.min(d, minSquareLength(t1, t2));
                }
            }
        }

        return d;
    }

    public long solve(String inputFile) {
        try {
            File file = new File(inputFile);
            BufferedReader br = new BufferedReader(new FileReader(file));

            int n = Integer.parseInt(br.readLine());
            long[][] X = new long[n][2];
            long[][] Y = new long[n][2];

            for (int i = 0; i < n; i++) {
                String[] point = br.readLine().split(" ");
                X[i][0] = Long.parseLong(point[0]);
                X[i][1] = Long.parseLong(point[1]);
                Y[i][0] = X[i][0];
                Y[i][1] = X[i][1];
            }

            br.close();

            // Compare based on the first element
            // If the first elements are equal, compare based on the second element
            Comparator<long[]> customComparator = Comparator.comparingLong((long[] a) -> a[0]).thenComparingLong(a -> a[1]);
            Arrays.sort(X, customComparator);
            Arrays.sort(Y, Comparator.comparingLong(a -> a[1]));
            return closestPair(X, Y, 0, n - 1);
            
        } catch (IOException e) {
            return -1;
        }
    }
    public static void main(String[] args) {
        MaxSideLength solver = new MaxSideLength();
        solver.solve("C:/Users/moham/Desktop/New Text Document.txt");
    }
}