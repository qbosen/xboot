package top.abosen.xboot.demo;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.widget.BizWidgetExtension;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/3/1
 */

@AutoService(BizWidgetExtension.class)
@Getter
@Slf4j
@EqualsAndHashCode
@ToString
public class VideoBizExtension implements BizWidgetExtension {
    final String key = "video";


    @Override
    public boolean checkValue(ValueHolder valueHolder) {

        // video 类型在前端操作时为String, 检验的schema与前端类型一致;
        // 在数据库存储时变为long类型; biz widget的check最后执行

        if (valueHolder != null && valueHolder.get() != null && valueHolder.get() instanceof String ) {
            String raw = ((String) valueHolder.get());
            Long video = Long.parseLong(raw) + 1000000L;
            log.info("传入视频id:{}, 存储视频id: {}", raw, video);
            valueHolder.set(video);
            return true;
        }
        return false;
    }

    @Override
    public void updateValue(ValueHolder valueHolder) {
        // video 类型保存为了 long, 前台再转为相应的业务信息
        if (valueHolder != null && valueHolder.get() != null && valueHolder.get() instanceof Number) {
            // 业务处理
            Long vid = ((Number) valueHolder.get()).longValue();
            String mid = String.valueOf(vid - 1000000L);
            Map<String, Object> videoModel = ImmutableMap.of("id", mid, "name", "视频名称",
                    "url", "http://www.baidu.com");
            log.info("数据库获取:{}, 转换返回: {}", vid, videoModel);
            valueHolder.set(videoModel);
        }
    }
}
