package cn.content.service.impl;

import cn.content.service.LLContentService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mial.common.utils.TaotaoResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
public class LLContentServiceImpl implements LLContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${IMAGE_PATH_URL}")
    private String  IMAGE_PATH_URL;
    //缓存列表的key
    @Value("${CONTENT_LIST_KEY}")
    private  String CONTENT_LIST_KEY;
    @Override
    public TaotaoResult addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public LLPageCommon<TbContent> getGridResult(Integer row, Integer pagesize) {
        PageHelper.startPage(row,pagesize);
        TbContentExample contentExample = new TbContentExample();
        List<TbContent> contentList =  contentMapper.selectByExample(contentExample);

        PageInfo<TbContent> pageInfo = new PageInfo<>(contentList);

        LLPageCommon pageCommon = new LLPageCommon();

        pageCommon.setRows(contentList);

        pageCommon.setTotal(pageInfo.getTotal());

        return pageCommon;
    }

    @Override
    public TaotaoResult deleteItenContent(long id) {
        contentMapper.deleteByPrimaryKey(id);
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public List<TbContent> getAdContentList(long cid) {
        //先查询缓存是否存在
        try {
            String contentJson = jedisClient.hget(IMAGE_PATH_URL, cid+"");
            if (StringUtils.isNotBlank(contentJson)){
                Type type = new TypeToken<List<TbContent>>(){}.getType();
                List<TbContent> contentList = new Gson().fromJson(contentJson, type);
                return  contentList;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(contentExample);
        //拼接完整图片url
        for ( TbContent contents : contentList) {
            String picUrl = IMAGE_PATH_URL + contents.getPic();
            contents.setPic(picUrl);
        }
        //添加缓存
        try {
            Long result  = jedisClient.hset(CONTENT_LIST_KEY, cid + "", new  Gson().toJson(contentList));
            System.out.println( "存储结果 === " + result);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return contentList;
    }
}
