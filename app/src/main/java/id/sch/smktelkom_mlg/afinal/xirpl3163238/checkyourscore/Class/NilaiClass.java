package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class;

/**
 * Created by Ferlina Firdausi on 18/04/2018.
 */

public class NilaiClass {
    private String deskripsi;
    private int nilai;
    private boolean status;

    public NilaiClass(String deskripsi, int nilai, boolean status) {

        this.deskripsi = deskripsi;
        this.nilai = nilai;
        this.status = status;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }

    public String getStatus() {
        if (status) {
            return "Tuntas";
        } else {
            return "Remidi";
        }

    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

