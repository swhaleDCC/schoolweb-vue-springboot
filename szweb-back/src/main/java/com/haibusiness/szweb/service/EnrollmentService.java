package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface EnrollmentService {
    Enrollment saveEnrollment(Enrollment enrollment);


    void removeEnrollment(Long id);


    void removeEnrollmentsInBatch(List<Enrollment> enrollments);

    Enrollment updateEnrollment(Enrollment enrollment);

    Enrollment getEnrollmentById(Long id);


    List<Enrollment> listEnrollments();


    Page<Enrollment> listEnrollmentsByTitleLike(String title, Pageable pageable);
}
