/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.Rete;


public class Main {

    public static void main(String[] args){
       /* String path = "/Users/Francesco/Documents/Documenti/Esami Magistrale/Algoritmi e Strutture Dati/Progetto/ASD-AC-17-18/src/FileInput/input.txt";
        try {
            Import file = new Import(path);
            Vector<String> vettore = file.apriFile();
            String[] automi = file.getAutomi(vettore);
            String[] link = file.getLink(vettore);
            String[] eventi = file.getEventi(vettore);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        */
//       long tempoInizio = System.currentTimeMillis();
//       long tempoFine;
//        Import.primoScenario();
//        Import.caricaReteDaFile();


        Rete rete = ControllerUtente.start();
//      rete = Import.primoScenario();
//        ControllerUtente.menuRete(rete);
        

//        ControllerUtente.start();
//        tempoFine=System.currentTimeMillis();
//        long differenza = tempoFine - tempoInizio;
//        System.out.println("Tempo impiegato:");
//        System.out.print(differenza);
//        System.out.print("ms");
//        System.out.println();
       
        
        
       // Rete.test();
    }
    
    //prova release 1.0
}
