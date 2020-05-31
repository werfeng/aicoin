package com.example.aicoin.entity.fam;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="yzcm_notice")
public class FamNotice {
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cmc_id")
	private Integer id;
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
	private int type;//类型
	@Column(name="cmc_count",columnDefinition="int default 0")
	private int count;//类型 查看数量
	@Column(name="cmc_logo",columnDefinition="varchar(255)")
	private String logo;//标题图片
    @Column(name="cmc_link",columnDefinition="varchar(255)")
    private String link;//标题连接
    @Column(name="cmc_sub_type",columnDefinition="int ")
    private int subType;//子类型
//	@Column(name="cmc_author",columnDefinition="varchar(255)")
//	private String author;//标题连接
}
