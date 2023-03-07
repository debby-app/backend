package com.project.debby.domain.integrations.minio.service;

import com.project.debby.domain.integrations.minio.client.exception.CannotRemoveObjectMinioException;
import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.User;
import org.springframework.web.multipart.MultipartFile;



public interface MinioService {

    void initDefaultBucket(); // create post construct logic

    /**
     * Получение ссылки на аватар пользователя.
     * Если у пользователя нет аватара - возвращает пустую строку
     * @param user - юзер для которого нужно получить ссылку
     * @return строку с урлом
     */
    String getAvatarURL(User user);

    /**
     * Сохранение автарки пользователя.
     * ВНИМАНИЕ File не добавляется в поле User.
     * File не сохраняется в репозиторий
     * @param user - пользователь для которого нужно сохранить аватарку
     * @param avatarFile - файл с аватаркой
     * @return сущность аватарки
     */
    File saveUserAvatar(User user, MultipartFile avatarFile);
    File saveImage(LoanState state, MultipartFile file);
    String getImageURL(LoanState state);

    void removeImage(LoanState state) throws CannotRemoveObjectMinioException;
}
