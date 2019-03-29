package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class LLSearchResult implements Serializable {

    private List<LLSearchItem> itemList;
    private  int totalPages;
    private int  recourdCount;

    public List<LLSearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<LLSearchItem> itemList) {
        this.itemList = itemList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(int recourdCount) {
        this.recourdCount = recourdCount;
    }
}
