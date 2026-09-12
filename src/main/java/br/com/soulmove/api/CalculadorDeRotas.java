package br.com.soulmove.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CalculadorDeRotas {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Classe simples para armazenar coordenadas
    public record Coordenadas(double lat, double lon) {}

    // 1. Geocodificação usando Nominatim
    public static Coordenadas buscarCoordenadas(String endereco) throws Exception {
        String enderecoCodificado = URLEncoder.encode(endereco, StandardCharsets.UTF_8);
        String url = "https://nominatim.openstreetmap.org/search?q=" + enderecoCodificado + "&format=json&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                // IMPORTANTE: Definir o User-Agent exigido pelo Nominatim
                .header("User-Agent", "MeuAppJava/1.0 (contato@meudominio.com)")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode rootNode = objectMapper.readTree(response.body());

        if (rootNode.isArray() && !rootNode.isEmpty()) {
            JsonNode primeiroResultado = rootNode.get(0);
            double lat = primeiroResultado.get("lat").asDouble();
            double lon = primeiroResultado.get("lon").asDouble();
            return new Coordenadas(lat, lon);
        }

        return null;
    }

    // 2. Cálculo de Rota usando OSRM
    public static double calcularRota(Coordenadas origem, Coordenadas destino) throws Exception {
        // Usa Locale.US para garantir o ponto decimal (ex: -23.561410 e não -23,561410)
        String url = String.format(Locale.US,
                "https://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=false",
                origem.lon(), origem.lat(), destino.lon(), destino.lat()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprime o status HTTP para saber se a API recusou a chamada
        if (response.statusCode() != 200) {
            System.err.println("Erro na API OSRM (Status HTTP " + response.statusCode() + "): " + response.body());
            return 0.0;
        }

        JsonNode rootNode = objectMapper.readTree(response.body());

        if (rootNode.has("routes") && !rootNode.get("routes").isEmpty()) {
            JsonNode rota = rootNode.get("routes").get(0);

            double distanciaMetros = rota.get("distance").asDouble();
            double duracaoSegundos = rota.get("duration").asDouble();

            return distanciaMetros / 1000.0;
        } else {
            System.out.println("Não foi possível calcular a rota. Resposta da API: " + response.body());
        }
        return  -1.0;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Buscando coordenadas...");
            Coordenadas origem = buscarCoordenadas("Avenida Paulista 1000, Sao Paulo");
            Coordenadas destino = buscarCoordenadas("MASP, Sao Paulo");

            if (origem != null && destino != null) {
                System.out.println("Origem encontrada: " + origem);
                System.out.println("Destino encontrado: " + destino);
                System.out.println("Calculando rota...");

                calcularRota(origem, destino);
            } else {
                System.out.println("Endereço não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}