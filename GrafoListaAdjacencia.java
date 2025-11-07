import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementação da interface Grafo usando Lista de Adjacência.
 *
 * Prós: Eficiente em memória para grafos esparsos (O(N + M)).
 * Listar vizinhos (getVizinhos) é O(grau(v)).
 * Contras: Verificação de aresta é O(grau(v)).
 */
public class GrafoListaAdjacencia implements Grafo {

    // Usamos um array de Listas. Cada índice do array (vértice)
    // armazena uma lista de seus vizinhos.
    private List<Integer>[] listaDeAdjacencia;
    private int numVertices;

    public GrafoListaAdjacencia(int numVertices) {
        this.numVertices = numVertices;
        
        // Inicializa o array (N posições)
        // É preciso usar esse cast em Java
        this.listaDeAdjacencia = (List<Integer>[]) new List[numVertices];
        
        // Inicializa cada elemento do array com uma lista vazia
        for (int i = 0; i < numVertices; i++) {
            this.listaDeAdjacencia[i] = new LinkedList<>(); // LinkedList é bom para adicionar
        }
    }

    @Override
    public int getNumVertices() {
        return this.numVertices;
    }

    @Override
    public void adicionarAresta(int v1, int v2) {
        if (v1 >= 0 && v1 < numVertices && v2 >= 0 && v2 < numVertices) {
            // Adiciona v2 à lista de v1
            this.listaDeAdjacencia[v1].add(v2);
            // Adiciona v1 à lista de v2 (grafo não-direcionado)
            this.listaDeAdjacencia[v2].add(v1);
        }
    }

    @Override
    public Iterable<Integer> getVizinhos(int v) {
        if (v >= 0 && v < numVertices) {
            // Retorna a própria lista de vizinhos (muito eficiente)
            return this.listaDeAdjacencia[v];
        }
        // Retorna uma lista vazia se o vértice for inválido
        return new LinkedList<>();
    }
}