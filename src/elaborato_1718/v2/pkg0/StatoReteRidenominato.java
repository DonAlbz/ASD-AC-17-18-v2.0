package elaborato_1718.v2.pkg0;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Stato utilizzato come nodo di uno spazio comportamentale, la ricerca di uno
 * StatoReteRidenominato avviene attraverso la variabile "nome".
 *
 * @author alber
 */
public class StatoReteRidenominato extends StatoReteAbstract {

    private String nome;

    /**
     * Crea uno StatoRete ridenominato
     *
     * @param link
     * @param stati
     * @param _numero
     */
    public StatoReteRidenominato(Evento[] link, Stato[] stati, int _numero) {
        super(link, stati, _numero);
        nome = Integer.toString(_numero);
    }

    /**
     * Crea uno StatoRete ridenominato
     *
     * @param link
     * @param stati
     * @param _numero
     */
    public StatoReteRidenominato(StatoReteAbstract s) {
        super(s.getLink(), s.getStati(), s.getNumero());
        setTransizionePrecedente(s.getTransizionePrecedente());
        nome = Integer.toString(s.getNumero());
    }

    /**
     * Crea un nuovo stato con la transizione inizializzata
     *
     * @param stato
     * @param transizione
     */
    public StatoReteRidenominato(StatoReteAbstract s, Transizione t) {
        super(s.getLink(), s.getStati(), s.getNumero());
        setTransizionePrecedente(t);
        nome = Integer.toString(s.getNumero());
    }

    
 public String getNomeToString(){
     return getNome();
 }
    
    
    /**
     * Override del metodo per poter implementare il metodo contains() in un
     * arrayList di cammini
     *
     * Due StatoReteRidenominato sono uguale se hanno lo stesso numero
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatoReteAbstract)) {
            return false;
        }
        StatoReteRidenominato stato2 = (StatoReteRidenominato) o;

        if (getNome().equals(stato2.getNome())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(getNome());
        return hash;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
