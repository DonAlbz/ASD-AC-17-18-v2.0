/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.io.IOException;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {
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
        Import.primoScenario();
        Rete.start2();
       // Rete.test();
    }
    
    //prova release 1.0
}
