package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class LLPageCommon<T> implements Serializable {
    private  long total;
    List<T>    rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }



}
