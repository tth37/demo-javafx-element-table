package demo.ui.components;

import com.alibaba.fastjson2.JSONObject;
import demo.ui.dto.PageDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ElementTable<T> extends Pagination {
    private Function<Integer, PageDto<T>> pageCallback;
    private Function<T, AnchorPane> doubleClickRenderer;
    private List<Pair<String, String>> columns;
    private Type elementType;
    private double dialogWidth, dialogHeight;

    public ElementTable(Type elementType) {
        super();

        this.elementType = elementType;

        this.setMaxPageIndicatorCount(5);

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

            tableView.setOnMouseClicked((event) -> {
                if (event.getClickCount() == 2) {
                    HashMap<String, Object> map = (HashMap<String, Object>) tableView.getSelectionModel().getSelectedItem();
                    T item = (T) new JSONObject(map).to(elementType);
//                    this.doubleClickCallback.apply((T) new JSONObject(map).to(elementType));
                    Stage dialog = new Stage();
                    dialog.initOwner(this.getScene().getWindow());
                    dialog.initModality(Modality.WINDOW_MODAL);
                    dialog.setWidth(this.dialogWidth);
                    dialog.setHeight(this.dialogHeight);

                    Scene dialogScene = new Scene(this.doubleClickRenderer.apply(item), this.dialogWidth, this.dialogHeight);

                    dialog.setScene(dialogScene);

                    dialog.show();
                }
            });

            return tableView;
        });
    }

    public void setColumns(List<Pair<String, String>> columns) {
        this.columns = columns;
    }

    public void setDialogSize(double width, double height) {
        this.dialogWidth = width;
        this.dialogHeight = height;
    }

    public void setPageCallback(Function<Integer, PageDto<T>> callback) {
        this.pageCallback = callback;

        this.setPageCount(this.pageCallback.apply(0).totalPages);
    }

    public void setDoubleClickRenderer(Function<T, AnchorPane> callback) {
        this.doubleClickRenderer = callback;
    }
}