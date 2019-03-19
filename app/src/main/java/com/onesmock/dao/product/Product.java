package com.onesmock.dao.product;


/**
 * 产品的相关信息
 */
public class Product {
    private int id;//不修改
    private int productId;//产品ID
    private String productName;//产品名称  南京  中华
    private String productDaysum;//产品日出货量    来自平台
    private String productTotal;//产品总出货量     来自平台
    private String imgUrl;//图片的地址
    private Long CreatedTime;//时间戳 数据修改的时候插入时间轴
    private String equipmenthost;//唯一标识 			010100000001
    private String equipmentbase;//产品机器码分机号码  货架  020200000001


    private String prematchImgurl;//预配
    private String prematchProductname;//预配

    private String productMess;//产品介绍信息
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDaysum='" + productDaysum + '\'' +
                ", productTotal='" + productTotal + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", CreatedTime=" + CreatedTime +
                ", equipmenthost='" + equipmenthost + '\'' +
                ", equipmentbase='" + equipmentbase + '\'' +
                ", prematchImgurl='" + prematchImgurl + '\'' +
                ", prematchProductname='" + prematchProductname + '\'' +
                ", productMess='" + productMess + '\'' +
                '}';
    }

    public String getProductMess() {
        return productMess;
    }

    public void setProductMess(String productMess) {
        this.productMess = productMess;
    }




    //需要现有的equipmentbase 的 具体信息 产品详情信息，  图片，


    public String getPrematchImgurl() {
        return prematchImgurl;
    }

    public void setPrematchImgurl(String prematchImgurl) {
        this.prematchImgurl = prematchImgurl;
    }

    public String getPrematchProductname() {
        return prematchProductname;
    }

    public void setPrematchProductname(String prematchProductname) {
        this.prematchProductname = prematchProductname;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDaysum() {
        return productDaysum;
    }

    public void setProductDaysum(String productDaysum) {
        this.productDaysum = productDaysum;
    }

    public String getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(String productTotal) {
        this.productTotal = productTotal;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Long createdTime) {
        CreatedTime = createdTime;
    }

    public String getEquipmenthost() {
        return equipmenthost;
    }

    public void setEquipmenthost(String equipmenthost) {
        this.equipmenthost = equipmenthost;
    }

    public String getEquipmentbase() {
        return equipmentbase;
    }

    public void setEquipmentbase(String equipmentbase) {
        this.equipmentbase = equipmentbase;
    }


}
/* */