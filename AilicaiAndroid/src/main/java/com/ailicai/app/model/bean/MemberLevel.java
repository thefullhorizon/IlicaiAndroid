package com.ailicai.app.model.bean;

import java.util.List;

public class MemberLevel {
    private int id; // 主键
    private String name; // 名称(如:V0)
    private int lowerLimit; // 该等级所需最低分数
    private int upperLimit; // 该等级所需最高分数
    private short status; // 状态(0:无效,1:有效)
    private List<Reward> rewards; // 该等级所能拥有的特权

    private String titleDesc; // 会员等级、当前会员等级、未达到会员等级
    private String validLevelTill =""; // 会员等级有效期(yyyy-MM-dd)


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

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public String getValidLevelTill() {
        return validLevelTill;
    }

    public void setValidLevelTill(String validLevelTill) {
        this.validLevelTill = validLevelTill;
    }

    //  id name lowerLimit upperLimit status 来判断是否是相同的memberlevel，rewards，validLevelTill和titleDesc不需要，其他的有需要再增加
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberLevel level = (MemberLevel) o;

        if (id != level.id) return false;
        if (lowerLimit != level.lowerLimit) return false;
        if (upperLimit != level.upperLimit) return false;
        if (status != level.status) return false;
        return name != null ? name.equals(level.name) : level.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + lowerLimit;
        result = 31 * result + upperLimit;
        result = 31 * result + (int) status;
        return result;
    }
}
