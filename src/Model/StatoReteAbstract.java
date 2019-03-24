/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import view.Parametri;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author alber
 */
public abstract class StatoReteAbstract {

    private Evento[] link;
    private Stato[] stati;
    private int numero;
    private String descrizione;
    private Transizione transizionePrecedente;
    private List<String> decorazione;
    private String osservabilita;
    private String nome;

    public StatoReteAbstract(Evento[] link, Stato[] stati, int _numero) {
        this.link = link;
        this.stati = stati;
        this.numero = _numero;
        this.descrizione = creaDescrizione();        
    }

    public String creaDescrizione() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < stati.length; i++) {
            s.append(stati[i].toString());
            s.append(" ");
        }
        for (int i = 0; i < link.length; i++) {
            if (link[i] != null) {
                s.append(link[i].toString());
            } else {
                s.append(Parametri.EVENTO_NULLO);
            }
            s.append(" ");
        }
        return s.toString().trim();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        if (transizionePrecedente != null) {
            s.append(transizionePrecedente);
            s.append(Parametri.VIRGOLA);
            s.append(Parametri.SPAZIO);
        }
        s.append(Parametri.PARENTESI_QUADRA_A);
        s.append(getNomeToString());
        s.append(Parametri.PARENTESI_QUADRA_C);
        s.append(Parametri.TAB);
        if (s.length() < 9) // serve per intabellare lo spazio decorato se ha uno stato iniziale più lungo di 9 caratteri
        {
            s.append(Parametri.TAB);
        }
        s.append(descrizione);
        s.toString();
        return s.toString();
    }

    public String toStringShort() {
        StringBuilder s = new StringBuilder();
        if (transizionePrecedente != null) {
            s.append(transizionePrecedente);
            s.append(Parametri.VIRGOLA);
            s.append(Parametri.SPAZIO);
        }
        s.append(Parametri.PARENTESI_QUADRA_A);
        s.append(getNomeToString());
        s.append(Parametri.PARENTESI_QUADRA_C);
//        s.append(Parametri.TAB);
//        if (s.length() < 9) // serve per intabellare lo spazio decorato se ha uno stato iniziale più lungo di 9 caratteri
//        {
//            s.append(Parametri.TAB);
//        }       
        s.toString();
        return s.toString();
    }

    public String getNomeToString() {
        return Integer.toString(numero);
    }

    public Evento[] getLink() {
        return link;
    }

    public Stato[] getStati() {
        return stati;
    }

    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Controllo se e' uno stato finale, quindi se ha i link vuoti
     *
     * @return
     */
    boolean isFinale() {
        for (Evento l : link) {
            if (l != null) {
                return false;
            }
        }
        return true;
    }

    public void setNome(StatoReteAbstract s){
        if (s.getNome() == null) {
            setNome(new String(Integer.toString(s.getNumero())));
        } else {
            setNome(s.getNome());
        }
    }
    
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Transizione getTransizionePrecedente() {
        return transizionePrecedente;
    }

    public void setTransizionePrecedente(Transizione transizionePrecedente) {
        this.transizionePrecedente = transizionePrecedente;
    }

    public List<String> getDecorazione() {
        return decorazione;
    }

    public void setDecorazione(List<String> decorazione) {
        this.decorazione = decorazione;
    }

    public String getOsservabilita() {
        return osservabilita;
    }

    public void setOsservabilita(String osservabilita) {
        this.osservabilita = osservabilita;
    }

   
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
