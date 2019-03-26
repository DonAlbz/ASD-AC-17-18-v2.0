/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.*;

/**
 *
 * @author alber
 */
public class ControllerUtente {

    static void start() {
        Rete rete = menuAvvio(); //
        menuRete(rete);
    }

    /**
     * Metodo che crea la rete attravero il menu iniziale (o la importa o la
     * carica da file)
     *
     * @return
     */
    private static Rete menuAvvio() {
        return Import.primoScenario();
    }

    /**
     * Metodo che permette di eseguire delle osservazioni o delle operazioni
     * sulla rete considerata
     *
     * @param rete
     */
    private static void menuRete(Rete rete) {
        infomazioniRete(rete);
        compiti(rete);
        salva(rete);
    }

    /**
     * Permette di visualizzare varie informazioni sulla rete
     *
     * @param rete
     */
    private static void infomazioniRete(Rete rete) {
    }

    /**
     * Permette di eseguire i compiti assegnati sulla rete
     *
     * @param rete
     */
    private static void compiti(Rete rete) {
        creaSpazioComportamentale(rete);
        creaSpazioComportamentaleDecorato(rete);
    }

    /**
     * Salva la rete considerata
     *
     * @param rete
     */
    private static void salva(Rete rete) {
    }

    /**
     * crea lo spazioComportamentale della rete (e visualizza info)
     *
     * @param rete
     */
    private static void creaSpazioComportamentale(Rete rete) {
        Controller.creaSpazioComportamentale(rete);
    }

    /**
     * crea lo spazioComportamentaleDecorato della rete (e visualizza info)
     *
     * @param rete
     */
    private static void creaSpazioComportamentaleDecorato(Rete rete) {
        Controller.creaSpazioComportamentaleDecorato(rete);
    }

}
