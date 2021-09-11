package vn.elca.training.model.response;

import lombok.NoArgsConstructor;
import vn.elca.training.model.entity.Project;

import java.util.List;

@NoArgsConstructor
public class ListResponse<T> {
    private int current;
    private int total;
    private int size;
    private List<T> data;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public ListResponse(int current, int total, int size, List<T> data) {
        this.current = current;
        this.total = total;
        this.size = size;
        this.data = data;
    }
}
