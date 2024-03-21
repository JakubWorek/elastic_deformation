package proj.Model;
// do całkowania
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
// do rozwiązywania układu równań i macierzy
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.LUDecomposition;
// no tablica zwykła
import java.util.Arrays;

public class KalkulatorProsty {
    // maszynka do całkowania
    private final UnivariateIntegrator maszynkaDoCaleczek = new IterativeLegendreGaussIntegrator(50, 1e-6, 1e-6);

    public Rozwiazanie rozwiaz(int N) {
        // N - liczba elementów skończonych
        sprawdz(N);
        // długość przedziału całkowania - [0,2] z treści zadania
        // Budujemy macierz B
        RealMatrix macierzB = zbudujMacierz(N);
        // Budujemy wektor l
        RealVector wektorL = zbudujWektor(N);
        // Rozwiązujemy układ równań - wyliczamy współczynniki
        RealVector wspolczynniki = rozwiazRownanieDekomponujac(macierzB, wektorL);
        // Dodajemy współczynniki dla warunku brzegowego
        double[] wyniki = Arrays.copyOf(wspolczynniki.toArray(), N + 1);
        wyniki[N] = 0;
        // Zwracamy rekord representujący rozwiązanie
        return new Rozwiazanie(0.0, 2.0, wyniki);
    }

    private RealMatrix zbudujMacierz(int N) {
        RealMatrix macierzB = new Array2DRowRealMatrix(N, N);

        for (int n = 0; n < N; n++) {
            for (int m = 0; m < N; m++) {
                double calka = policzCalke(N, n, m);

                macierzB.setEntry(n, m, -3.0 * funkcjaKsztaltu(N, n, 0) * funkcjaKsztaltu(N, m, 0) + calka);
            }
        }

        return macierzB;
    }

    private double policzCalke(int N, int n, int m) {
        double caleczka = 0;

        if (Math.abs(m - n) <= 1) {
            // znajdujemy przecięcie dziedzin funkcji bazowej i dziedziny globalnej [0, 2]
            double calkaOd = 2.0 * Math.max(Math.max(n, m) - 1, 0) / N;
            double calkaDo = 2.0 * Math.min(Math.min(n, m) + 1, N) / N;

            caleczka = maszynkaDoCaleczek.integrate(
                    Integer.MAX_VALUE, // maksymalna liczba iteracji całkowania
                    x -> E(x) * pochodnaFunkcjiKsztaltu(N, n, x) * pochodnaFunkcjiKsztaltu(N, m, x), // funkcja podcałkowa
                    calkaOd, // dolna granica całkowania
                    calkaDo    // górna granica całkowania
            );
        }

        return caleczka;
    }

    private RealVector zbudujWektor(int N) {
        RealVector wektorL = new ArrayRealVector(N, 0);
        wektorL.setEntry(0, - 30.0 * funkcjaKsztaltu(N, 0, 0));
        return wektorL;
    }

    private RealVector rozwiazRownanieDekomponujac(RealMatrix bMatrix, RealVector lVector) {
        return new LUDecomposition(bMatrix).getSolver().solve(lVector);
    }

    private static void sprawdz(int N) {
        if (N < 2) {
            throw new IllegalArgumentException("Liczba elementow skonczonych musi byc >= 2");
        }
    }

    private static double E(double x) {
        // z treści zadania funkcja E jest określona w następujący sposób:
        return x <= 1.0 ? 3.0 : 5.0;
    }

    private static double funkcjaKsztaltu(int N, int i, double x) {
        // h = 2.0 / N
        // h w v = N / 2.0
        // środek to 2.0*i/N
        // lewa to środek - h
        // prawa to środek + h
        if (x < (2.0 * i / N) - 2.0/N || x > (2.0 * i / N) + 2.0/N) return 0.0;
        // x <= środek ? x-lewa * N/2.0 : prawa-x * N/2.0
        return x <= 2.0 * i / N ? (x - ((2.0 * i / N) - 2.0/N)) * (N/2.0) : (((2.0 * i / N) + 2.0/N) - x) * (N/2.0);
    }

    private static double pochodnaFunkcjiKsztaltu(int N, int i, double x) {
        // h = 2.0 / N
        // h w v = N / 2.0
        // środek to 2.0*i/N
        // lewa to środek - h
        // prawa to środek + h
        if (x < (2.0 * i / N) - 2.0/N || x > (2.0 * i / N) + 2.0/N) return 0.0;
        // x <= środek ? h w v : - h w v
        return x <= 2.0*i/N  ? N/2.0 : -N/2.0;
    }
}