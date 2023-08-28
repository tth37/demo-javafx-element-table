package demo.ui.dto;

import java.util.List;
public class PageDto<T> {
    public int totalPages;
    public long totalElements;
    public List<T> content;
}
