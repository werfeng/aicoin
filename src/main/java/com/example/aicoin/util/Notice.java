package com.example.aicoin.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
@Table(name="yzcm_notice")
public class Notice {
	@Id
	@GeneratedValue
	@Column(name="cmc_id")
	private int id;
	@Column(name="cmc_title",columnDefinition="varchar(255) not null")
	private String title;
	@Column(name="cmc_subtitle")
	private String subtitle;//副标题
	@Column(name="contents",columnDefinition="text not null")
	private String contents;
	@Column(name="cmc_sj",columnDefinition="datetime not null")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sj;
	@Column(name="cmc_zt",columnDefinition="int  default 0")
	private int zt;
	@Column(name="cmc_type",columnDefinition="int  default 0")
	private int type;//类型 0-通知(交易通知) 1-公告(官方公告) 2-活动(福利活动)3 火蚁财经 4 用户需知 5 交易需知 6全球站,7直播公告,8知识城邦
	@Column(name="cmc_count",columnDefinition="int default 0")
	private int count;//类型 查看数量
	@Column(name="cmc_logo",columnDefinition="varchar(255)")
	private String logo;//标题图片
    @Column(name="cmc_link",columnDefinition="varchar(255)")
    private String link;//标题连接
    @Column(name="cmc_sub_type",columnDefinition="int ")
    private int subType;//子类型
	@Column(name="cmc_author",columnDefinition="varchar(255)")
	private String author;//标题连接
}
