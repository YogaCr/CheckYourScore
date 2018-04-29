package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class;

import java.text.DecimalFormat;

/**
 * Created by sadaa on 4/26/2018.
 */

public class IsiNilaiClass {
    DecimalFormat df = new DecimalFormat("#");
    private String NamaSiswa;
    private Double NilaiSiswa;

    public String getNamaSiswa() {
        return NamaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.NamaSiswa = namaSiswa;
    }

    public Double getNilaiSiswa() {

        if (NilaiSiswa % 10 == 0) {
            return Math.floor(NilaiSiswa);
        }
        return NilaiSiswa;
    }

    public void setNilaiSiswa(Double nilaiSiswa) {
        this.NilaiSiswa = nilaiSiswa;
    }
}
