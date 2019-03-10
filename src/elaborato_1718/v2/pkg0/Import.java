/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elaborato_1718.v2.pkg0;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

public class Import {

    private String path;

    public Import(String file_path) {
        path = file_path;
    }

    static void primoScenario() {

        Automa c2 = new Automa("c2");
        Stato s20 = new Stato("20");
        Stato s21 = new Stato("21");
        Evento e2 = new Evento("e2");
        Evento e3 = new Evento("e3");
        Evento[] eventi = {e2, e3};
        Evento[] link = new Evento[2];
        String[] osservabilita = {"o2", "o3"};
        String[] rilevanza = {"r", "f"};
        Rete.creaRete("primo scenario", link, eventi, osservabilita, rilevanza);

        Vector<Evento> eventiIn = new Vector<>();
        Vector<Evento> eventiOut = new Vector<>();

        Evento[] eventoOut = {null, e3};
        String[] osservabilitaT2a = new String[1];
        String[] rilevanzaT2a = null;
        osservabilitaT2a[0] = Rete.getEtichettaOsservabilita()[0];
        Transizione t2a = new Transizione("t2a", s21, 0, e2, eventoOut, rilevanzaT2a, osservabilitaT2a);
        s20.addTransazione(t2a);

        Evento[] eventot2b = {null, e3};
        String[] osservabilitaT2b = null;
        String[] rilevanzaT2b = new String[1];
        rilevanzaT2b[0] = Rete.getEtichettaRilevanza()[0];
        Transizione t2b = new Transizione("t2b", s20, -1, null, eventot2b, rilevanzaT2b, osservabilitaT2b);
        s21.addTransazione(t2b);

        c2.addStato(s20);
        c2.addStato(s21);

        Rete.addAutoma(c2);

        Automa c3 = new Automa("c3");

        Stato s30 = new Stato("30");
        Stato s31 = new Stato("31");

        Evento[] eventit3a = {e2, null};
        String[] osservabilitaT3a = new String[1];
        String[] rilevanzaT3a = null;
        osservabilitaT3a[0] = Rete.getEtichettaOsservabilita()[1];
        Transizione t3a = new Transizione("t3a", s31, -1, null, eventit3a, rilevanzaT3a, osservabilitaT3a);
        s30.addTransazione(t3a);

        String[] osservabilitaT3c = null;
        String[] rilevanzaT3c = new String[1];
        
        rilevanzaT3c[0] = Rete.getEtichettaRilevanza()[1];
        Transizione t3c = new Transizione("t3c", s31, 1, e3, null, rilevanzaT3c, osservabilitaT3c);
        Transizione t3b = new Transizione("t3b", s30, 1, e3, null, null, null);
        s31.addTransazione(t3b);
        s31.addTransazione(t3c);

        c3.addStato(s30);
        c3.addStato(s31);

        Rete.addAutoma(c3);
    }

    public Vector<String> apriFile() throws FileNotFoundException, IOException {
        FileReader file = new FileReader(path);
        BufferedReader textReader = new BufferedReader(file);

        Vector<String> fileToString = new Vector<>();
        String[] input = null;
        try {
            int i = 0;
            String lineaDaCopiare;
            while ((lineaDaCopiare = textReader.readLine()) != null) {
                fileToString.add(lineaDaCopiare);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        textReader.close();
        return fileToString;
    }

    public String[] getAutomi(Vector<String> fileInput) {
        // sappiamo che gli automi sono posizionati in riga 0 del file
        String daSplittare = fileInput.get(0);
        String[] automi = daSplittare.split("\t");
        for (String stringa : automi) {
            System.out.println(stringa);
        }
        return automi;
    }

    public String[] getLink(Vector<String> fileInput) {
        // sappiamo che i link sono posizionati in riga 2 del file
        String daSplittare = fileInput.get(2);
        String[] link = daSplittare.split("\t");
        for (String stringa : link) {
            System.out.println(stringa);
        }
        return link;
    }

    public String[] getEventi(Vector<String> fileInput) {
        // sappiamo che gli eventi sono posizionati in riga 4 del file
        String daSplittare = fileInput.get(4);
        String[] eventi = daSplittare.split("\t");
        for (String stringa : eventi) {
            System.out.println(stringa);
        }
        return eventi;
    }

}
