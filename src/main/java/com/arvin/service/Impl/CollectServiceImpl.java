package com.arvin.service.Impl;

import com.arvin.common.Response;
import com.arvin.dao.CollectMapper;
import com.arvin.dao.ProductMapper;
import com.arvin.pojo.Collect;
import com.arvin.service.ICollectService;
import com.arvin.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/5/15.
 */
@Service("iCollectService")
public class CollectServiceImpl implements ICollectService{


    private static final Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);

    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ProductMapper productMapper;


    public Response addCollect(Collect collect){
        //先查询表中是否已有改商品
        int count = collectMapper.selectCollect(collect);
        if(count > 0){
            return Response.createByErrorMessage("您已收藏改商品");
        }
        //没有则插入数据库
        else {
            int result = collectMapper.insertCollect(collect);
            if(result > 0){
                return Response.createBySuccess();
            }else {
                return Response.createByErrorMessage("收藏失败");
            }
        }

    }


    public Response collectList(Integer userId){
        try{
            Map<String,Object> map = new HashMap<>();
            List<Map<String,String>> list = collectMapper.selectCollectList(userId);
            if(list == null){
                return Response.createByErrorMessage("您没有收藏商品，快去浏览商品吧！");
            }
           /* ProductCollectVo productCollectVo = new ProductCollectVo();
            List<ProductCollectVo> list1 = new ArrayList<>();*/
            for(Map<String,String> listItem : list){
                String str = listItem.get("main_image");
                listItem.put("main_image", PropertiesUtil.getProperty("ftp.server.http.prefix")+str);
            }
            map.put("res",list);
            return Response.createBySuccess(map);
        }catch (RuntimeException e){
            logger.error("运行异常" ,e);
        }
        return Response.createBySuccess();

    }

    public Response delCollect(Collect collect){
        collectMapper.deleteCollect(collect);
        return Response.createBySuccess();
    }
}
