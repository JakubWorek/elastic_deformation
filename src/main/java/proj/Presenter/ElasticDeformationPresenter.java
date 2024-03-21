package proj.Presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Spinner;
import proj.Model.Rozwiazanie;
import proj.Model.KalkulatorProsty;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ElasticDeformationPresenter implements Initializable {
    @FXML
    private Spinner<Integer> elementySkonczone;

    @FXML
    private LineChart<Number, Number> wykres;

    private KalkulatorProsty kalkulator = new KalkulatorProsty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // rekalkulacja wykresu po każdej zmianie wartości w spinnerze
        this.elementySkonczone.getValueFactory().valueProperty().addListener(value -> this.przekalkulujToJeszczeRaz());
        // wykres dokładnego rozwiązania
        XYChart.Series<Number, Number> dokladneRozwiazanie = new XYChart.Series<>();
        dokladneRozwiazanie.getData().add(new XYChart.Data<>(0d, 80d / 3d));
        dokladneRozwiazanie.getData().add(new XYChart.Data<>(1d, 10d));
        dokladneRozwiazanie.getData().add(new XYChart.Data<>(2d, 0d));
        this.wykres.getData().add(0, dokladneRozwiazanie);
        // dodajemy wykres przybliżonego rozwiązania
        this.przekalkulujToJeszczeRaz();
    }

    private void przekalkulujToJeszczeRaz() {
        // obliczamy rozwiązanie
        Rozwiazanie rozwiazanie = this.kalkulator.rozwiaz(this.elementySkonczone.getValue());
        // tworzymy serię danych do wykresu
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // dodajemy punkty do serii danych
        for (int i = 0; i < rozwiazanie.wspolczynniki().length; i++) {
            double x = (rozwiazanie.prawaGranica() - rozwiazanie.lewaGranica()) * i / (rozwiazanie.wspolczynniki().length - 1);
            series.getData().add(new XYChart.Data<>(x, Arrays.stream(rozwiazanie.wspolczynniki()).toArray()[i]));
        }
        // usuwamy starą serię danych i dodajemy nową
        if (this.wykres.getData().size() > 1) {
            this.wykres.getData().remove(1);
        }
        this.wykres.getData().add(1, series);
    }

}
