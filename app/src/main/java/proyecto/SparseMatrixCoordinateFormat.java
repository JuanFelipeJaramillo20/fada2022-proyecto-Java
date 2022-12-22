package proyecto;

import javax.naming.OperationNotSupportedException;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList;

import java.io.FileNotFoundException;

public class SparseMatrixCoordinateFormat {

    private LoadFile loader = LoadFile.getInstance();
    @Setter
    private int[][] matrix;
    @Getter
    @Setter
    private int[] rows;
    @Getter
    @Setter
    private int[] columns;
    @Getter
    @Setter
    private int[] values;

    public void createRepresentation(String inputFile) throws OperationNotSupportedException, FileNotFoundException {
        // Load data
        loader.loadFile(inputFile);
        matrix = loader.getMatrix();

        LinkedList<Integer> valores = new LinkedList<Integer>();
        LinkedList<Integer> filas = new LinkedList<Integer>();
        LinkedList<Integer> columnas = new LinkedList<Integer>();

        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                if (this.matrix[i][j] != 0) {
                    valores.add(this.matrix[i][j]);
                    filas.add(i);
                    columnas.add(j);
                }
            }
        }

        this.rows = filas.stream().mapToInt(i -> i).toArray();
        this.columns = columnas.stream().mapToInt(i -> i).toArray();
        this.values = valores.stream().mapToInt(i -> i).toArray();
    }

    public int getElement(int i, int j) throws OperationNotSupportedException {
        for (int k = 0; k < this.values.length; k++) {
            if (this.rows[k] == i && this.columns[k] == j) {
                return this.values[k];
            }
        }
        return 0;
    }

    public int[] getRow(int i) throws OperationNotSupportedException {
        int[] fila = new int[this.matrix[0].length];
        for (int j = 0; j < this.rows.length; j++) {
            if (this.rows[j] == i) {
                fila[this.columns[j]] = this.values[j];
            }
        }
        return fila;
    }

    public int[] getColumn(int j) throws OperationNotSupportedException {
        int[] columna = new int[this.matrix.length];
        for (int i = 0; i < this.columns.length; i++) {
            if (this.columns[i] == j) {
                columna[this.rows[i]] = this.values[i];
            }
        }
        return columna;
    }

    public void setValue(int i, int j, int value) throws OperationNotSupportedException {
        // Cambiar los atributos rows, cols, values y matrix aqui
        this.matrix[i][j] = value;
        int[] newCol = new int[this.columns.length + 1];
        int[] newFil = new int[this.rows.length + 1];
        int[] newValue = new int[this.values.length + 1];

        // Llenar las nuevas listas, añadiendo el dato ingresado
        int cnt = 0; // para saber cuantos elementos hay en la fila que se va a insertar el nuevo
                     // dato
        int whereBegins = 0;
        for (int r = 0; r < this.rows.length; r++) {
            if (this.rows[r] == i) {
                cnt++;
                if (cnt == 1) {
                    whereBegins = r;
                }
            }
        }

        int locationDato = whereBegins;
        for (int r = whereBegins; r < whereBegins + cnt; r++) {
            if (locationDato < this.columns[r]) {
                locationDato = this.columns[r];
            }
        }
        newFil[locationDato] = i;
        newCol[locationDato] = j;
        newValue[locationDato] = value;

        for (int r = 0; r < locationDato; r++) {
            newFil[r] = this.rows[r];
            newCol[r] = this.columns[r];
            newValue[r] = this.values[r];
        }

        for (int r = locationDato + 1; r <= this.rows.length; r++) {
            newFil[r] = this.rows[r - 1];
            newCol[r] = this.columns[r - 1];
            newValue[r] = this.values[r - 1];
        }

        this.rows = newFil;
        this.columns = newCol;
        this.values = newValue;

    }

    public void rebuildMatrixRepresentation() {
        LinkedList<Integer> valores = new LinkedList<Integer>();
        LinkedList<Integer> filas = new LinkedList<Integer>();
        LinkedList<Integer> columnas = new LinkedList<Integer>();

        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                if (this.matrix[i][j] != 0) {
                    valores.add(this.matrix[i][j]);
                    filas.add(i);
                    columnas.add(j);
                }
            }
        }

        this.rows = filas.stream().mapToInt(i -> i).toArray();
        this.columns = columnas.stream().mapToInt(i -> i).toArray();
        this.values = valores.stream().mapToInt(i -> i).toArray();
    }

    /*
     * This method returns a representation of the Squared matrix
     *
     * @return object that contests the squared matrix;
     */
    public SparseMatrixCoordinateFormat getSquareMatrix() throws OperationNotSupportedException {
        SparseMatrixCoordinateFormat squaredMatrix = new SparseMatrixCoordinateFormat();
        squaredMatrix.setColumns(this.columns);
        squaredMatrix.setRows(this.rows);
        int[] values = new int[this.values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) Math.pow(this.values[i], 2);
        }
        squaredMatrix.setValues(values);
        return squaredMatrix;
    }

    /*
     * This method returns a representation of the transposed matrix
     *
     * @return object that contests the transposed matrix;
     */
    public SparseMatrixCoordinateFormat getTransposedMatrix() throws OperationNotSupportedException {
        SparseMatrixCoordinateFormat transposedMatrix = new SparseMatrixCoordinateFormat();
        // Usar los metodos Set aqui de los atributos
        int[] transRow = new int[this.rows.length];
        int[] transCol = new int[this.columns.length];
        int[] values = new int[this.values.length];
        int[][] matrix = new int[this.columns.length][this.rows.length];
        for (int i = 0; i < matrix.length; i++) {
            matrix[this.columns[i]][this.rows[i]] = this.matrix[this.rows[i]][this.columns[i]];

        }
        transposedMatrix.setMatrix(matrix);
        int z = 0; // iterador para las listas de columnas y filas
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    transRow[z] = i;
                    transCol[z] = j;
                    values[z] = matrix[i][j];
                    z++;
                }
            }
        }
        transposedMatrix.setRows(transRow);
        transposedMatrix.setColumns(transCol);
        transposedMatrix.setValues(values);
        return transposedMatrix;
    }

}
