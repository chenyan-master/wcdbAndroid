package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/25 上午10:22
 * @desp
 */
@Entity
public class HGroup {
    @PrimaryKey
    @NonNull
    public String groupid;

    public String parentid;
    public String path;
    public String craccount;
    public String groupname;
    public String grouplogo;
    public String groupinfo;
    public String groupboard;
    public String topmsg;
    public String entrytime;
    public String surl;
    public int title;
    public int usercount = -1;
    public int maxmember = -1;
    public int version = -1;
    public int userversion = -1;
    public int groupstate = -1;
    public int type;
    public int role;
    public int banall;
    public int banurl;
    public int banpic;
    public int banchat;
    public int banchild;
    public int banscreenshot;
    public int banuser;
    public int banmsg;
    public String detail;

    @Override
    public String toString() {
        return new StringBuilder().append("{ groupid: ").append(groupid)
                .append(",parentid: ").append(parentid)
                .append(",path: ").append(path)
                .append(",craccount: ").append(craccount)
                .append(",groupname: ").append(groupname)
                .append(",grouplogo: ").append(grouplogo)
                .append(",groupinfo: ").append(groupinfo)
                .append(",topmsg: ").append(topmsg)
                .append(",groupboard: ").append(groupboard)
                .append(",entrytime: ").append(entrytime)
                .append(",surl: ").append(surl)
                .append(",title: ").append(title)
                .append(",usercount: ").append(usercount)
                .append(",maxmember: ").append(maxmember)
                .append(",version: ").append(version)
                .append(",userversion: ").append(userversion)
                .append(",groupstate: ").append(groupstate)
                .append(",type: ").append(type)
                .append(",role: ").append(role)
                .append(",banall: ").append(banall)
                .append(",banurl: ").append(banurl)
                .append(",banpic: ").append(banpic)
                .append(",banchat: ").append(banchat)
                .append(",banchild: ").append(banchild)
                .append(",banscreenshot: ").append(banscreenshot)
                .append(",banuser: ").append(banuser)
                .append(",banmsg: ").append(banmsg)
                .append(",detail: ").append(detail)
                .append(" }").toString();
    }
}
