package com.example.aicoin.entity.zet;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
* @date 2020年5月21日
*/
@Entity
@Data
@Table(name="yzcm_community_notice")
public class CommunityNotice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cmc_id")
	private int id;
	
	@Column(name = "cmc_deploy_id", columnDefinition = "int not null")
	private int deployId; //社区id 关联yzcm_community_deploy
	
	@Column(name="cmc_title",columnDefinition="varchar(255)")
	private String title; //标题
	@Column(name="cmc_subtitle")
	private String subtitle;//副标题
	@Column(name="contents",columnDefinition="text")
	private String contents; //内容
	@Column(name="cmc_zt",columnDefinition="int  default 0")
	private int zt; //状态: 0未发布 1发布成功 2发布失败
	@Column(name="cmc_type",columnDefinition="int  default 0")
	private int type;//类型 0群消息 1群风采,2商学院知识城邦,3 社区新消息 4直播公告 5精读学院 6音频学院 7视频学院
	@Column(name="cmc_count",columnDefinition="int default 0")
	private int count;//喜欢的数量
	@Column(name="cmc_logo",columnDefinition="varchar(255)")
	private String logo;//标题图片
    @Column(name="cmc_link",columnDefinition="varchar(255)")
    private String link;//标题连接
    
    @Column(name="cmc_recorder",columnDefinition="varchar(50)")
    private String recorder;//记录员
    
    @Column(name="cmc_sj",columnDefinition="datetime not null")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sj;
}
