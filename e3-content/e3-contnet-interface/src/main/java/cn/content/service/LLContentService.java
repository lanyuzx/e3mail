package cn.content.service;

import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbContent;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLContentService {

   TaotaoResult addContent(TbContent content);
   LLPageCommon<TbContent> getGridResult(Integer row , Integer pagesize);
   TaotaoResult deleteItenContent(long id);
}
