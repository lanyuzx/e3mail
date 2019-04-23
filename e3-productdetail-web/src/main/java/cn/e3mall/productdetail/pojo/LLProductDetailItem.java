package cn.e3mall.productdetail.pojo;

import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

public class LLProductDetailItem extends TbItem {


    public LLProductDetailItem(TbItem item) {
        this.setId(item.getId());
        this.setTitle(item.getTitle());
        this.setSellPoint(item.getSellPoint());
        this.setPrice(item.getPrice());
        this.setNum(item.getNum());
        this.setBarcode(item.getBarcode());
        this.setImage(item.getImage());
        this.setCid(item.getCid());
        this.setStatus(item.getStatus());
        this.setCreated(item.getCreated());
        this.setUpdated(item.getUpdated());
    }

    public String[] getImages(){
        String imgs = this.getImage();
        if (StringUtils.isNotBlank(imgs)){
            return  imgs.split(",");
        }
        return  null;
    }
}
