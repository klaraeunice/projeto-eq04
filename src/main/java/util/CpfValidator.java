package util;
    /**
     * Classe utilitária para validação matemática de CPF.
     */
    public class CpfValidator {

        /**
         * Valida um CPF verificando seus dígitos verificadores.
         * @param cpf O CPF a ser validado (pode conter pontos e traços)
         * @return true se o CPF for válido, false caso contrário.
         */
        public static boolean isValid(String cpf) {
            if (cpf == null) return false;

            // Remove caracteres não numéricos (pontos e traços)
            cpf = cpf.replaceAll("\\D", "");

            // Verifica se tem 11 dígitos ou se todos são iguais (ex: 111.111.111-11)
            if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
                return false;
            }

            try {
                // Cálculo do primeiro dígito verificador
                int sum = 0;
                for (int i = 0; i < 9; i++) {
                    sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
                }
                int remainder = 11 - (sum % 11);
                int digito1 = (remainder >= 10) ? 0 : remainder;

                if (digito1 != Character.getNumericValue(cpf.charAt(9))) {
                    return false;
                }

                // Cálculo do segundo dígito verificador
                sum = 0;
                for (int i = 0; i < 10; i++) {
                    sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
                }
                remainder = 11 - (sum % 11);
                int digito2 = (remainder >= 10) ? 0 : remainder;

                return digito2 == Character.getNumericValue(cpf.charAt(10));
            } catch (Exception e) {
                return false;
            }
        }
    }
