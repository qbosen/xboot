package top.abosen.dddboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/2/4
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);

    boolean exist(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
