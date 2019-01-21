package com.onesmock.dao.video;

public class Video {
    private int id;
    private String equipmenthost;
    private String avd_url;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvd_url() {
        return avd_url;
    }

    public void setAvd_url(String avd_url) {
        this.avd_url = avd_url;
    }

    public String getEquipmenthost() {
        return equipmenthost;
    }

    public void setEquipmenthost(String equipmenthost) {
        this.equipmenthost = equipmenthost;
    }


    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", equipmenthost='" + equipmenthost + '\'' +
                ", avd_url='" + avd_url + '\'' +
                '}';
    }
}
