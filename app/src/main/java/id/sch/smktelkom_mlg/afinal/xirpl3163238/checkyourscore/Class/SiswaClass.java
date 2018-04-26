package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class;

/**
 * Created by Sakata Yoga on 18/04/2018.
 */

public class SiswaClass {
    private String nama;
    private String UID;
    private int Nilai = 0;

    public int getNilai() {
        return Nilai;
    }

    public void setNilai(int nilai) {
        Nilai = nilai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
