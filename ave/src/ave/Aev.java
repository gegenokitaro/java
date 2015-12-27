/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ave;

/**
 *
 * @author je
 */
public class Aev {
    private int id_soal;
    private String desk;
    private String jwbn_a,jwbn_b,jwbn_c,jwbn_d;
    private String kunci_jwbn;
    private int id_pokba;

    public Aev() {
            
    }
        
    public Aev(int id_soal1, String desk1, String jwbn_a1, String jwbn_b1, String jwbn_c1, String jwbn_d1, String kunci_jwbn1, int id_pokba1) {
        this.id_soal = id_soal1;
        this.desk = desk1;
        this.jwbn_a = jwbn_a1;
        this.jwbn_b = jwbn_b1;
        this.jwbn_c = jwbn_c1;
        this.jwbn_d = jwbn_d1;
        this.kunci_jwbn = kunci_jwbn1;
        this.id_pokba = id_pokba1;
    }

    /**
     * @return the id_soal
     */
    public int getId_soal() {
        return id_soal;
    }

    /**
     * @param id_soal the id_soal to set
     */
    public void setId_soal(int id_soal) {
        this.id_soal = id_soal;
    }

    /**
     * @return the desk
     */
    public String getDesk() {
        return desk;
    }

    /**
     * @param desk the desk to set
     */
    public void setDesk(String desk) {
        this.desk = desk;
    }

    /**
     * @return the jwbn_a
     */
    public String getJwbn_a() {
        return jwbn_a;
    }

    /**
     * @param jwbn_a the jwbn_a to set
     */
    public void setJwbn_a(String jwbn_a) {
        this.jwbn_a = jwbn_a;
    }

    /**
     * @return the jwbn_b
     */
    public String getJwbn_b() {
        return jwbn_b;
    }

    /**
     * @param jwbn_b the jwbn_b to set
     */
    public void setJwbn_b(String jwbn_b) {
        this.jwbn_b = jwbn_b;
    }

    /**
     * @return the jwbn_c
     */
    public String getJwbn_c() {
        return jwbn_c;
    }

    /**
     * @param jwbn_c the jwbn_c to set
     */
    public void setJwbn_c(String jwbn_c) {
        this.jwbn_c = jwbn_c;
    }

    /**
     * @return the jwbn_d
     */
    public String getJwbn_d() {
        return jwbn_d;
    }

    /**
     * @param jwbn_d the jwbn_d to set
     */
    public void setJwbn_d(String jwbn_d) {
        this.jwbn_d = jwbn_d;
    }

    /**
     * @return the kunci_jwbn
     */
    public String getKunci_jwbn() {
        return kunci_jwbn;
    }

    /**
     * @param kunci_jwbn the kunci_jwbn to set
     */
    public void setKunci_jwbn(String kunci_jwbn) {
        this.kunci_jwbn = kunci_jwbn;
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
         * @return the id_soal
         */
        
}
