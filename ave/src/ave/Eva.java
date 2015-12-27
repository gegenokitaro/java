/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ave;

/**
 *
 * @author rzl
 */
public class Eva {
    private int id_pokba;
    private String pokba;
    
    public Eva() {
        
    }
    
    public Eva(int id_pokba1, String pokba1) {
        this.id_pokba = id_pokba1;
        this.pokba = pokba1;
    }

    /**
     * @return the id_pokba
     */
    public int getId_pokba() {
        return id_pokba;
    }

    /**
     * @param id_pokba the id_pokba to set
     */
    public void setId_pokba(int id_pokba) {
        this.id_pokba = id_pokba;
    }

    /**
     * @return the pokba
     */
    public String getPokba() {
        return pokba;
    }

    /**
     * @param pokba the pokba to set
     */
    public void setPokba(String pokba) {
        this.pokba = pokba;
    }
    
}
