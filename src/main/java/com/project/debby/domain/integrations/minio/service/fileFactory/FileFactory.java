package com.project.debby.domain.integrations.minio.service.fileFactory;

import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.loan.model.LoanState;
import org.springframework.web.multipart.MultipartFile;

public interface FileFactory {
    File create(LoanState state);
}
