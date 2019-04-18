package Utilita;

import java.util.*;
import view.Parametri;

public class InputDati {

    private static Scanner lettore = creaScanner();

    private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
    private final static String ERRORE_MINIMO = "Attenzione: e' richiesto un valore maggiore o uguale a ";
    private final static String ERRORE_STRINGA_VUOTA = "Attenzione: non hai inserito alcun carattere";
    private final static String ERRORE_MASSIMO = "Attenzione: e' richiesto un valore minore o uguale a ";
    private final static String MESSAGGIO_AMMISSIBILI = "Attenzione: i caratteri ammissibili sono: ";
    private final static String ERRORE_SEPARATORE = "Attenzione: nella stringa inserita manca il separatore corretto";
    private final static String ERRORE_DIZIONARIO = "Attenzione: uno o più simboli inseriti non appartiene al dizionario di osservabilità";
    private final static String ERRORE_ALFABETO_DIZIONARIO = "Attenzione: l'inserimento dell'espressione regolare non è conforme alla sintassi";
    private final static String ERRORE_ALFABETO_PARENTESI = "Attenzione: le parentesi inserite non sono corrette";
    private final static String ERRORE_ALFABETO_SINTASSI ="Attenzione: la sintassi dell'espressione regolare non è corretta";

    private final static char RISPOSTA_SI = 'S';
    private final static char RISPOSTA_NO = 'N';

    private static Scanner creaScanner() {
        Scanner creato = new Scanner(System.in);
        creato.useDelimiter(System.getProperty("line.separator"));
        return creato;
    }

    public static String leggiStringa(String messaggio) {
        System.out.print(messaggio);
        return lettore.nextLine();
    }

    public static String leggiStringaNonVuota(String messaggio) {
        boolean finito = false;
        String lettura = null;
        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            if (lettura.length() > 0) {
                finito = true;
            } else {
                System.out.println(ERRORE_STRINGA_VUOTA);
            }
        } while (!finito);

        return lettura;
    }

    public static char leggiChar(String messaggio) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            System.out.print(messaggio);
            // String lettura = lettore.next();
            // l'operazione commentata genera problemi in ambienti windows
            String lettura = lettore.nextLine();
            if (lettura.length() > 0) {
                valoreLetto = lettura.charAt(0);
                finito = true;
            } else {
                System.out.println(ERRORE_STRINGA_VUOTA);
            }
        } while (!finito);
        return valoreLetto;
    }

    public static char leggiUpperChar(String messaggio, String ammissibili) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            valoreLetto = leggiChar(messaggio);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (ammissibili.indexOf(valoreLetto) != -1) {
                finito = true;
            } else {
                System.out.println(MESSAGGIO_AMMISSIBILI + ammissibili);
            }
        } while (!finito);
        return valoreLetto;
    }

    public static int leggiIntero(String messaggio) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            System.out.print(messaggio);
            try {
                //valoreLetto = lettore.nextInt();
                // nuovo metodo perché genera problemi su ambienti windows
                try{
                    valoreLetto = Integer.parseInt(lettore.nextLine());
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public static int leggiInteroPositivo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 1);
    }

    public static int leggiInteroNonNegativo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 0);
    }

    public static int leggiInteroConMinimo(String messaggio, int minimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo) {
                finito = true;
            } else {
                System.out.println(ERRORE_MINIMO + minimo);
            }
        } while (!finito);

        return valoreLetto;
    }

    public static int leggiIntero(String messaggio, int minimo, int massimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo && valoreLetto <= massimo) {
                finito = true;
            } else if (valoreLetto < minimo) {
                System.out.println(ERRORE_MINIMO + minimo);
            } else {
                System.out.println(ERRORE_MASSIMO + massimo);
            }
        } while (!finito);

        return valoreLetto;
    }

    public static double leggiDouble(String messaggio) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            System.out.print(messaggio);
            try {
                valoreLetto = lettore.nextDouble();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public static double leggiDoubleConMinimo(String messaggio, double minimo) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo) {
                finito = true;
            } else {
                System.out.println(ERRORE_MINIMO + minimo);
            }
        } while (!finito);

        return valoreLetto;
    }

    public static boolean yesOrNo(String messaggio) {
        String mioMessaggio = messaggio + "(" + RISPOSTA_SI + "/" + RISPOSTA_NO + ")";
        char valoreLetto = leggiUpperChar(mioMessaggio, String.valueOf(RISPOSTA_SI) + String.valueOf(RISPOSTA_NO));

        if (valoreLetto == RISPOSTA_SI) {
            return true;
        } else {
            return false;
        }
    }
    
    public static List<String> leggiInserimentoStringaEtichette(String messaggio, List<String> etichetteOsservabilita){
        boolean finito = false;
        List<String> etichette = new ArrayList<String>();
        String lettura = null;
        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            String carattereIniziale = lettura.substring(0, 1);
            String carattereFinale = lettura.substring(lettura.length()-1, lettura.length());
            if(carattereIniziale.equalsIgnoreCase(Parametri.CARATTERE_MINORE) && carattereFinale.equalsIgnoreCase(Parametri.CARATTERE_MAGGIORE)){
                if(lettura.contains(Parametri.VIRGOLA)){
                    // rimozione del minore e maggiore
                    lettura = lettura.substring(1,lettura.length()-1);
                    // split
                    String[] temp = lettura.split(Parametri.VIRGOLA);
                    // controllo che i caratteri facciano parte dell'osservabilita
                    boolean controlloEsterno = true;
                    for(int i = 0; i<temp.length; i++){
                        boolean controlloInterno = false;
                        for (String etic : etichetteOsservabilita) {
                            if (etic.equalsIgnoreCase(temp[i])) {
                                controlloInterno = true;
                            }
                        }
                        if (!controlloInterno) {
                            controlloEsterno = false;
                            break;
                        }
                    }
                    if (controlloEsterno) {
                        for (int i = 0; i < temp.length; i++) {
                            etichette.add(temp[i]);
                        }
                        finito = true;
                    } else {
                        System.out.println(ERRORE_DIZIONARIO);
                    }
                } else {
                    System.out.println(ERRORE_SEPARATORE);
                }
            } else {
                System.out.println(ERRORE_FORMATO);
            }

        } while (!finito);

        return etichette;
    }
    
    
    public static String inserimentoEspressioneRegolare(String messaggio, String[] alfabetoEspressione){
        String espressione = null;
        boolean finito = false;
        
        // inizializzazione di tutti i caratteri ammessi nell'espressione regolare
        String parentesiAperta = Parametri.PARENTESI_TONDA_A;
        String parentesiChiusa = Parametri.PARENTESI_TONDA_C;
        char parentesiChiusaChar = ')';
        char parentesiApertaChar = '(';
        String asterisco = Parametri.ASTERISCO;
        String piu = Parametri.PIU;
        String apice = Parametri.APICE;
        
        // controllo alfabeto
        // controllo delle parentesi
        // controllo sintassi (esempio * succede ^ altrimenti errore)
        do{
            espressione = leggiStringa(messaggio);
            
            // inizializzazione variabili di controllo
            boolean controlloAlfabeto = true;
            boolean controlloParentesi = false;
            boolean controlloSintassi = false;
            
            // CONTROLLO DELL'ALFABETO
            // elimino caratteri ammessi per controllare se l'alfabeto è corretto
            String espressionePulita = espressione;
            espressionePulita = espressionePulita.replace(parentesiAperta, " ");
            espressionePulita = espressionePulita.replace(parentesiChiusa, " ");
            espressionePulita = espressionePulita.replace(asterisco, " ");
            espressionePulita = espressionePulita.replace(piu, " ");
            espressionePulita = espressionePulita.replace(apice, " ");
            // spacchetto la stringa per verificare i caratteri
            String[] splittato = espressionePulita.split(" ");
            for (String split : splittato) {
                boolean controlloParole = false;
                if (!split.equalsIgnoreCase("")) {
                    for (String alfabeto : alfabetoEspressione) {
                        if (split.equalsIgnoreCase(alfabeto)) {
                            controlloParole = true;
                        }
                    }
                } else {
                    controlloParole = true;
                }
                if(!controlloParole){
                    controlloAlfabeto = false;
                }
            }

            // CONTROLLO DELLE PARENTESI
            int contaParentesiChiuse = contaOccorrenze(espressione, parentesiChiusaChar);
            int contaParentesiAperte = contaOccorrenze(espressione, parentesiApertaChar);
            if(contaParentesiAperte == contaParentesiChiuse){
                controlloParentesi = true;
            }
            
            // CONTROLLO SINTASSI
            
            
            // CONTROLLO FINALE PER MESSAGGI DI ERRORE
            if(controlloAlfabeto){
                if(controlloParentesi){
                    if(controlloSintassi){
                        finito = true;
                    } else {
                        System.out.println(ERRORE_ALFABETO_SINTASSI);
                    }
                } else {
                    System.out.println(ERRORE_ALFABETO_PARENTESI);
                }
            } else {
                System.out.println(ERRORE_ALFABETO_DIZIONARIO);
            }
            
            
        } while (!finito);
        return espressione;
    }
    
    private static int contaOccorrenze(String source, char target){
        int occorrenze = 0;
        for(int i = 0; i<source.length(); i++){
            if(source.charAt(i) == target){
                occorrenze++;
            }
        }
        return occorrenze;
    }
   

}
