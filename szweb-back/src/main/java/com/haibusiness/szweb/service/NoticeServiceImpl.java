package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.NoticeDao;
import com.haibusiness.szweb.entity.Notice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private NoticeDao noticeDao;
    @Override
    public Notice saveNotice(Notice notice) {
        return noticeDao.save(notice);
    }

    @Override
    public void removeNotice(Long id) {
        noticeDao.deleteById(id);
    }

    @Override
    public void removeNoticesInBatch(List<Notice> notices) {
        noticeDao.deleteInBatch(notices);
    }

    @Override
    public Notice updateNotice(Notice notice) {
        return noticeDao.save(notice);
    }

    @Override
    public Notice getNoticeById(Long id) {
        return noticeDao.getOne(id);
    }

    @Override
    public List<Notice> listNotices() {
        return noticeDao.findAll();
    }

    @Override
    public Page<Notice> listNoticesByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Notice> notices = noticeDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return notices;
    }
}
