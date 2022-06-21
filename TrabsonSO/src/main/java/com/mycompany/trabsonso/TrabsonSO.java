package com.mycompany.trabsonso;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Scanner;

public class TrabsonSO {

    public static void main(String[] args) {
        Operacoes operacao = new Operacoes();
        Scanner scan = new Scanner(System.in);
        ArrayList<String> nomes = new ArrayList<>();
        
        Semaphore semaphore = new Semaphore(1);
        ProcessadorThread NPC = new ProcessadorThread(1, semaphore); 
        ProcessadorThread Jogador = new ProcessadorThread(2, semaphore); 

        operacao.adicionarNomes(nomes); // Adiciona vários nomes aleatorios a nosso Array de nomes
        Random rand = new Random();
        int n1, n2, resultado, resposta, pontoNpc = 0, pontoJogador = 0, nperg = 1;
        
        operacao.apresentadorFala1();
        
        /*Seleciona um nome aleatório para o nosso npc*/
        String nomeNpc = nomes.get(rand.nextInt(nomes.size()));
        
        operacao.npcFala1(nomeNpc);
        operacao.apresentadorFala2(nomeNpc);
        
        /*Lê o nome do usuário*/
        String nomeJogador = scan.nextLine();
        
        operacao.apresentadorFala3(nomeJogador);
        
        System.out.println("####### APERTE ENTER ASSIM QUE VER O AVISO DE VALENDO #######");        
        NPC.start();
        Jogador.start();
        
        while(nperg < 11){
            n1 = rand.nextInt(1, 1000);
            n2 = rand.nextInt(1, 1000);
            resultado = n1 + n2;
            operacao.pergunta(n1, n2, nperg);
            nperg++;
            
            long startTime = System.currentTimeMillis();
            try{System.in.read();} // Esta área recupera
            catch(IOException e){} // o tempo de reação do usuário
            long endTime = System.currentTimeMillis();
            
            long reactTime = endTime - startTime; // Tempo de reação do usuário
            long timeRand = rand.nextLong(800, 1400);
            
            if(timeRand < reactTime){ // Compara a reação do usuário com o random time da máquina, caso maior o NPC responde.
                int rng = rand.nextInt(0, 100); // Gera um RNG para ter a possibilidade do npc errar (se rng > 85 = npc erra, logo 15% de chance de erro)
                resposta = NPC.npcApertar(nomeNpc, rng, resultado); //Nesta parte o npc tem o acquire do semaphore, dando release após sua execução
                if (resposta == resultado){
                    System.out.println("Apresentador: Certa resposta!\n");  
                    pontoNpc++; // Caso npc acerte a resposta, ganha ponto
                }else{
                    System.out.println("Apresentador: Errou!! A resposta correta é: " + resultado + "\n");  
                }
            }else{ // Caso o tempo de reação seja menor que o da máquina, o usuário responde
                Jogador.jogadorApertar(nomeJogador); //Nesta parte o jogador tem o acquire do semaphore, dando release após sua execução
                resposta = scan.nextInt();
                if (resposta == resultado){
                    System.out.println("Apresentador: Certa resposta!\n");  
                    pontoJogador++; // Caso o usuário acerte a resposta, ganha ponto
                }else{
                    System.out.println("Apresentador: Errou!! A resposta correta é: " + resultado + "\n");  
                }
            }
        }
        // Ao fim ds perguntas, o vencedor é anunciado.
        operacao.apresentaVencedor(pontoJogador, nomeJogador, pontoNpc, nomeNpc);
    }
}