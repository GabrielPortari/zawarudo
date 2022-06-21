package com.mycompany.trabsonso;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class ProcessadorThread extends Thread {
    int idThread;
    Semaphore semaforo;
    
    public ProcessadorThread(int id, Semaphore semaphore) {
        this.idThread = id;
        this.semaforo = semaphore;
    }
    public int npcApertar(String nomeNpc, int rng, int resposta) {
        try {
            semaforo.acquire();
            System.out.println(nomeNpc + " apertou o botão primeiro !");
            if(rng > 85){
                Random rand = new Random();
                resposta = resposta + rand.nextInt(1, 5);
                System.out.println(nomeNpc + ": Essa ta dificil, eu não sei a resposta, vou chutar " + resposta);
            }else{
                System.out.println(nomeNpc + ": Facil, a resposta é " + resposta);
            }
        } catch (InterruptedException e) {
        } finally {
            semaforo.release();
        }
        return resposta;
    }
     public void jogadorApertar(String nomeJogador) {
        try {
            semaforo.acquire();
            System.out.println(nomeJogador + " apertou o botão primeiro !");
            System.out.print(nomeJogador + ": Essa é facil, a resposta é ");
        } catch (InterruptedException e) {
        } finally {
            semaforo.release();
        }
    }
}