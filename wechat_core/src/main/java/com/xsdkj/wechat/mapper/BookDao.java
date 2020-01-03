package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.other.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/9 14:29
 */
public interface BookDao extends MongoRepository<Book, Integer> {
    List<Book> findByAuthorContains(String author);

    Book findByNameEquals(String name);

    List<Book> findByIdLessThan(Integer id);

    List<Book> findBookByIdBetween(int min, int max);
}
