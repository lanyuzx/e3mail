package cn.e3mall.portal.controller;

import cn.content.service.LLContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LLIndexController {
    @Autowired
    private LLContentService contentService;
    @Value("${AD_LUNBO_CID}")
    private long AD_LUNBO_CID;
    @RequestMapping("index")
    public  String index(Model model) {
       List<TbContent> contentList =  contentService.getAdContentList(AD_LUNBO_CID);
       model.addAttribute("ad1List", contentList);
        return  "index";
    }
}
