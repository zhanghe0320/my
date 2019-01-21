package com.onesmock.dao.author;

/**
 * 管理员的相关信息
 */
public class Author {
    private  int id; //不修改
    private String author_name;
    private String author_phone;

    private String equipmenthost;//机器码唯一标识   010100000001
    private Long CreatedTime;//时间戳 数据修改的时候插入时间轴

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

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_phone() {
        return author_phone;
    }

    public void setAuthor_phone(String author_phone) {
        this.author_phone = author_phone;
    }


    public String getEquipmenthost() {
        return equipmenthost;
    }

    public void setEquipmenthost(String equipmenthost) {
        this.equipmenthost = equipmenthost;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", author_name='" + author_name + '\'' +
                ", author_phone='" + author_phone + '\'' +
                ", equipmenthost='" + equipmenthost + '\'' +
                ", CreatedTime=" + CreatedTime +
                '}';
    }
}
/* */