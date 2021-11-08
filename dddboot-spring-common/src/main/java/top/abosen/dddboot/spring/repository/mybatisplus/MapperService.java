package top.abosen.dddboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import top.abosen.dddboot.shared.data.Query;
import top.abosen.dddboot.shared.data.SliceList;
import top.abosen.dddboot.spring.data.PageList;
import top.abosen.dddboot.spring.data.QueryUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/7/2
 */
public class MapperService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

    @Transactional(rollbackFor = Exception.class)
    public boolean save(List<T> list) {
        return CollectionUtils.isNotEmpty(list) && SqlHelper.retBool(baseMapper.insertBatchSomeColumn(list));
    }

    public boolean exist(Wrapper<T> queryWrapper) {
        return baseMapper.exist(queryWrapper);
    }

    public SliceList<T> page(Query query, Wrapper<T> queryWrapper) {
        return PageList.of(page(QueryUtils.ipage(query), queryWrapper));
    }

    @Override public List<T> listByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectBatchIds(idList);
    }
}
