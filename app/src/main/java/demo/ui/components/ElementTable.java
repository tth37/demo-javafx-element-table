package demo.ui.components;

import com.alibaba.fastjson2.JSONObject;
import demo.ui.dto.PageDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ElementTable<T> extends Pagination {

    private Function<Integer, PageDto<T>> pageCallback;
    private List<Pair<String, String>> columns;

    public ElementTable() {
        super();
        this.setMaxPageIndicatorCount(5);
    }

    public void setColumns(List<Pair<String, String>> columns) {
        this.columns = columns;
    }

    public void setPageCallback(Function<Integer, PageDto<T>> callback) {
        this.pageCallback = callback;

        this.setPageCount(this.pageCallback.apply(0).totalPages);

        this.setPageFactory((pageIndex) -> {
            TableView tableView = new TableView();

            for (Pair<String, String> column : this.columns) {
                TableColumn<Map, String> tableColumn = new TableColumn<>(column.getKey());
                tableColumn.setCellValueFactory(new MapValueFactory<>(column.getValue()));
                tableView.getColumns().add(tableColumn);
            }

            ObservableList<Map<String, Object>> items =
                    FXCollections.<Map<String, Object>>observableArrayList();

            PageDto<T> page = this.pageCallback.apply(pageIndex);

            for (T item : page.content) {
                Map<String, Object> map = new HashMap<>();
                for (Pair<String, String> column : this.columns) {
                        map.put(column.getValue(), ((JSONObject) item).get(column.getValue()));
                }
                items.add(map);
            }

            tableView.getItems().addAll(items);

            return tableView;
        });
    }
}