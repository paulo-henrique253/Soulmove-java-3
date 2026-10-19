package br.com.soulmove.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PixValidator {

    private static final String CNPJ_SPTRANS = "60498411000183";

    public String extrairNomeRecebedor(String pixCopiaECola) {
        try {
            // Encontra o índice do ID 59
            int index59 = pixCopiaECola.indexOf("59");
            if (index59 == -1) return null;

            // Os próximos 2 caracteres são o tamanho da string do nome
            int tamanhoNome = Integer.parseInt(pixCopiaECola.substring(index59 + 2, index59 + 4));

            // Extrai o nome baseado no tamanho lido
            return pixCopiaECola.substring(index59 + 4, index59 + 4 + tamanhoNome);
        } catch (Exception e) {
            return null; // Falha no parse
        }
    }

    public BigDecimal extrairValor(String pixCopiaECola) {
        if (pixCopiaECola == null || pixCopiaECola.isEmpty()) {
            return null;
        }

        int i = 0;
        while (i < pixCopiaECola.length()) {
            try {
                // 1. Lê a Tag (ID) - 2 caracteres
                String id = pixCopiaECola.substring(i, i + 2);
                i += 2;

                // 2. Lê o Tamanho (Length) - 2 caracteres
                int tamanho = Integer.parseInt(pixCopiaECola.substring(i, i + 2));
                i += 2;

                // 3. Lê o Valor (Value) com base no tamanho extraído
                String valor = pixCopiaECola.substring(i, i + tamanho);
                i += tamanho; // Avança o índice para o início do próximo bloco

                // Se encontrou o ID 54 (Transaction Amount), converte e retorna
                if ("54".equals(id)) {
                    // Retorna como BigDecimal (padrão ouro para dinheiro em Java)
                    return new BigDecimal(valor);
                }
            } catch (Exception e) {
                // Interrompe caso a string seja malformada
                System.err.println("Erro estrutural no PIX no índice: " + i);
                return null;
            }
        }

        // Retorna nulo se o PIX não tiver a Tag 54 (PIX com valor aberto, onde o pagador digita)
        return null;
    }

    public boolean validarCrc(String pixCopiaECola) {
        if (pixCopiaECola == null || pixCopiaECola.length() < 4) {
            return false;
        }

        // 1. O hash original fornecido são sempre os últimos 4 caracteres
        String crcFornecido = pixCopiaECola.substring(pixCopiaECola.length() - 4).toUpperCase();

        // 2. O payload para o cálculo exclui os 4 últimos caracteres (mas inclui o "6304" final)
        String payload = pixCopiaECola.substring(0, pixCopiaECola.length() - 4);

        // 3. Calcula o hash real e compara
        String crcCalculado = calcularCrc16Ccitt(payload);

        return crcFornecido.equals(crcCalculado);
    }

    private String calcularCrc16Ccitt(String payload) {
        int crc = 0xFFFF; // Valor inicial definido pelo padrão EMV
        int polinomio = 0x1021; // Polinômio CCITT-FALSE

        // O padrão exige que a string seja tratada em UTF-8
        byte[] bytes = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        for (byte b : bytes) {
            // (b & 0xFF) é crucial em Java para evitar propagação de sinal negativo no byte
            crc ^= (b & 0xFF) << 8;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polinomio;
                } else {
                    crc <<= 1;
                }
            }
            // Garante que o valor não passe de 16 bits após as operações bit a bit
            crc &= 0xFFFF;
        }

        // Retorna o hash formatado como 4 caracteres hexadecimais em maiúsculo (ex: "A1B2")
        return String.format("%04X", crc);
    }

    public boolean validarPixSpTrans(String pixCopiaECola) {
        if (pixCopiaECola == null || pixCopiaECola.isEmpty()) {
            return false;
        }

        String pixUpper = pixCopiaECola.toUpperCase();

        // Verifica se a string contém o CNPJ ou o nome de registro
        boolean contemCnpj = pixUpper.contains(CNPJ_SPTRANS);
        boolean contemNome = pixUpper.contains("SPTRANS") ||
                pixUpper.contains("SAO PAULO TRANSPORTE");

        return contemCnpj || contemNome;
    }

}
