/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.*;
import view.*;
import Utilita.InputDati;
import Utilita.MyMenu;
import Utilita.ServizioFile;
import view.Parametri;
import java.io.File;
import java.io.FilenameFilter;
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
        compiti(rete);
    }
    
    private static void importa(){
        File cartella = new File(Parametri.PATH_FILE_INPUT);
        
        // inizializzazione della classe che mi permette di filtrare i txt
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(Parametri.FORMATO_TXT)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] listaFile = cartella.listFiles(textFilter);
        String[] nomiFile = new String[listaFile.length];
        for(int i = 0; i < listaFile.length; i++){
            nomiFile[i] = listaFile[i].getName();
        }
        
        MyMenu menuSelezione = new MyMenu(Parametri.IMPORT_SELEZIONE, nomiFile);
        int selezione = menuSelezione.scegliSenzaUscita();
        File fileScelto = listaFile[selezione-1];
        String pathCompletoFile = fileScelto.getAbsolutePath();
        Import nuovoImport = new Import();
        rete = nuovoImport.caricaReteDaFile(pathCompletoFile);
        
        View.messaggioImportCorretto(rete);
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
    
    private static boolean selezioneMenuRete(int selezione){
        boolean end = false;
        switch(selezione){
            case 1: menuInformazioniRete(rete);
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
    private static void menuInformazioniRete(Rete rete) {
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INFORMAZIONI_RETE, Parametri.VOCI_MENU_INFORMAZIONI_RETE);
        int selezione = menu.scegliMenuInterno();
        switch (selezione) {
            case 1:
                View.stampaNomeRete(rete);
                break;

            case 2:
                View.stampaAutomi(rete);
                break;

            case 3:
                View.stampaStati(rete);
                break;
                

            // ecc ecc
        }
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
