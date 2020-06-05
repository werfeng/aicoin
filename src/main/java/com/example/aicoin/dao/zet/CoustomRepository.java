package com.example.aicoin.dao.zet;

import com.example.aicoin.entity.zet.Notice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * date 2020/6/5
 */
public class CoustomRepository {

    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public List<Notice> noticeList() {
        CriteriaQuery<Notice> criteriaQuery = em.getCriteriaBuilder().createQuery(Notice.class);
        criteriaQuery.select(criteriaQuery.from(Notice.class));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
