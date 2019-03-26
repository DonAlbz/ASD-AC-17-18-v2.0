/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.*;
import Utilita.InputDati;
import Utilita.MyMenu;
import Utilita.ServizioFile;
import view.Parametri;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alber
 */
public class ControllerUtente {
    
    public static Rete rete;
    public static File fileSalvataggio = new File(Parametri.PATH_FILE_INPUT+"/salvataggio.dat");

    public static Rete start() {
        menuAvvio();
        return rete;
    }

    /**
     * Metodo che crea la rete attravero il menu iniziale (o la importa o la
     * carica da file)
     */
    private static void menuAvvio() {
        boolean fineProgramma = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INIZIALE, Parametri.VOCI_MENU_INIZIALE);
        int selezione = menu.scegli();
        fineProgramma = selezioneMenuAvvio(selezione);  
    }
    
    private static boolean selezioneMenuAvvio(int selezione){
        boolean end = false;
        switch(selezione){
            case 1: importa();
            break;
            
            case 2: carica();
            break;
            
            case 0: end = InputDati.yesOrNo(Parametri.MESSAGGIO_FINE_PROGRAMMA);
        }
        return end;
    }
    
    private static void importa(){
        // impostazione della directory di partenza
        JFileChooser fileScelto = new JFileChooser(Parametri.PATH_FILE_INPUT);
        // creazione del filtro txt
        FileFilter filtroTxt = new FileNameExtensionFilter("TXT file", "txt");
        // inserisco nella scelta del tipo di file un filtro TXT
        fileScelto.addChoosableFileFilter(filtroTxt);
        // di default filtro solo i file txt (lo aggiunge in automatico alla combo sottostante)
        fileScelto.setFileFilter(filtroTxt);
        // imposto di default il nome del file da cercare
        fileScelto.setSelectedFile(new File("input.txt"));

        int selezione = fileScelto.showDialog(null, "Seleziona il file da aprire");
        if (selezione == JFileChooser.APPROVE_OPTION) {
            String path = fileScelto.getSelectedFile().getPath();
            System.out.println(Parametri.MESSAGGIO_PATH_FILE_IMPORT);
            System.out.println(fileScelto.getSelectedFile());
            Import nuovoImport = new Import();
            rete = nuovoImport.caricaReteDaFile(path);
        }
    }
    
    private static void carica(){
        File fileSalvataggio = null;
        
         // impostazione della directory di partenza
        JFileChooser fileScelto = new JFileChooser(Parametri.PATH_FILE_INPUT);
        // creazione del filtro dat
        FileFilter filtroDat = new FileNameExtensionFilter("DAT file", "dat");
        // inserisco nella scelta del tipo di file un filtro dat
        fileScelto.addChoosableFileFilter(filtroDat);
        // di default filtro solo i file dat (lo aggiunge in automatico alla combo sottostante)
        fileScelto.setFileFilter(filtroDat);
        // imposto di default il nome del file da cercare
        fileScelto.setSelectedFile(new File("salvataggio.dat"));

        int selezione = fileScelto.showDialog(null, "Seleziona il file da caricare");
        if (selezione == JFileChooser.APPROVE_OPTION) {
            String path = fileScelto.getSelectedFile().getPath();
//            System.out.println(fileScelto.getSelectedFile());
//            Import nuovoImport = new Import();
//            rete = nuovoImport.caricaReteDaFile(path);
            fileSalvataggio = new File(path);
        }
        
        if (fileSalvataggio.exists()) {
            try {
                rete = (Rete) ServizioFile.caricaSingoloOggetto(fileSalvataggio);
            } catch (ClassCastException exc) {
                System.out.println("Non c'è il cast");
            } finally {
                if (rete != null) {
                    System.out.println("Caricamento corretto");
                } else {
                    System.out.println("Problemi con il caricameto");
                }
            }
        } else {
            System.out.println("Problemi con il file");
        }
    }

    /**
     * Metodo che permette di eseguire delle osservazioni o delle operazioni
     * sulla rete considerata
     *
     * @param rete
     */
    public static void menuRete(Rete rete) {
        boolean fineProgramma = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_RETE, Parametri.VOCI_MENU_RETE);
        do{
            int selezione = menu.scegli();
            fineProgramma = selezioneMenuRete(selezione);
        }while(!fineProgramma);
        infomazioniRete(rete);
        compiti(rete);
    }
    
    private static boolean selezioneMenuRete(int selezione){
        boolean end = false;
        switch(selezione){
            case 1: System.out.println("Stampa informazioni sulla rete");
            break;
            
            case 2: System.out.println("Esegui compito");
            break;
            
            case 3: salva();
            break;
            
            case 4: System.out.println("Cambia rete");
            break;
            
            case 0: end = InputDati.yesOrNo(Parametri.MESSAGGIO_FINE_PROGRAMMA);
        }
        return end;
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
    private static void salva() {
        ServizioFile.salvaSingoloOggetto(fileSalvataggio, rete);
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
