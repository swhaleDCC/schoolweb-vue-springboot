package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.DownloadDao;
import com.haibusiness.szweb.entity.Download;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DownloadServiceImpl implements DownloadService {

    private DownloadDao downloadDao;
    @Override
    public Download saveDownload(Download download) {

        return downloadDao.save(download);
    }

    @Override
    public void removeDownload(Long id) {

        downloadDao.deleteById(id);
    }

    @Override
    public void removeDownloadsInBatch(List<Download> downloads) {

        downloadDao.deleteInBatch(downloads);
    }

    @Override
    public Download updateDownload(Download download) {

        return downloadDao.save(download);
    }

    @Override
    public Download getDownloadById(Long id) {

        return downloadDao.getOne(id);
    }

    @Override
    public List<Download> listDownloads() {

        return downloadDao.findAll();
    }
    @Override
    public Page<Download> listDownloadsByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Download> downloads = downloadDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return downloads;
    }
}
