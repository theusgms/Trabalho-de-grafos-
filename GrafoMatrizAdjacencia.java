import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface Grafo usando Matriz de Adjacência.
 *
 * Prós: Verificação de aresta (existeAresta(v1, v2)?) é O(1).
 * Contras: Requer O(N^2) de memória, mesmo para grafos esparsos.
 * Listar vizinhos (getVizinhos) é O(N).
 */
public class GrafoMatrizAdjacencia implements Grafo {

    private boolean[][] matriz;
    private int numVertices;

    public GrafoMatrizAdjacencia(int numVertices) {
        this.numVertices = numVertices;
        // Java inicializa arrays booleanos com 'false' por padrão.
        this.matriz = new boolean[numVertices][numVertices];
    }

    @Override
    public int getNumVertices() {
        return this.numVertices;
    }

    @Override
    public void adicionarAresta(int v1, int v2) {
        // Validar se os vértices estão dentro dos limites
        if (v1 >= 0 && v1 < numVertices && v2 >= 0 && v2 < numVertices) {
            this.matriz[v1][v2] = true;
            this.matriz[v2][v1] = true; // Grafo não-direcionado
        }
    }

    @Override
    public Iterable<Integer> getVizinhos(int v) {
        List<Integer> vizinhos = new ArrayList<>();
        if (v >= 0 && v < numVertices) {
            // Percorre a linha inteira da matriz para o vértice v
            for (int i = 0; i < numVertices; i++) {
                if (this.matriz[v][i]) {
                    vizinhos.add(i);
                }
            }
        }
        return vizinhos;
    }
}