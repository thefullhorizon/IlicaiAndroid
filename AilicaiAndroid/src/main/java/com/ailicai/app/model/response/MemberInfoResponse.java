package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.MemberLevel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * name: MemberInfoResponse <BR>
 * description: 会员信息返回 <BR>
 * create date: 2017/9/5
 *
 * @author: IWJW Zhou Xuan
 */
public class MemberInfoResponse extends Response {

    private int id; // 会员主键
    private String userName; // 开户姓名
    private int score; // 会员当前积分
    private int nextLevelScore;// 下一个等级所需的最低积分
    private MemberLevel memberLevel; // 当前等级
    private List<MemberLevel> levels; // 所有的等级信息
    private String validLevelTill; // 会员等级有效期(yyyy-MM-dd)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNextLevelScore() {
        return nextLevelScore;
    }

    public void setNextLevelScore(int nextLevelScore) {
        this.nextLevelScore = nextLevelScore;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public List<MemberLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<MemberLevel> levels) {
        this.levels = levels;
    }

    public String getValidLevelTill() {
        return validLevelTill;
    }

    public void setValidLevelTill(String validLevelTill) {
        this.validLevelTill = validLevelTill;
    }
}
