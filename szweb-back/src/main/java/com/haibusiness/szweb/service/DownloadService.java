package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Download;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DownloadService {
    Download saveDownload(Download download);


    void removeDownload(Long id);


    void removeDownloadsInBatch(List<Download> downloads);

    Download updateDownload(Download download);

    Download getDownloadById(Long id);


    List<Download> listDownloads();


    Page<Download> listDownloadsByTitleLike(String name, Pageable pageable);
}
