/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import Model.Rete;
import Utilita.InputDati;
import Utilita.MyMenu;
import java.io.IOException;
import java.util.Vector;
import view.Parametri;

public class Main {

    public static void main(String[] args) throws IOException {
        long tempoInizio = System.currentTimeMillis();
        long tempoFine;
        
        Rete rete = ControllerUtente.start();
        rete = Import.primoScenario();
        ControllerUtente.menuRete(rete);
//        Import.primoScenario();
//        Import.caricaReteDaFile();


        // DA SISTEMARE IL CONTEGGIO PERCHE MENU FANNO COMUNQUE SALIRE IL CONTEGGIO DEL TEMPO
//        ControllerUtente.start();
//        tempoFine = System.currentTimeMillis();
//        long differenza = tempoFine - tempoInizio;
//        System.out.println("Tempo impiegato:");
//        System.out.print(differenza);
//        System.out.print("ms");
//        System.out.println();
       
        
        
       // Rete.test();
    }
    
    //prova release 1.0
}
