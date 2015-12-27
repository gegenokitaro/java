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
public class Ave {

    private int id_matkul;
    private String matkul;
    
    
    public Ave() {

    }
    
    public Ave(int id_matkul1, String matkul1) {
        this.id_matkul = id_matkul1;
        this.matkul = matkul1;
    }

    /**
     * @return the id_matkul
     */
    public int getId_matkul() {
        return id_matkul;
    }

    /**
     * @param id_matkul the id_matkul to set
     */
    public void setId_matkul(int id_matkul) {
        this.id_matkul = id_matkul;
    }

    /**
     * @return the matkul
     */
    public String getMatkul() {
        return matkul;
    }

    /**
     * @param matkul the matkul to set
     */
    public void setMatkul(String matkul) {
        this.matkul = matkul;
    }
    
    
}


