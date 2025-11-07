import java.util.Iterable;

/**
 * Interface que define as operações básicas de um grafo não-direcionado.
 * Os vértices são representados por inteiros de 0 a N-1, onde N é getNumVertices().
 */
public interface Grafo {

    /**
     * Retorna o número total de vértices no grafo (N).
     */
    int getNumVertices();

    /**
     * Adiciona uma aresta não-direcionada entre os vértices v1 e v2.
     */
    void adicionarAresta(int v1, int v2);

    /**
     * Retorna um iterável (como uma Lista) contendo todos os vizinhos
     * do vértice v.
     */
    Iterable<Integer> getVizinhos(int v);

    // Futuramente, você pode adicionar mais métodos aqui, como:
    // int getNumArestas();
    // int getGrau(int v);
}