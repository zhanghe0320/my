package com.onesmock.dao.SystemValues;

/**
 * 系统基础信
 */
public class SystemValues {
    private int id;//数据库ID 自带 不修改
    private String name;//名称  超级密码 密码 。。。。
    private String value;//参数     名称是【本机编号】的时候   就是主机的号码  010100000001
    private Long CreatedTime;//时间戳 数据修改的时候插入时间轴
    private String equipmenthost;//唯一标识符     010100000001

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
/**/