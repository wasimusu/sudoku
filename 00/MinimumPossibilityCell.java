public class MinimumPossibilityCell extends Sudoku {

    // We need a snapshot of the sudoku to reverse our guessing decision
    // Guess index

    MinimumPossibilityCell(int sudokuSize, String map, String sudoku) {
        super(sudokuSize, map, sudoku);
    }

    public Sudoku solve() {
        return this.minimalOption();
    }

    Sudoku minimalOption() {
        // Loop through the cells
        // Find the possible values for missing cells
        // If it's one - just fill it
        // for each cell determine its quad to determine expected quad values

        // If there is no change between two iterations, return
        this.currentlyMissing = this.getTotalMissingCells();
//        System.out.println("Empty Cells: " + this.currentlyMissing);
        if (this.currentlyMissing == this.previouslyMissing || this.currentlyMissing == 0)
            return new Sudoku(this.getSudokuSize(), this.getCharset(), this.toString());
        ;
        this.previouslyMissing = this.currentlyMissing;

        // Find the block of the sudoku with minimum missing pieces
        // Find expected value for each cell
        for (int i = 0; i < this.sudokuSize; i++) {
            for (int j = 0; j < this.sudokuSize; j++) {
                if (this.cells[i][j] == this.BLANK) fillCell(i, j);
            }
        }
        return minimalOption();
    }

    private void fillCell(int row, int col) {
        // determine the block from the row, col
        // find the missing values for that quad
        // check which of the values are valid for each missing cell in that quad

        int quadRow = Math.floorDiv(row, this.blockSize);
        int quadCol = Math.floorDiv(col, this.blockSize);

        int missingValueSize = this.getMissingInBlockCount(quadRow, quadCol);
        int[] expectedValues = this.missingInBlock(quadRow, quadCol, missingValueSize);

        int validValueCount = 0;
        int validValue = -2;
        boolean isValid;
        for (int value : expectedValues) {
            isValid = this.isValidForCell(row, col, value);
            if (isValid) {
                validValueCount += 1;
                validValue = value;
            }
        }
        if (validValueCount == 1) {
            this.cells[row][col] = validValue;
//            System.out.println(row + ", " + col + " : + " + validValue);
        }
    }


}