package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class;

/**
 * Created by Ferlina Firdausi on 18/04/2018.
 */

public class NilaiClass {
    private String deskripsi;
    private Double nilai;
    private boolean status;
    private String id;
    private String mapId;

    public NilaiClass(String deskripsi, Double nilai, boolean status, String id, String mapId) {
        this.deskripsi = deskripsi;
        this.nilai = nilai;
        this.status = status;
        this.id = id;
        this.mapId = mapId;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Double getNilai() {
        return nilai;
    }

    public void setNilai(Double nilai) {
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

