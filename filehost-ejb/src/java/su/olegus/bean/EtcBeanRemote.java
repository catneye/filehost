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

/**
 *
 * @author plintus
 */
@Remote
public interface EtcBeanRemote {
    public List<EtcInfo> getEtcs();
    public EtcInfo getEtc(Integer id);
    public EtcInfo getEtcByKey(String key);
    public EtcInfo setEtc(EtcInfo etc);
    public List<FilesInfo> getFiles();
    public FilesInfo getFile(Integer id);
    public FilesInfo setFile(FilesInfo etcinfo);
    public FilesInfo processFile(String filename);
    public FilesInfo setFileDescription(FilesInfo etcinfo);
    //
    public String createImagePreview(String filename, Integer size);
    public List<FilesInfo> getFilesBeforeDate(Date adddate, Integer count);
}
