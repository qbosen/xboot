package top.abosen.xboot.spring.repository.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2021/2/4
 */
public class SqlInjectEnhancer extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new ExistMethod());
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}