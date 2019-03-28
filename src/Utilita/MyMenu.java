package Utilita;

/*
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta 0 
e sia presentata in fondo al menu

 */
public class MyMenu {

    final private static String CORNICE = "--------------------------------";
    final private static String VOCE_USCITA = "0\tEsci";
    final private static String VOCE_MENU_PRINCIPALE = "Ritorna al menù principale";
    final private static String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";

    private String titolo;
    private String[] voci;

    public MyMenu(String titolo, String[] voci) {
        this.titolo = titolo;
        this.voci = voci;
    }

    public int scegli() {
        stampaMenu();
        return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }

    private void stampaMenu() {
        System.out.println(CORNICE);
        System.out.println(titolo);
        System.out.println(CORNICE);
        for (int i = 0; i < voci.length; i++) {
            System.out.println((i + 1) + "\t" + voci[i]);
        }
        System.out.println();
        System.out.println(VOCE_USCITA);
        System.out.println();
    }
    
    private void stampaMenuSenzaUscita() {
        System.out.println(CORNICE);
        System.out.println(titolo);
        System.out.println(CORNICE);
        for (int i = 0; i < voci.length; i++) {
            System.out.println((i + 1) + "\t" + voci[i]);
        }
    }
    
    public int scegliMenuInterno() {
        stampaMenuInterno();
        return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }
    
    private void stampaMenuInterno() {
        System.out.println(CORNICE);
        System.out.println(titolo);
        System.out.println(CORNICE);
        for (int i = 0; i < voci.length; i++) {
            System.out.println((i + 1) + "\t" + voci[i]);
        }
        System.out.println();
        System.out.println((voci.length+1) + "\t" + VOCE_MENU_PRINCIPALE);
        System.out.println(VOCE_USCITA);
        System.out.println();
    }
    
    public int scegliSenzaUscita(){
        stampaMenuSenzaUscita();
        return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 1, voci.length);
    }

}
