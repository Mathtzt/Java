import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Escalonador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //declaração de variaveis
        int quantum, N, entrada, tempoAtual, execucao, q, quantidadeProcessos, burstNovo;
        int temposFinais[], temposExecucao[];
        ArrayList chegada, burst, processos, cpChegada, cpBurst;
        String ordem;
        double tempoMedioExecucao, tempoMedioEspera, turnaround;
        int contTeste = 0;
        String formato, saida;
        DecimalFormat decimal = new DecimalFormat("0.00");
        //
        System.out.print("Escalonador em Java - Round Robin\n\n");
        System.out.println("Quantos processos deseja armazenar?");
        N = scanner.nextInt();
        System.out.println("Qual o valor de quantum?");
        quantum = scanner.nextInt();

        while (N != 0) {
            contTeste++;
            processos = new ArrayList();
            chegada = new ArrayList();
            burst = new ArrayList();
            ordem = "";
            quantidadeProcessos = N;
            temposFinais = new int[N];
            temposExecucao = new int[N];

            for (int i=0; i<N; i++) {
                //lê e adiciona tempo de ingresso do processo
                System.out.println("Qual o tempo de chegada do P" + (i+1));
                entrada = scanner.nextInt();
                chegada.add(entrada);
                //lê e adiciona qual o burst do processo
                System.out.println("Qual o burst do P" + (i+1));
                entrada = scanner.nextInt();
                burst.add(entrada);
            }

            cpChegada = (ArrayList) chegada.clone();
            cpBurst = (ArrayList) burst.clone();

            //tempo atual = primeiro tempo da lista de processos
            tempoAtual = (int) chegada.get(0);

            while (quantidadeProcessos > 0) {
                //percorre ingressos para achar processos que ingressam nesse tempo
                addProcessosEChegada(N, tempoAtual, chegada, processos);

                //assumindo que o primeiro da lista é o de menor duracao
                if (processos.isEmpty()) {
                    tempoAtual++;
                } else {
                    execucao = (int) processos.remove(0); //remove primeiro processo da lista para executar
                    ordem += "p" + (execucao + 1) + " "; //guarda a ordem de execução dos processos
                    q = quantum;

                    while (q > 0 && (int) burst.get(execucao) > 0) {
                        tempoAtual++;
                        q--;
                        burstNovo = (int) burst.get(execucao) - 1;
                        burst.set(execucao, burstNovo);
                    }

                    if ((int) burst.get(execucao) > 0) {
                        addProcessosEChegada(N, tempoAtual, chegada, processos);
                        processos.add(execucao);
                    } else {
                        temposFinais[execucao] = tempoAtual;
                        quantidadeProcessos--;
                    }
                }
            }
            
            //calculo do tempo de execucao e tempo de espera
            tempoMedioExecucao = 0;
            tempoMedioEspera = 0;
            for (int i=0; i<N; i++) {
                temposExecucao[i] = temposFinais[i] - (int) cpChegada.get(i);
                tempoMedioExecucao += temposExecucao[i];
                tempoMedioEspera += temposExecucao[i] - (int) cpBurst.get(i);
            }

            tempoMedioExecucao /= N;
            tempoMedioEspera /= N;
            System.out.println("PROCESSAMENTO - PART" + contTeste);

            for (int i=0; i<N; i++) {
                turnaround = (int) temposFinais[i] - (int) cpChegada.get(i);
                formato = decimal.format(turnaround);
                saida = "|Turnaround| p" + i + ": " + formato + "ms";
                saida = getReplace(saida);
                System.out.println(saida);
            }

            formato = decimal.format(tempoMedioExecucao);
            saida = "Tempo medio de execucao: " + formato + "s";
            saida = getReplace(saida);
            System.out.println(saida);

            formato = decimal.format(tempoMedioEspera);
            saida = "Tempo medio de espera: " + formato + "s";
            saida = getReplace(saida);
            System.out.println(saida);

            System.out.println("Ordem de execução dos processos: " + ordem);
            System.out.println();
            System.out.println("Quantos processos deseja armazenar?");
            N = scanner.nextInt();
        }
    }

    private static String getReplace(String saida) {
        return saida.replace(".", ",");
    }

    private static void addProcessosEChegada(int n, int tempoAtual, ArrayList chegada, ArrayList processos) {
        for (int i = 0; i< n; i++) {
            int valorChegada = (int) chegada.get(i);
            if (valorChegada != -1 && valorChegada <= tempoAtual) {
                processos.add(i);
                chegada.set(i, -1);
            }
        }
    }
}