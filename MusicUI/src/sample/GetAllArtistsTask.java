package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import sample.model.Artist;
import sample.model.Datasource;

class GetAllArtistsTask extends Task {

    @Override
    public ObservableList<Artist> call()  {
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }
}