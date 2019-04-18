/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author Alb
 */
public class Parametri {

    public final static String EVENTO_NULLO = "\u0190";
    public final static String INSIEME_VUOTO = "\u00D8";
    public final static String STATO_DECORATO_PREFISSO = "a";
    public final static String SEPARATORE = "-----------------------";
    public final static String NULL = "null";
    public final static String INIZIALIZZAZIONE_RETE = "********* INIZIALIZZAZIONE RETE *********";
    public final static String A_CAPO = System.getProperty("line.separator");
    public final static String SPAZIO_COMPORTAMENTALE_ETICHETTA = "********* SPAZIO COMPORTAMENTALE *********";
    public final static String TRAIETTORIE_ETICHETTA = "********* TRAIETTORIE *********";
    public final static String CAMMINI_ETICHETTA = "********* CAMMINI *********";
    public final static String CAMMINI_NUMERO_ETICHETTA = "numero cammino: ";
    public final static String TRAIETTORIA_NUMERO_ETICHETTA = "numero traiettoria: " ;
    public final static String TAB = "\t";
    public final static String PARENTESI_TONDA_A = "(";
    public final static String PARENTESI_TONDA_C = ")";
    public final static String PARENTESI_QUADRA_A = "[";
    public final static String PARENTESI_QUADRA_C = "]";
    public final static String PARENTESI_GRAFFA_A = "{";
    public final static String PARENTESI_GRAFFA_C = "}";
    public final static String CARATTERE_MINORE = "<";
    public final static String CARATTERE_MAGGIORE = ">";
    public final static String SPAZIO = " ";
    public final static String VIRGOLA = ",";
    public final static String FRECCIA = "-->";
    public final static String ASTERISCO = "*";
    public final static String PIU = "+";
    public final static String APICE = "^";
    public final static String TITOLO_MENU_INIZIALE = "AUTOMA A STATI FINITI";
    public final static String VOCI_MENU_INIZIALE[] = {"Importa una nuova rete",
                                                        "Carica una nuova rete"};
    public final static String MESSAGGIO_FINE_PROGRAMMA = "Sei sicuro di voler uscire? ";
    public final static String TITOLO_MENU_RETE = "AUTOMA";
    public final static String VOCI_MENU_RETE[] = {"Stampa informazioni della rete",
                                                    "Esegui compito",
                                                    "Salva rete",
                                                    "Cambia rete"};
    public final static String PATH_FILE_INPUT = "./src/FileInput";
    public final static String MESSAGGIO_PATH_FILE_IMPORT = "Il file importato risiede nel seguente path:";
    public final static String OSSERVABILITA = "osservabilita";
    public final static String RILEVANZA = "rilevanza";
    public final static String FORMATO_TXT = ".txt";
    public final static String FORMATO_DAT = ".dat";
    public final static String IMPORT_SELEZIONE = "SELEZIONA IL FILE INPUT USATO COME SORGENTE";
    public final static String CARICAMENTO_SELEZIONE = "SELEZIONE IL FILE DA CARICARE";
    public final static String TITOLO_MENU_INFORMAZIONI_RETE = "INFORMAZIONI DELLA RETE";
    public final static String VOCI_MENU_INFORMAZIONI_RETE [] = {"Stampa nome della rete",
                                                                "Stampa automi che compongono la rete",
                                                                "Stampa stati che compongono la rete",
                                                                "Stampa transizioni che compongono la rete",
                                                                "Stampa globale delle informazioni della rete"};
    public final static String TITOLO_MENU_COMPITI = "MENU COMPITI";
    public final static String VOCI_MENU_COMPITI[] = {"Crea spazio comportamentale",
                                                        "Crea spazio comportamentale decorato",
                                                        "Distilla diagnosi",
                                                        "Distilla diagnosi da dizionario parziale"};
    public final static String MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE = "Inserisci una stringa come nell'esempio sottostante per effetturare una ricerca nel dizionario:";
    public final static String ESEMPIO_MESSAGGIO_INSERIMENTO_STRINGA_ETICHETTE = "Esempio di ingresso: <o3,o2,o3,o2>";
    public final static String MESSAGGIO_INSERISCI = "Inserisci la stringa desiderata: ";
    public final static String LEGENDA_ESPRESSIONE_REGOLARE = "LEGENDA ESPRESSIONE REGOLARE";
    public final static String VOCI_LEGENDA_ESPRESSIONE_REGOLARE[] = {"^ = apice per l'elevamento a potenza",
                                                                        "* = da mettere in seguito a ^ per definire opzionalità multipla",
                                                                        "+ = da mettere in seguito a ^ per definire multiplicità",
                                                                        "( ) = sono da considerarsi solo le parentesi tonde, anche nidificate, per definire l'espressione"};
    public final static String ESPRESSIONE_REGOLARE_ESEMPIO = "Esempio di espressione regolare: (o2 o3)^* o3";
    public final static String MESSAGGIO_INSERISCI_ESPRESSIONE_REGOLARE = "Basandosi come nell'esempio soprastante inserisci l'espressione regolare desiderata > ";
        
    
    public final static int NUMERO_RIGA_NOME_RETE = 0;
    public final static int NUMERO_RIGA_NOMI_AUTOMI = 3;
    public final static int NUMERO_RIGA_NOMI_LINK = 6;
    public final static int NUMERO_RIGA_NOMI_EVENTI = 9;
    public final static int NUMERO_RIGA_NOMI_OSSERVABILITA = 12;
    public final static int NUMERO_RIGA_NOMI_RILEVANZA = 15;
    
}
