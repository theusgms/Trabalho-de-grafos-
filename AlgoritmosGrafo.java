import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe utilitária para executar algoritmos em grafos.
 */
public class AlgoritmosGrafo {

    /**
     * Versão pública da BFS (que imprime no console).
     */
    public static int[] bfs(Grafo grafo, int inicio) {
        // Chama a versão principal, NÃO silenciosa
        return bfs(grafo, inicio, false); 
    }

    /**
     * Implementação principal da Busca em Largura (BFS).
     *
     * @param grafo   O grafo
     * @param inicio  O vértice inicial
     * @param silent  Se true, não imprime nada no console (útil para chamadas repetidas)
     * @return Array de distâncias (dist[i] = -1 se inalcançável)
     */
    private static int[] bfs(Grafo grafo, int inicio, boolean silent) {
        int numVertices = grafo.getNumVertices();
        
        Queue<Integer> fila = new LinkedList<>();
        int[] distancias = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            distancias[i] = -1;
        }

        distancias[inicio] = 0;
        fila.add(inicio);

        if (!silent) {
            System.out.println("Iniciando BFS a partir do vértice " + inicio + "...");
        }

        while (!fila.isEmpty()) {
            int v = fila.poll();

            for (int u : grafo.getVizinhos(v)) {
                if (distancias[u] == -1) {
                    distancias[u] = distancias[v] + 1;
                    fila.add(u);
                }
            }
        }

        if (!silent) {
            System.out.println("BFS concluída.");
        }
        return distancias;
    }

    /**
     * Encontra e analisa os componentes conexos de um grafo.
     */
    public static void analisarComponentesConexos(Grafo grafo) {
        System.out.println("Iniciando análise de Componentes Conexos...");
        
        int numVertices = grafo.getNumVertices();
        boolean[] visitadoGlobal = new boolean[numVertices];
        
        int numComponentes = 0;
        int tamanhoMaior = 0;
        int tamanhoMenor = Integer.MAX_VALUE; 

        for (int v = 0; v < numVertices; v++) {
            if (!visitadoGlobal[v]) {
                numComponentes++; 
                int[] distancias = bfs(grafo, v, true); // true = silent
                
                int tamanhoComponenteAtual = 0;
                for (int j = 0; j < numVertices; j++) {
                    if (distancias[j] != -1) {
                        visitadoGlobal[j] = true; 
                        tamanhoComponenteAtual++;
                    }
                }
                
                if (tamanhoComponenteAtual > tamanhoMaior) {
                    tamanhoMaior = tamanhoComponenteAtual;
                }
                if (tamanhoComponenteAtual < tamanhoMenor) {
                    tamanhoMenor = tamanhoComponenteAtual;
                }
            }
        } 

        System.out.println("--- Resultado da Análise de Componentes ---");
        System.out.println("Número total de Componentes Conexos: " + numComponentes);
        System.out.println("Tamanho do MAIOR componente: " + tamanhoMaior + " vértices");
        System.out.println("Tamanho do MENOR componente: " + tamanhoMenor + " vértices");
    }

    /**
     * Analisa os graus de todos os vértices do grafo.
     */
    public static void analisarGraus(Grafo grafo) {
        System.out.println("Iniciando análise de Graus...");
        int numVertices = grafo.getNumVertices();
        
        if (numVertices == 0) {
            System.out.println("Grafo está vazio.");
            return;
        }

        int minGrau = Integer.MAX_VALUE;
        int maxGrau = 0;
        long somaGraus = 0;

        for (int v = 0; v < numVertices; v++) {
            int grauAtual = 0;
            for (int vizinho : grafo.getVizinhos(v)) {
                grauAtual++;
            }
            
            somaGraus += grauAtual;
            
            if (grauAtual > maxGrau) {
                maxGrau = grauAtual;
            }
            
            if (grauAtual < minGrau) {
                minGrau = grauAtual;
            }
        }

        double grauMedio = (double)somaGraus / numVertices;

        System.out.println("--- Resultado da Análise de Graus ---");
        System.out.println("Grau MÁXIMO encontrado: " + maxGrau);
        System.out.println("Grau MÍNIMO encontrado: " + minGrau);
        System.out.println("Grau MÉDIO: " + String.format("%.2f", grauMedio));
        System.out.println("Comparação: O maior grau possível (num grafo completo) seria N-1 = " + (numVertices - 1));
    }

    /**
     * Calcula o diâmetro do grafo (a maior das menores distâncias).
     * AVISO: Este é um algoritmo MUITO LENTO (O(N*(N+M))).
     * (ADICIONADO NO PASSO 7)
     *
     * @param grafo O grafo a ser analisado.
     */
    public static void calcularDiametro(Grafo grafo) {
        System.out.println("Iniciando cálculo do Diâmetro...");
        System.out.println("AVISO: Este processo é MUITO LENTO e pode demorar de 5 a 10 minutos.");
        
        int numVertices = grafo.getNumVertices();
        int diametro = 0; // O diâmetro do grafo

        // Itera por CADA vértice 'v' no grafo
        for (int v = 0; v < numVertices; v++) {
            
            // --- Otimização ---
            // Se o vértice 'v' for isolado (grau 0), pulamos ele.
            // (Baseado no seu log anterior, sabemos que o minGrau é 0)
            boolean temVizinhos = false;
            for (int vizinho : grafo.getVizinhos(v)) {
                temVizinhos = true;
                break; // Só precisamos saber se tem pelo menos um
            }
            if (!temVizinhos) {
                continue; // Pula este vértice isolado
            }

            
            // 1. Roda uma BFS a partir de 'v' (usando a versão silenciosa)
            int[] distancias = bfs(grafo, v, true);
            
            int maiorDistanciaDoVerticeV = 0; // "Excentricidade" de v
            
            // 2. Encontra a maior distância a partir de 'v'
            for (int d : distancias) {
                // Ignoramos os inalcançáveis (d == -1)
                if (d > maiorDistanciaDoVerticeV) {
                    maiorDistanciaDoVerticeV = d;
                }
            }
            
            // 3. O diâmetro é o "máximo das máximas distâncias"
            if (maiorDistanciaDoVerticeV > diametro) {
                diametro = maiorDistanciaDoVerticeV;
            }

            // Imprime o progresso para o utilizador não achar que o programa travou
            if (v > 0 && v % 1000 == 0) {
                System.out.println("... Diâmetro parcial: " + diametro + " (processado " + v + "/" + numVertices + " vértices)");
            }
        }
        
        System.out.println("--- Resultado da Análise do Diâmetro ---");
        System.out.println("Diâmetro do grafo (maior caminho mínimo): " + diametro);
    }
}