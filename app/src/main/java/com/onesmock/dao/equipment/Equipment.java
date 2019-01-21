package com.onesmock.dao.equipment;

/**
 *
 * 设备的相关信息
 */
public class Equipment {
    private int id;//数据库 固定  不许插入数据
    private String equipmentName;//设备名字   设备0~设备9
    private Long CreatedTime;//时间戳 数据修改的时候插入时间轴
    private String equipmenthost;//主机标志，010100000001     (主机的 host base相同，货架host,base参数不相同)
    private String equipmentbase;//绑定 010100000001           根据equipmentbase删除货架
    private String equipmentid;//绑定 010100000001           根据equipmentbase删除货架
    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
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