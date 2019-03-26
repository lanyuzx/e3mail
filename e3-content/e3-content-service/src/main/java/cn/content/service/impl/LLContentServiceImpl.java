package cn.content.service.impl;

import cn.content.service.LLContentService;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mial.common.utils.TaotaoResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LLContentServiceImpl implements LLContentService {
    @Autowired
    private TbContentMapper contentMapper;
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
}
