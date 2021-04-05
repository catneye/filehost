/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import su.olegus.db.Tags;
import su.olegus.info.TagsInfo;

/**
 *
 * @author plintus
 */
@Stateless
public class TagsBean implements TagsBeanRemote {

    @EJB
    EtcBeanRemote etcEjb;

    @PersistenceContext(unitName = "filehost-ejbPU")
    private EntityManager em;

    @Override
    public List<TagsInfo> getTags() {
        List<TagsInfo> ret = new ArrayList();
        Query query = em.createNamedQuery("TagsEtc.findAll");
        List<Tags> etcs = query.getResultList();
        for (Tags etc : etcs) {
            TagsInfo etci = new TagsInfo();
            etci.setId(etc.getId());
            etci.setName(etc.getName());
            ret.add(etci);
        }
        return ret;
    }

    @Override
    public TagsInfo getTag(Integer id) {
        TagsInfo ret = null;
        Query query = em.createNamedQuery("Tags.findById");
        query.setParameter("id", id);
        try {
            Tags etc = (Tags) query.getSingleResult();
            ret = new TagsInfo();
            ret.setId(etc.getId());
            ret.setName(etc.getName());
        } catch (NoResultException ex) {
        }
        return ret;
    }

    @Override
    public TagsInfo setTag(TagsInfo etcinfo) {
        TagsInfo ret = null;
        Tags etc = null;
        if (etcinfo.getId() != null) {
            Query query = em.createNamedQuery("Tags.findById");
            query.setParameter("id", etcinfo.getId());
            try {
                etc = (Tags) query.getSingleResult();
                etc.setName(etcinfo.getName());

                em.merge(etc);
                em.flush();
            } catch (NoResultException ex) {
            }
        }
        if (etc == null) {
            etc = new Tags();
            etc.setName(etcinfo.getName());
            em.persist(etc);
            em.flush();
        }
        ret = new TagsInfo();
        ret.setId(etc.getId());
        ret.setName(etc.getName());
        return ret;
    }

}
