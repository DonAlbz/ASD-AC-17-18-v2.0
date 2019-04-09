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
import java.util.ArrayList;
import java.util.List;
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
        int selezione = menuAvvio();
        if(selezione!=0){
            menuRete(rete);
        }
        
        return rete;
    }

    /**
     * Metodo che crea la rete attravero il menu iniziale (o la importa o la
     * carica da file)
     */
    public static int menuAvvio() {
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INIZIALE, Parametri.VOCI_MENU_INIZIALE);
        int selezione = menu.scegli();
        switch(selezione){
            case 1: importa(); 
                break;
            
            case 2: carica();     
                break;
        }
        return selezione;
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
    
    // DA SISTEMARE
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
            
            case 2: menuCompiti(rete);
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
        boolean fineMenu = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_INFORMAZIONI_RETE, Parametri.VOCI_MENU_INFORMAZIONI_RETE);
        do {
            int selezione = menu.scegliMenuInterno();
            switch (selezione) {
                case 1:
                    System.out.println("");
                    View.stampaNomeRete(rete);
                    break;

                case 2:
                    System.out.println("");
                    View.stampaAutomi(rete);
                    break;

                case 3:
                    System.out.println("");
                    View.stampaStati(rete);
                    break;
                    
                case 4:
                    System.out.println("");
                    View.stampaTransizioni(rete);
                    break;
                    
                case 5:
                    System.out.println("");
                    View.stampaGlobaleRete(rete);
                    break;

                case 0:
                    fineMenu = true;

                }
            } while (!fineMenu);  
    }

    /**
     * Permette di eseguire i compiti assegnati sulla rete
     *
     * @param rete
     */
    private static void menuCompiti(Rete rete) {
        boolean fineMenu = false;
        MyMenu menu = new MyMenu(Parametri.TITOLO_MENU_COMPITI, Parametri.VOCI_MENU_COMPITI);
        do {
            int selezione = menu.scegliMenuInterno();
            switch (selezione) {
                case 1:
                    System.out.println("");
                    creaSpazioComportamentale(rete);
                    break;

                case 2:
                    System.out.println("");
                    creaSpazioComportamentaleDecorato(rete);
                    break;

                case 3:
                    System.out.println("");
                    distillaDiagnosi();
                    break;

                case 0:
                    fineMenu = true;
            }
        } while (!fineMenu);
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
        if(rete.getSpazioC()!=null){
        Controller.creaSpazioComportamentaleDecorato(rete);
        }else{
            //TO-DO: messaggio di errore
        }
        }

   private static void creaDizionario(Rete rete) {
        Controller.creaDizionario(rete);
    }
   
   private static void distillaDiagnosi(){
       creaSpazioComportamentale(rete);
       creaSpazioComportamentaleDecorato(rete);
       creaDizionario(rete);
       List<String> etichetteOsservabilita = acquisisciStringaEtichetteOsservabilita();
       String diagnosi = Controller.distillaDiagnosi(rete, etichetteOsservabilita);
       if (diagnosi != null){
           System.out.println(diagnosi);
       }else{
           //TO-DO: messaggio errore;
       }
   }
   
   private static List<String> acquisisciStringaEtichetteOsservabilita(){
       List<String> daCercare = null;
       System.out.println(Parametri.MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE);
       System.out.println(Parametri.ESEMPIO_MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE);
       List<String> etichetteOsservabilita = getEtichetteOsservabilita();
       daCercare = InputDati.leggiInserimentoStringaEtichette(Parametri.MESSAGGIO_INSERISCI, etichetteOsservabilita);
       return daCercare;
   }
   
   private static List<String> getEtichetteOsservabilita(){
       List<String> etichette = new ArrayList<String>();
       String[] temp = rete.getEtichettaOsservabilita();
       for(int i  = 0; i<temp.length; i++){
           etichette.add(temp[i]);
       }
       return etichette;
   }
  
}