package top.abosen.dddboot.shared.data;

/**
 * @author qiubaisen
 * @date 2021/3/31
 */
public interface Pageable {
    int DEFAULT_PAGE = 1;

    int DEFAULT_SIZE = 20;

    int MIN_SIZE = 1;

    int MAX_SIZE = 200;

    Integer getPage();

    void setPage(Integer page);

    Integer getSize();

    void setSize(Integer size);
}
