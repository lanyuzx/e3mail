package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.LLSearchResult;
import cn.search.service.LLSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LLSearchController {
    @Autowired
    private LLSearchService searchService;
    @Value("${PAGE_ROWS}")
    private long PAGE_ROWS;
    @RequestMapping("/search.html")
    public String searchItem(String keyword, @RequestParam(defaultValue="1") Integer page, Model model)  {
        try {
            LLSearchResult searchResult = searchService.searchItem(keyword, page, (int) PAGE_ROWS);
            keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
            model.addAttribute("query", keyword);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("recourdCount", searchResult.getRecourdCount());
            model.addAttribute("page", page);
            model.addAttribute("itemList", searchResult.getItemList());
            return "search";
        }catch (Exception e) {
            e.printStackTrace();
            return  "/error/exception";
        }

    }
}
