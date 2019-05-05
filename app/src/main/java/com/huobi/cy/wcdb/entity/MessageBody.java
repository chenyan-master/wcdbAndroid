package com.huobi.cy.wcdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/25 上午10:19
 * @desp
 */
@Entity
public class MessageBody {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int msgid;
    public int msgtype;
    public String fromuser;
    public String fromgroup;
    public String fromver;
    public String touser;
    public int totype;
    public String content;
    public String remark;
    public String localpath;
    public String orilocalpath;
    public String fileurl;
    public int sendflag;
    public int sendstate;
    public int atflag;
    public String sendtime;
    public int revokeflag;
    public int accountid;

    @Ignore
    @Override
    public String toString() {
        return new StringBuilder().append("{ msgid: ").append(msgid)
                .append(",msgtype: ").append(msgtype)
                .append(",fromuser: ").append(fromuser)
                .append(",fromgroup: ").append(fromgroup)
                .append(",touser: ").append(touser)
                .append(",totype: ").append(totype)
                .append(",content: ").append(content)
                .append(",remark: ").append(remark)
                .append(",localpath: ").append(localpath)
                .append(",orilocalpath: ").append(orilocalpath)
                .append(",fileurl: ").append(fileurl)
                .append(",sendflag: ").append(sendflag)
                .append(",sendstate: ").append(sendstate)
                .append(",atflag: ").append(atflag)
                .append(",sendtime: ").append(sendtime)
                .append(",revokeflag: ").append(revokeflag)
                .append(",accountid: ").append(accountid)
                .append(" }").toString();
    }
}
