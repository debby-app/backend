package com.project.debby.domain.integrations.minio.service;

import com.project.debby.domain.integrations.minio.model.entity.ProfileAvatar;
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
     * ВНИМАНИЕ ProfileAvatar не добавляется в поле User.
     * ProfileAvatar не сохраняется в репозиторий
     * @param user - пользователь для которого нужно сохранить аватарку
     * @param avatarFile - файл с аватаркой
     * @return сущность аватарки
     */
    ProfileAvatar saveUserAvatar(User user, MultipartFile avatarFile);
}
