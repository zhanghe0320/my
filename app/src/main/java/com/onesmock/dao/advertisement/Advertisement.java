package com.onesmock.dao.advertisement;

public class Advertisement {
    private int id;
    private String avd_url;
    private String goods_img_id;

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

    public String getGoods_img_id() {
        return goods_img_id;
    }

    public void setGoods_img_id(String goods_img_id) {
        this.goods_img_id = goods_img_id;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", avd_url='" + avd_url + '\'' +
                ", goods_img_id='" + goods_img_id + '\'' +
                '}';
    }
}
