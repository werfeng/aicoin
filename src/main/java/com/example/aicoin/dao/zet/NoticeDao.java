package com.example.aicoin.dao.zet;

import com.example.aicoin.entity.zet.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeDao extends JpaRepository<Notice, Integer> {

    @Query(value = "select top 1 cmc_subtitle from yzcm_notice n where cmc_type=3 and cmc_sub_type =:type and cmc_subtitle like '8btc%' order by cmc_id desc", nativeQuery = true)
    String getMaxId(@Param("type") int type);

}