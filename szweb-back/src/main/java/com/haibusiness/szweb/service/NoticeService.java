package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NoticeService {
    Notice saveNotice(Notice notice);


    void removeNotice(Long id);


    void removeNoticesInBatch(List<Notice> notices);

    Notice updateNotice(Notice notice);

    Notice getNoticeById(Long id);


    List<Notice> listNotices();


    Page<Notice> listNoticesByTitleLike(String title, Pageable pageable);
}
