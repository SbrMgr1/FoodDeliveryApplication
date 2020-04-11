package com.food.service.core.service;

import com.food.service.config.Translator;
import com.food.service.core.entity.BookShelfEntity;
import com.food.service.core.enums.Status;
import com.food.service.core.params.BookShelfParam;
import com.food.service.core.repository.BookShelfRepository;
import com.food.service.core.util.ParamAndEntityBuilder;
import com.food.service.errorhandler.ErrorCodes;
import com.food.service.errorhandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookShelfServiceImpl implements BookShelfService {

    @Autowired
    private ParamAndEntityBuilder paramAndEntityBuilder;

    @Autowired
    private BookShelfRepository repository;

    @Autowired
    private Translator translator;

    @Override
    public void add(BookShelfParam param) {
        param.setStatus(Status.ACTIVE);
        repository.save(paramAndEntityBuilder.bookShelfParamToEntity(param));

    }

    @Override
    public void update(BookShelfParam param) {
        param.setStatus(Status.ACTIVE);
        repository.save(paramAndEntityBuilder.bookShelfParamToEntity(param));
    }

    @Override
    public BookShelfParam getOne(long id) throws ResourceNotFoundException {
        BookShelfEntity entity = repository.getOne(id);
        if (entity == null) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.BOOKSHELF_GET,
                    ErrorCodes.CODE.BOOKSELF_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.BOOKSELF_NOT_FOUND)));
        }

        return paramAndEntityBuilder.bookShelfEntityToParam(entity);
    }

    @Override
    public List<BookShelfParam> getAll() throws ResourceNotFoundException {
        List<BookShelfEntity> bookShelfEntities = repository.findAll();
        if (bookShelfEntities.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.BOOKSHELF_GET,
                    ErrorCodes.CODE.BOOKSELF_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.BOOKSELF_NOT_FOUND)));
        }

        return bookShelfEntities.stream()
                .filter(x -> x.getStatus().equals(Status.ACTIVE))
                .map(x -> paramAndEntityBuilder.bookShelfEntityToParam(x))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException {
        BookShelfEntity bookShelfEntity = repository.getOne(id);
        if (bookShelfEntity == null) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.BOOKSHELF_DELETE,
                    ErrorCodes.CODE.BOOKSELF_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.BOOKSELF_NOT_FOUND)));
        }

        bookShelfEntity.setStatus(Status.DELETED);
        repository.save(bookShelfEntity);
    }
}
