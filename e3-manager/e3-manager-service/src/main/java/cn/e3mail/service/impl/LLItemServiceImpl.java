package cn.e3mail.service.impl;

import cn.e3mail.service.LLItemService;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mial.common.utils.IDUtils;
import cn.e3mial.common.utils.TaotaoResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LLItemServiceImpl implements LLItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {

        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public LLPageCommon<TbItem> getGridResult(Integer row, Integer pagesize) {
        PageHelper.startPage(row,pagesize);
        TbItemExample itemExample = new TbItemExample();
        List <TbItem> itemList =  itemMapper.selectByExample(itemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
        LLPageCommon pageCommon = new LLPageCommon();
        pageCommon.setRows(itemList);
        pageCommon.setTotal(pageInfo.getTotal());
        return pageCommon;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //补全数据
        //生成产品的id
       long itemId =  IDUtils.genItemId();
       item.setId(itemId);
        //生成创建时间
        Date createdData =  new Date();
        item.setCreated(createdData);
       //生成更新时间
        Date updatedData =  new Date();
        item.setUpdated(updatedData);
       //生成状态
        item.setStatus((byte) 1);
        //商品描述
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(createdData);
        itemDesc.setUpdated(updatedData);
        //插入数据

        //先插入描述数据
       int result =   itemDescMapper.insert(itemDesc);
        System.out .println("插入结果 === " + result);
        itemMapper.insert(item);
        //创建返回结果pojo
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public TaotaoResult deleteItem(long id) {
        //先删除产品描述
        itemDescMapper.deleteByPrimaryKey(id);
        //在删除商品
        itemMapper.deleteByPrimaryKey(id);
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public TaotaoResult findItemDescByIdToJson(long id) {
       TbItemDesc itemDesc =  itemDescMapper.selectByPrimaryKey(id);
       //String itemDescJson  = new Gson().toJson(itemDesc);
       TaotaoResult taotaoResult = new TaotaoResult(itemDesc);
        return taotaoResult;
    }

    @Override
    public TaotaoResult findItemByIdToJson(long id) {
       TbItem item =  itemMapper.selectByPrimaryKey(id);
      // String itemJson = new Gson().toJson(item);
       TaotaoResult taotaoResult = new TaotaoResult(item);
        return taotaoResult;
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {

        return null;
    }
}
