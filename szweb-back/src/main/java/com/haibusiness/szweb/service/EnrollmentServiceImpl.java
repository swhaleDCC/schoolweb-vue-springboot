package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.EnrollmentDao;
import com.haibusiness.szweb.entity.Enrollment;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private EnrollmentDao enrollmentDao;
    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentDao.save(enrollment);
    }

    @Override
    public void removeEnrollment(Long id) {
        enrollmentDao.deleteById(id);
    }

    @Override
    public void removeEnrollmentsInBatch(List<Enrollment> enrollments) {
        enrollmentDao.deleteInBatch(enrollments);
    }

    @Override
    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentDao.save(enrollment);
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentDao.getOne(id);
    }

    @Override
    public List<Enrollment> listEnrollments() {
        return enrollmentDao.findAll();
    }

    @Override
    public Page<Enrollment> listEnrollmentsByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Enrollment> enrollments = enrollmentDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return enrollments;
    }
}
