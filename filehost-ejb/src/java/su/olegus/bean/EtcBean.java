/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.bean;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import su.olegus.db.Etc;
import su.olegus.db.Files;
import su.olegus.info.EtcInfo;
import su.olegus.info.FilesInfo;

/**
 *
 * @author plintus
 */
@Stateless
public class EtcBean implements EtcBeanRemote {

    @EJB
    EtcBeanRemote etcEjb;

    @PersistenceContext(unitName = "filehost-ejbPU")
    private EntityManager em;

    @Override
    public List<EtcInfo> getEtcs() {
        List<EtcInfo> ret = new ArrayList();
        Query query = em.createNamedQuery("Etc.findAll");
        List<Etc> etcs = query.getResultList();
        for (Etc etc : etcs) {
            EtcInfo etci = new EtcInfo();
            etci.setId(etc.getId());
            etci.setKey(etc.getKey());
            etci.setValue(etc.getValue());
            ret.add(etci);
        }
        return ret;
    }

    @Override
    public EtcInfo getEtc(Integer id) {
        EtcInfo ret = null;
        Query query = em.createNamedQuery("Etc.findById");
        query.setParameter("id", id);
        try {
            Etc etc = (Etc) query.getSingleResult();
            ret = new EtcInfo();
            ret.setId(etc.getId());
            ret.setKey(etc.getKey());
            ret.setValue(etc.getValue());
        } catch (NoResultException ex) {
        }
        return ret;
    }

    @Override
    public EtcInfo getEtcByKey(String key) {
        EtcInfo ret = null;
        Query query = em.createNamedQuery("Etc.findByKey");
        query.setParameter("key", key);
        try {
            Etc etc = (Etc) query.getSingleResult();
            ret = new EtcInfo();
            ret.setId(etc.getId());
            ret.setKey(etc.getKey());
            ret.setValue(etc.getValue());
        } catch (NoResultException ex) {
        }
        return ret;
    }

    @Override
    public EtcInfo setEtc(EtcInfo etcinfo) {
        EtcInfo ret = null;
        Etc etc = null;
        if (etcinfo.getId() != null) {
            Query query = em.createNamedQuery("Etc.findById");
            query.setParameter("id", etcinfo.getId());
            try {
                etc = (Etc) query.getSingleResult();
                etc.setKey(etcinfo.getKey());
                etc.setValue(etcinfo.getValue());

                em.merge(etc);
                em.flush();
            } catch (NoResultException ex) {
            }
        }
        if (etc == null) {
            etc = new Etc();
            etc.setKey(etcinfo.getKey());
            etc.setValue(etcinfo.getValue());
            em.persist(etc);
            em.flush();
        }
        ret = new EtcInfo();
        ret.setId(etc.getId());
        ret.setKey(etc.getKey());
        ret.setValue(etc.getValue());
        return ret;
    }

    @Override
    public List<FilesInfo> getFiles() {
        List<FilesInfo> ret = new ArrayList();
        Query query = em.createNamedQuery("Files.findAll");
        List<Files> etcs = query.getResultList();
        for (Files etc : etcs) {
            FilesInfo etci = new FilesInfo();
            etci.setId(etc.getId());
            etci.setFilepath(etc.getFilepath());
            etci.setDescription(etc.getDescription());
            etci.setFilemime(etc.getFilemime());
            etci.setName(etc.getName());
            etci.setRealname(etc.getRealname());
            etci.setAdddate(etc.getAdddate());
            ret.add(etci);
        }
        return ret;
    }

    @Override
    public FilesInfo getFile(Integer id) {
        FilesInfo ret = null;
        Query query = em.createNamedQuery("Files.findById");
        query.setParameter("id", id);
        try {
            Files etc = (Files) query.getSingleResult();
            ret = new FilesInfo();
            ret.setId(etc.getId());
            ret.setFilepath(etc.getFilepath());
            ret.setDescription(etc.getDescription());
            ret.setFilemime(etc.getFilemime());
            ret.setName(etc.getName());
            ret.setRealname(etc.getRealname());
            ret.setAdddate(etc.getAdddate());
        } catch (NoResultException ex) {
        }
        return ret;
    }

    @Override
    public FilesInfo setFile(FilesInfo etcinfo) {
        FilesInfo ret = null;
        Files etc = null;
        if (etcinfo.getId() != null) {
            Query query = em.createNamedQuery("Files.findById");
            query.setParameter("id", etcinfo.getId());
            try {
                etc = (Files) query.getSingleResult();
                etc.setId(etcinfo.getId());
                etc.setFilepath(etcinfo.getFilepath());
                etc.setDescription(etcinfo.getDescription());
                etc.setFilemime(etcinfo.getFilemime());
                etc.setName(etcinfo.getName());
                etc.setRealname(etcinfo.getRealname());
                etc.setAdddate(etcinfo.getAdddate());

                em.merge(etc);
                em.flush();
            } catch (NoResultException ex) {
            }
        }
        if (etc == null) {
            etc = new Files();
            etc.setId(etcinfo.getId());
            etc.setFilepath(etcinfo.getFilepath());
            etc.setDescription(etcinfo.getDescription());
            etc.setFilemime(etcinfo.getFilemime());
            etc.setName(etcinfo.getName());
            etc.setRealname(etcinfo.getRealname());
            etc.setAdddate(etcinfo.getAdddate());
            em.persist(etc);
            em.flush();
        }
        ret = new FilesInfo();
        ret.setId(etc.getId());
        ret.setFilepath(etc.getFilepath());
        ret.setDescription(etc.getDescription());
        ret.setFilemime(etc.getFilemime());
        ret.setName(etc.getName());
        ret.setRealname(etc.getRealname());
        ret.setAdddate(etc.getAdddate());
        return ret;
    }

    @Override
    public FilesInfo setFileDescription(FilesInfo etcinfo) {
        FilesInfo ret = null;
        Files etc = null;
        if (etcinfo.getId() != null) {
            Query query = em.createNamedQuery("Files.findById");
            query.setParameter("id", etcinfo.getId());
            try {
                etc = (Files) query.getSingleResult();
                etc.setDescription(etcinfo.getDescription());
                em.merge(etc);
                em.flush();
                ret = new FilesInfo();
                ret.setId(etc.getId());
                ret.setFilepath(etc.getFilepath());
                ret.setDescription(etc.getDescription());
                ret.setFilemime(etc.getFilemime());
                ret.setName(etc.getName());
                ret.setRealname(etc.getRealname());
                ret.setAdddate(etc.getAdddate());
            } catch (NoResultException ex) {
            }
        }
        return ret;
    }

    @Override
    public FilesInfo processFile(String filename) {
        FilesInfo ret = null;
        EtcInfo etci = etcEjb.getEtcByKey("upload");
        EtcInfo etcf = etcEjb.getEtcByKey("files");
        if ((etci != null) && (etcf != null)) {
            String uploadpath = etci.getValue();
            String filespath = etcf.getValue();
            if ((uploadpath != null) && (!uploadpath.isEmpty()) && (filespath != null) && (!filespath.isEmpty())) {
                String dscPath = uploadpath + filename + ".dsc";
                String uploadPath = uploadpath + filename;
                String filesPath = filespath + filename;
                try {
                    FileInputStream fis = new FileInputStream(dscPath);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    HashMap dschm = (HashMap) ois.readObject();
                    ois.close();
                    fis.close();
                    /*
                dschm.put("filename", savePath + ui.getFilename());
                dschm.put("originalname", ui.getOriginalname());
                dschm.put("description", ui.getDescription());
                     */
                    if (dschm.containsKey("filename") && dschm.containsKey("originalname") && dschm.containsKey("originalname")) {
                        //String filename = dschm.get("filename");
                        String originalname = (String) dschm.get("originalname");
                        String description = (String) dschm.get("description");

                        String mimeType = java.nio.file.Files.probeContentType(Paths.get(uploadPath));
                        Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "processFile mimeType {0}", mimeType);
                        //ret = new FilesInfo();
                        //ret.setDescription(description);
                        //ret.set
                        //image/jpeg
                        //application/vnd.ms-excel
                        //application/pdf
                        FileUtils.copyFile(new File(uploadPath), new File(filesPath));
                        FilesInfo fi = new FilesInfo();
                        fi.setDescription(description);
                        fi.setFilemime(mimeType);
                        fi.setName(filename);
                        fi.setRealname(originalname);
                        fi.setFilepath(filespath);
                        fi.setAdddate(new Date());
                        ret = setFile(fi);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "processFile {0}", ex);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "processFile {0}", ex);
                }
            }
        }
        return ret;
    }
//customs

    @Override
    public String createImagePreview(String filename, Integer size) {
        String ret = null;
        EtcInfo etcp = etcEjb.getEtcByKey("preview");
        EtcInfo etcf = etcEjb.getEtcByKey("files");
        Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "createImagePreview size {0}", size);
        if ((etcf != null) && (etcp != null)
                && (filename != null) && (!filename.isEmpty())) {
            try {
                String[] nameparts = filename.split("\\.");
                String exten = nameparts[nameparts.length - 1];
                String name = StringUtils.join(nameparts, "", 0, nameparts.length - 1);
                String newname = name + size;
                //Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "file: {0}", file.trim());
                String mimeType = java.nio.file.Files.probeContentType(Paths.get(etcf.getValue() + filename));
                File f1 = new File(etcf.getValue() + filename);
                File f2 = new File(etcp.getValue() + newname + "." + exten);

                TiffOutputSet outputSet = null;
                if (mimeType.equals("image/jpeg")) {
                    try {
                        IImageMetadata metadata = Sanselan.getMetadata(f1);
                        if ((metadata != null) && (metadata instanceof JpegImageMetadata)) {
                            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                            outputSet = jpegMetadata.getExif().getOutputSet();
                            Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "outputSet {0}", outputSet);
                        }
                    } catch (ImageReadException | ImageWriteException ex) {
                        Logger.getLogger(EtcBean.class.getName()).log(Level.SEVERE, "ImageReadException {0}", ex);
                    }
                } else {
                    Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "metadata non image/jpeg {0}", mimeType);
                }
                BufferedImage bufferedImage = ImageIO.read(f1);
                Float prop = new Float(bufferedImage.getWidth()) / new Float(bufferedImage.getHeight());
                Integer width = (prop.intValue() <= 1) ? size : new Float(size * prop).intValue();
                Integer height = (prop.intValue() > 1) ? size : new Float(size / prop).intValue();
                Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "prop {0}", prop);
                Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "width {0}", width);
                Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "height {0}", height);

                BufferedImage scaledBI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = scaledBI.createGraphics();
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                rh.put(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHints(rh);
                g.setComposite(AlphaComposite.Src);
                g.drawImage(bufferedImage, 0, 0, width, height, null);
                g.dispose();
                //ImageIO.write(scaledBI, exten, f2); 
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(scaledBI, exten, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                //copy metatags and write file
                try {
                    if (mimeType.equals("image/jpeg")) {
                        if (outputSet == null) {
                            outputSet = new TiffOutputSet();
                            TiffOutputField aperture = TiffOutputField.create(TiffConstants.EXIF_TAG_APERTURE_VALUE, outputSet.byteOrder, 0.3f);
                            TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                            // make sure to remove old value if present (this method will
                            // not fail if the tag does not exist).
                            exifDirectory.removeField(TiffConstants.EXIF_TAG_APERTURE_VALUE);
                            exifDirectory.add(aperture);
                        }
                        //OutputStream os = null;
                        //File tempFile = new File(f2.getAbsolutePath() + ".tmp");
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f2));
                        new ExifRewriter().updateExifMetadataLossless(imageBytes, bos, outputSet);
                        bos.close();
                    } else if (mimeType.equals("image/png")) {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f2));
                        outputStream.writeTo(bos);
                        bos.close();
                    } else {
                        Logger.getLogger(EtcBean.class.getName()).log(Level.INFO, "copy metatags non image/jpeg {0}", mimeType);
                    }
                } catch (ImageReadException | ImageWriteException ex) {
                    Logger.getLogger(EtcBean.class.getName()).log(Level.SEVERE, "ImageReadException {0}", ex);
                }

                ret = newname + "." + exten;
            } catch (IOException ex) {
                Logger.getLogger(EtcBean.class.getName()).log(Level.SEVERE, "createImagePreview {0}", ex);
            } catch (Exception ex) {
                Logger.getLogger(EtcBean.class.getName()).log(Level.SEVERE, "createImagePreview {0}", ex);
            }
        } else {
            Logger.getLogger(EtcBean.class.getName()).log(Level.SEVERE, "createImagePreview filename {0}", filename);
        }
        return ret;
    }

    @Override
    public List<FilesInfo> getFilesBeforeDate(Date adddate, Integer count) {
        List<FilesInfo> ret = new ArrayList();
        Query query = em.createNamedQuery("Files.findByBeforeDate");
        query.setParameter("adddate", adddate);
        query.setMaxResults(count);
        List<Files> etcs = query.getResultList();
        for (Files etc : etcs) {
            FilesInfo etci = new FilesInfo();
            etci.setId(etc.getId());
            etci.setFilepath(etc.getFilepath());
            etci.setDescription(etc.getDescription());
            etci.setFilemime(etc.getFilemime());
            etci.setName(etc.getName());
            etci.setRealname(etc.getRealname());
            etci.setAdddate(etc.getAdddate());
            ret.add(etci);
        }
        return ret;
    }
}
