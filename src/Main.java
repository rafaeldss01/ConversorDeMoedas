import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConversorDeMoedas {
    private static final String API_KEY = "42e1aae23121fc3246adb01d";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Conversor de Moedas!");

        // Exibir menu de opções
        while (true) {
            System.out.println("\nEscolha uma opção de conversão:");
            System.out.println("1. USD para BRL");
            System.out.println("2. BRL para USD");
            System.out.println("3. EUR para BRL");
            System.out.println("4. BRL para EUR");
            System.out.println("5. GBP para BRL");
            System.out.println("6. BRL para GBP");
            System.out.println("7. Sair");
            System.out.print("Digite a sua opção: ");

            int opcao = scanner.nextInt();
            if (opcao == 7) {
                System.out.println("Saindo do programa. Até logo!");
                break;
            }

            System.out.print("Digite o valor a ser convertido: ");
            double valor = scanner.nextDouble();

            String fromCurrency = "";
            String toCurrency = "";

            switch (opcao) {
                case 1:
                    fromCurrency = "USD";
                    toCurrency = "BRL";
                    break;
                case 2:
                    fromCurrency = "BRL";
                    toCurrency = "USD";
                    break;
                case 3:
                    fromCurrency = "EUR";
                    toCurrency = "BRL";
                    break;
                case 4:
                    fromCurrency = "BRL";
                    toCurrency = "EUR";
                    break;
                case 5:
                    fromCurrency = "GBP";
                    toCurrency = "BRL";
                    break;
                case 6:
                    fromCurrency = "BRL";
                    toCurrency = "GBP";
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
            }

            double taxa = obterTaxaDeCambio(fromCurrency, toCurrency);
            if (taxa != -1) {
                double resultado = valor * taxa;
                System.out.printf("Resultado: %.2f %s\n", resultado, toCurrency);
            } else {
                System.out.println("Erro ao obter a taxa de câmbio. Tente novamente mais tarde.");
            }
        }

        scanner.close();
    }

    // Método para obter a taxa de câmbio usando a API
    private static double obterTaxaDeCambio(String fromCurrency, String toCurrency) {
        try {
            String urlString = BASE_URL + fromCurrency;
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject rates = jsonResponse.getJSONObject("conversion_rates");
                return rates.getDouble(toCurrency);
            } else {
                System.out.println("Erro: Código de resposta da API " + responseCode);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
            return -1;
        }
    }
}
