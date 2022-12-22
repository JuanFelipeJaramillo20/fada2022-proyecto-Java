package proyecto;

import javax.naming.OperationNotSupportedException;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SparseMatrixCSC {
    private LoadFile loader = LoadFile.getInstance();
    private int[][] matrix;
    @Getter
    private int[] rows;
    @Getter
    private int[] columns;
    @Getter

    private int[] values;

    public void createRepresentation(String inputFile) throws OperationNotSupportedException, FileNotFoundException {
        // Load data
        loader.loadFile(inputFile);
        matrix = loader.getMatrix();
        LinkedList<Integer> values = new LinkedList<Integer>();
        LinkedList<Integer> rows = new LinkedList<Integer>();
        this.columns = new int[this.matrix[0].length + 1];
        int cnt = 0;
        // se recorre la matriz y se buscan los indices ya que se va a hacer busqueda
        // por columnas
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != 0) {
                    values.add(matrix[j][i]);
                    rows.add(j);
                    cnt++; // el contador cuenta cuantos numeros hay por columna
                }
            }
            if (i == 0) {
                this.columns[1] = cnt;
                cnt = 0;
            } else {
                this.columns[i + 1] = this.columns[i] + cnt; // asi añadimos los valores a las columnas, siempre
                                                             // contando con 2 indices
                cnt = 0;
            }
        }

        // Valores
        this.values = new int[values.size()];
        int i = 0;
        for (int val : values) {
            this.values[i] = val;
            i++;
        }

        // Filas
        this.rows = new int[rows.size()];
        i = 0;
        for (int row : rows) {
            this.rows[i] = row;
            i++;
        }
    }

    public int getElement(int i, int j) throws OperationNotSupportedException {

        for (int z = this.columns[j]; z < this.columns[j + 1]; z++) {
            if (this.rows[z] == i) {
                return this.values[z];
            }
        }
        return 0;
    }

    /*
     * Este metodo es casi igual que obtener elemento solo que si esta en la fila
     * ingresada
     * lo añade al array, aprovechando que al crear un array es lleno d eceros
     * solo agregamos
     * los que tengan valor
     */
    public int[] getRow(int i) throws OperationNotSupportedException {
        int[] rowValue = new int[this.columns.length - 1];
        for (int j = 0; j < rowValue.length; j++) {
            for (int z = this.columns[j]; z < this.columns[j + 1]; z++) {
                if (this.rows[z] == i) {
                    rowValue[j] = this.values[z];
                    continue;
                }
            }
        }
        return rowValue;
    }

    public int[] getColumn(int j) throws OperationNotSupportedException {
        int[] colValue = new int[mayor(this.rows) + 1];
        // #este metodo es parecido al de obtener fila pero tenemos que iterar sobre las
        // columnas

        for (int i = 0; i < this.columns.length; i++) {
            if (i == j) {
                for (int z = this.columns[i]; z < this.columns[i + 1]; z++) {
                    colValue[this.rows[z]] = this.values[z];

                }
            }
        }
        return colValue;
    }

    public void setValue(int i, int j, int value) throws OperationNotSupportedException {
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> rows = new ArrayList<Integer>();

        int a = 0;
        for (int r : this.rows) {
            rows.add(r);
            a++;
        }

        a = 0;
        for (int v : this.values) {
            values.add(v);
            a++;
        }

        a = 0;
        while (a < this.columns.length) {
            if (a == j) {
                int lim1 = this.columns[a];
                int lim2 = this.columns[a + 1] - 1;
                while (lim1 <= lim2) {
                    if (rows.get(lim1) == i) {
                        values.add(lim1, value);
                        break;
                    } else {
                        lim1++;
                    }
                }
                while (a < this.columns.length - 1) {
                    int valor = this.columns[a + 1] + 1;
                    this.columns[a + 1] = valor;
                    a++;
                }
                lim1 = this.columns[j];
                lim2 = this.columns[j + 1] - 1;
                while (lim1 <= lim2) {
                    if (rows.get(lim1) < i) {
                        lim1++;
                    } else {
                        rows.add(lim1, i);
                        values.add(lim1, value);
                        break;
                    }
                }
                break;
            } else {
                a++;
            }
        }

        this.values = new int[values.size()];
        this.rows = new int[rows.size()];

        a = 0;
        for (int v : values) {
            this.values[a] = v;
            a++;
        }

        a = 0;
        for (int r : rows) {
            this.rows[a] = r;
            a++;
        }
    }

    /*
     * This method returns a representation of the Squared matrix
     *
     * @return object that contests the squared matrix;
     */
    public SparseMatrixCSC getSquareMatrix() throws OperationNotSupportedException {
        SparseMatrixCSC squaredMatrix = new SparseMatrixCSC();
        squaredMatrix.columns = this.columns;
        squaredMatrix.rows = this.rows;
        int[] values = new int[this.values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) Math.pow(this.values[i], 2);
        }
        squaredMatrix.values = values;
        return squaredMatrix;
    }

    /*
     * This method returns a representation of the transposed matrix
     *
     * @return object that contests the transposed matrix;
     */
    public SparseMatrixCSC getTransposedMatrix() throws OperationNotSupportedException {
        SparseMatrixCSC transposedMat;
        int[][] matrix = new int[this.columns.length - 1][mayor(this.rows) + 1];
        for (int i = 0; i < this.columns.length - 1; i++) {
            for (int z = this.columns[i]; z < this.columns[i + 1]; z++) {
                matrix[i][this.rows[z]] = this.matrix[this.rows[z]][i];
            }
        }

        transposedMat = createRepresentationTrans(matrix);
        return transposedMat;
    }

    // Métodos útiles
    private int mayor(int[] a) {
        int mayor = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > mayor) {
                mayor = a[i];
            }
        }
        return mayor;
    }

    public SparseMatrixCSC createRepresentationTrans(int[][] matrix) {
        SparseMatrixCSC matCSC = new SparseMatrixCSC();
        int[] transValues;
        int[] transRow;
        LinkedList<Integer> values = new LinkedList<Integer>();
        LinkedList<Integer> rows = new LinkedList<Integer>();
        int[] transColumns = new int[matrix[0].length + 1];
        int cnt = 0;
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != 0) {
                    values.add(matrix[j][i]);
                    rows.add(j);
                    cnt++;
                }
            }
            if (i == 0) {
                transColumns[1] = cnt;
                cnt = 0;
            } else {
                transColumns[i + 1] = transColumns[i] + cnt;
                cnt = 0;
            }
        }

        // Valores
        transValues = new int[values.size()];
        int i = 0;
        for (int val : values) {
            transValues[i] = val;
            i++;
        }

        // Filas
        transRow = new int[rows.size()];
        i = 0;
        for (int row : rows) {
            transRow[i] = row;
            i++;
        }
        matCSC.columns = transColumns;
        matCSC.rows = transRow;
        matCSC.values = transValues;
        matCSC.matrix = matrix;

        return matCSC;
    }
}
