/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.bean;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import su.olegus.info.EtcInfo;
import su.olegus.info.FilesInfo;
import su.olegus.info.TagsInfo;

/**
 *
 * @author plintus
 */
@Remote
public interface TagsBeanRemote {
    public List<TagsInfo> getTags();
    public TagsInfo getTag(Integer id);
    public TagsInfo setTag(TagsInfo etcinfo);
}
