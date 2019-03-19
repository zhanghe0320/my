package com.onesmock.dao.product;

public class ProductMess {
    private int id;
    private String equipmentbase;
    private String imgUrl;
    private String videoUrl;
    private Long CreatedTime;

    @Override
    public String toString() {
        return "ProductMess{" +
                "id=" + id +
                ", equipmentbase='" + equipmentbase + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", CreatedTime=" + CreatedTime +
                '}';
    }

    public Long getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Long createdTime) {
        CreatedTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipmentbase() {
        return equipmentbase;
    }

    public void setEquipmentbase(String equipmentbase) {
        this.equipmentbase = equipmentbase;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
