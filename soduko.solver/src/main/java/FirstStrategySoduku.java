import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class FirstStrategySoduku extends Sudoku{
//    private int[][] cells;
//    private HashMap<String, Integer> char_to_int = new HashMap<String, Integer>();
//    private HashMap<Integer, String> int_to_char = new HashMap<Integer, String>();
//    private String puzzle;
//    private String charset;
//    private int puzzleSize;
    private int sudokuSum;

    private int[] missingRow;
    private int[] missingCol;
//    private int size;
//
//    HashMap<Integer, Integer> minimumCells = new HashMap<Integer, Integer>();

    public FirstStrategySoduku(int puzzleSize, String map, String puzzle) {
        super(puzzleSize, map, puzzle);
    }

    public void solve() {
        missingCol = new int[this.puzzleSize];
        missingRow = new int[this.puzzleSize];
        int minColIndex = 1000; // Row with minimum missing elements
        int minRowIndex = 1000; // Col with minimum missing elements
        int min = 1000;         // count of minimum missing elements
        int totalMissing = 0;         // count of maximum missing elements
        boolean row = false;    // If its row or col

        for (int i = 0; i < this.puzzleSize; i++) {
            missingRow[i] = missingInRowCount(i);
            missingCol[i] = missingInColCount(i);

            if (min >= missingCol[i]) {
                min = missingCol[i];
                minColIndex = i;
                row = false;
            }

            if (min >= missingRow[i]) {
                min = missingRow[i];
                minRowIndex = i;
                row = true;
            }

            if (missingCol[i] == this.puzzleSize) missingCol[i] = 0;
            totalMissing += missingCol[i];
        }

        if (totalMissing == 0) return;
        if (min > 1) {
            System.out.println(min);
            return;
        } // if the minimum number of missing items in a row or col is more than 1 it can't solve

        int sum = 0;
        int j = -1;
        for (int i = 0; i < this.puzzleSize; i++) {
            if (row) {
                if (this.cells[minRowIndex][i] >= 0) {
                    sum += this.cells[minRowIndex][i];
                } else {
                    j = i;
                }
            }
            if (!row) {
                if (this.cells[i][minColIndex] >= 0) {
                    sum += this.cells[i][minColIndex];
                } else {
                    j = i;
                }
            }
        }

        // Assign the probable value to the cell
        if (row) {
            int value = this.sudokuSum - sum;
            this.cells[minRowIndex][j] = value;
//            System.out.println("Updated Row" + minColIndex + " " + j + " " + value);
        }
        if (!row) {
            int value = this.sudokuSum - sum;
//            System.out.println("Updated Col" + j + " " + minColIndex + " " + value);
            this.cells[j][minColIndex] = value;
        }

        this.solve();
    }

    public void solvePart() {
        // solve a quard for 4*4 or nonet for 9*9
        // if we have a missing value for a cell
        // we find what is missing
        // we find valid values for that column
        // we find valid values for that row

        int min = 1000;
        int row = 1000;
        int col = 1000;
        int a;
        int totalMissing = 0;
        // Find the part of the sudoku with minimum missing pieces
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a = missingInPartCount(i, j);
                if (min > a) {
                    min = a;
                    row = i;
                    col = j;
                }
                // Count the total number of missing items
                if (a == puzzleSize) a = 0;  // because if missing item is zero a larger number is sent by the function
                totalMissing += a;

            }
        }

//        System.out.println("Total missing : " + totalMissing);
        if (totalMissing == 0) return;

        System.out.println("Minimum missing quad : " + row + " , " + col + " : " + min + " values");
        // These are problematic areas resolve them soon
        int[] expectedValues = this.missingInPart(row, col, min);

        for (int i = row * size; i < row * size + size; i++) {
            for (int j = col * size; j < col * size + size; j++) {
                if (this.cells[i][j] < 0) {
                    this.validValuesForCell(i, j, expectedValues);
                }
            }
        }

        this.solvePart();
    }

    public void validValuesForCell(int row, int col, int[] expectedValues) {
        // Finds suitable place for expected value looking through row and col
        // Find all the elements of that row and check if one of expected value is in that row
        // Do above for column also
        int filledCount = 0; // how many missing items was filled
        System.out.println("Hey");
        boolean valid = true;
        int validCount = 0;
        int validValue = 0;
        for (int value : expectedValues) {
            for (int i = 0; i < puzzleSize; i++) {
                if (value == this.cells[row][i]) valid = false;
                if (value == this.cells[i][col]) valid = false;
            }
            if (valid) {
                validCount++;
                validValue = value;
            }
            System.out.println("Value : " + value + "\t(" + row + ", " + col + ")" + "\tCount " + validCount);
        }

        // It has to be at the end of for loop
        if (validCount == 1) {
            this.cells[row][col] = validValue;
            System.out.println(this.toString());
        }
        else{
            System.out.println("Did not apply any value");
        }
    }

    public int[] missingInPart(int r, int c, int missCount) {
//        System.out.println(r + " " + c + " : " + missCount);
        int[] missing = new int[missCount];
        HashSet<Integer> present = new HashSet<>();
        int index = 0;
        for (int i = r * size; i < r * size + size; i++) {
            for (int j = c * size; j < c * size + size; j++) {
                if (this.cells[i][j] >= 0) {
                    present.add(this.cells[i][j]);
                    index++;
                }
            }
        }

        index = 0;
        for (int i = 1; i <= puzzleSize; i++) {
            if (!present.contains(i)) {
                missing[index] = i;
                index++;
            }
        }

        return missing;
    }

//    public int[] missingInRow(int row){}
//    public int[] missingInCol(int col){}

    public int missingInPartCount(int r, int c) {
        int count = 0;
        for (int i = r * size; i < r * size + size; i++) {
            for (int j = c * size; j < c * size + size; j++) {
                if (this.cells[i][j] < 0) count++;
            }
        }
        if (count == 0) {
            return this.puzzleSize;
        } else return count;
    }

    public int missingInRowCount(int r) {
        // No of missing item in given row
        int count = 0;
        for (int i = 0; i < this.puzzleSize; i++) {
            if (this.cells[r][i] < 0) count++;
        }
        if (count == 0) {
            return this.puzzleSize;
        } else return count;
    }

    public int missingInColCount(int c) {
        // No of missing item in given col
        int count = 0;
        for (int i = 0; i < this.puzzleSize; i++) {
            if (this.cells[i][c] < 0) count++;
        }
        if (count == 0) {
            return this.puzzleSize;
        } else return count;
    }



}
