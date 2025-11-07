import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Classe utilitária para ler arquivos de texto contendo listas de arestas
 * e construir um objeto Grafo (Matriz ou Lista).
 * * VERSÃO 2.0: Robusta para pular linhas mal formatadas (lixo de PDF).
 */
public class LeitorDeGrafo {

    public enum TipoRepresentacao {
        LISTA,
        MATRIZ
    }

    /**
     * Carrega um grafo de um arquivo de texto.
     */
    public static Grafo carregarGrafo(String caminhoArquivo, TipoRepresentacao tipo) throws FileNotFoundException {
        
        System.out.println("Iniciando Passso 1: Leitura de " + caminhoArquivo + " para descobrir N...");

        // --- PASSO 1: Descobrir o número de vértices (maxId) ---
        int maxId = -1;
        long linhasLidasPasso1 = 0;
        long linhasIgnoradasPasso1 = 0;

        Scanner scannerPasso1 = new Scanner(new File(caminhoArquivo));

        while (scannerPasso1.hasNextLine()) {
            linhasLidasPasso1++;
            String linha = scannerPasso1.nextLine();
            Scanner scannerLinha = new Scanner(linha);
            
            try {
                if (scannerLinha.hasNextInt()) {
                    int v1 = scannerLinha.nextInt();
                    if (scannerLinha.hasNextInt()) {
                        int v2 = scannerLinha.nextInt();
                        if (v1 > maxId) maxId = v1;
                        if (v2 > maxId) maxId = v2;
                    } else {
                        linhasIgnoradasPasso1++;
                    }
                } else {
                    linhasIgnoradasPasso1++;
                }
            } catch (InputMismatchException e) {
                linhasIgnoradasPasso1++;
            }
            scannerLinha.close();
        }
        scannerPasso1.close();

        if (maxId == -1) {
            throw new RuntimeException("Arquivo de grafo vazio ou em formato incorreto.");
        }

        int numVertices = maxId + 1;
        
        System.out.println("Passo 1 Concluído. Total de linhas lidas: " + linhasLidasPasso1);
        System.out.println("Linhas ignoradas (lixo): " + linhasIgnoradasPasso1);
        System.out.println("N (maxId + 1) = " + numVertices);

        // --- PASSO 2: Instanciar o Grafo ---
        Grafo grafo;
        if (tipo == TipoRepresentacao.LISTA) {
            System.out.println("Criando GrafoListaAdjacencia...");
            grafo = new GrafoListaAdjacencia(numVertices);
        } else {
            System.out.println("Criando GrafoMatrizAdjacencia...");
            grafo = new GrafoMatrizAdjacencia(numVertices);
        }

        // --- PASSO 3: Ler o arquivo novamente e popular o grafo ---
        System.out.println("Iniciando Passo 2: Populando o grafo...");
        Scanner scannerPasso2 = new Scanner(new File(caminhoArquivo));
        long numArestas = 0;
        
        while (scannerPasso2.hasNextLine()) {
            String linha = scannerPasso2.nextLine();
            Scanner scannerLinha = new Scanner(linha);
            
            try {
                if (scannerLinha.hasNextInt()) {
                    int v1 = scannerLinha.nextInt();
                    if (scannerLinha.hasNextInt()) {
                        int v2 = scannerLinha.nextInt();
                        grafo.adicionarAresta(v1, v2);
                        numArestas++;
                    }
                }
            } catch (InputMismatchException e) {
                // Ignora a linha mal formatada
            }
            scannerLinha.close();
        }
        scannerPasso2.close();
        
        System.out.println("Carregamento concluído. Total de arestas lidas: " + numArestas);
        
        return grafo;
    }

    /**
     * Método principal (main) para carregar os grafos e testar algoritmos.
     * ESTE É O NOVO 'main' DO PASSO 7 (FINAL)
     */
    public static void main(String[] args) {
        
        String arquivoColab = "collaboration_graph.txt";
        String arquivoAS = "as_graph.txt";

        try {
            // --- ESTUDO DE CASO 1: collaboration_graph ---
            // (Requer os dados corretos: 99k arestas)
            System.out.println("----- Carregando Grafo: " + arquivoColab + " -----");
            Grafo gColab = carregarGrafo(arquivoColab, TipoRepresentacao.LISTA);
            System.out.println("Vértices no gColab: " + gColab.getNumVertices());
            
            // Pergunta 3 do Estudo de Caso 1
            AlgoritmosGrafo.analisarComponentesConexos(gColab);
            System.out.println("\n\n");


            // --- ESTUDO DE CASO 2: as_graph ---
            // (Requer os dados corretos: 48k arestas)
            System.out.println("----- Carregando Grafo: " + arquivoAS + " -----");
            Grafo gAS = carregarGrafo(arquivoAS, TipoRepresentacao.LISTA);
            System.out.println("Vértices no gAS: " + gAS.getNumVertices());

            // Pergunta 1 do Estudo de Caso 2
            AlgoritmosGrafo.analisarGraus(gAS);
            System.out.println("\n");

            // Pergunta 2 do Estudo de Caso 2
            AlgoritmosGrafo.analisarComponentesConexos(gAS);
            System.out.println("\n");
            
            // Pergunta 3 do Estudo de Caso 2
            if (gAS.getNumVertices() > 1) {
                System.out.println("--- Teste de Distância (BFS) ---");
                int[] distancias = AlgoritmosGrafo.bfs(gAS, 1);
                
                int maiorDistancia = 0;
                for (int i = 0; i < distancias.length; i++) {
                    if (distancias[i] > maiorDistancia) {
                        maiorDistancia = distancias[i];
                    }
                }
                System.out.println("Maior distância (nível) encontrada a partir do vértice 1: " + maiorDistancia);
            }
            System.out.println("\n");
            
            // Pergunta 4 do Estudo de Caso 2 (NOVO E LENTO)
            AlgoritmosGrafo.calcularDiametro(gAS);


        } catch (FileNotFoundException e) {
            System.err.println("\n--- ERRO CRÍTICO ---");
            System.err.println("Arquivo de grafo (.txt) não encontrado.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}