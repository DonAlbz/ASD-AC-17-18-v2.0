/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Model.*;
import java.util.*;

/**
 *
 * @author alber
 */
public class View {
    public static void stampaCammini(List<Cammino> daStampare) {

        System.out.println(Parametri.CAMMINI_ETICHETTA);
        System.out.println();
        for (int i = 0; i < daStampare.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println(Parametri.CAMMINI_NUMERO_ETICHETTA + numeroCammino);
            System.out.println();
            System.out.println(daStampare.get(i).toString());
            System.out.println();
        }
    }

    public static void stampaTraiettorie(List<Cammino> traiettorie) {

        System.out.println(Parametri.TRAIETTORIE_ETICHETTA);
        System.out.println();
        for (int i = 0; i < traiettorie.size(); i++) {
            int numeroCammino = i + 1;
            System.out.println(Parametri.TRAIETTORIA_NUMERO_ETICHETTA+ numeroCammino);
            System.out.println();
            System.out.println(traiettorie.get(i).toString());
            System.out.println();
            System.out.println();
        }
    }
    
    public static void stampaFileTxt(List<String> fileTxt){
        for(String str : fileTxt){
            System.out.println(str);
        }
    }
}
