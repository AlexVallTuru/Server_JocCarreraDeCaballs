/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

/**
 *
 * @author aitor
 */
public class EnumCartes {
    public enum Tipus {
        UNO_DE_OROS("Oros", "1_de_Oros.png", 1),
        DOS_DE_OROS("Oros", "2_de_Oros.png", 2),
        TRES_DE_OROS("Oros", "3_de_Oros.png", 3),
        CUATRO_DE_OROS("Oros", "4_de_Oros.png", 4),
        CINCO_DE_OROS("Oros", "5_de_Oros.png", 5),
        SEIS_DE_OROS("Oros", "6_de_Oros.png", 6),
        SIETE_DE_OROS("Oros", "7_de_Oros.png", 7),
        DIEZ_DE_OROS("Oros", "10_de_Oros.png", 10),
        ONCE_DE_OROS("Oros", "11_de_Oros.png", 11),
        DOCE_DE_OROS("Oros", "12_de_Oros.png", 12),
        // Copas
        UNO_DE_COPAS("Copas", "1_de_Copas.png", 1),
        DOS_DE_COPAS("Copas", "2_de_Copas.png", 2),
        TRES_DE_COPAS("Copas", "3_de_Copas.png", 3),
        CUATRO_DE_COPAS("Copas", "4_de_Copas.png", 4),
        CINCO_DE_COPAS("Copas", "5_de_Copas.png", 5),
        SEIS_DE_COPAS("Copas", "6_de_Copas.png", 6),
        SIETE_DE_COPAS("Copas", "7_de_Copas.png", 7),
        DIEZ_DE_COPAS("Copas", "10_de_Copas.png", 10),
        ONCE_DE_COPAS("Copas", "11_de_Copas.png", 11),
        DOCE_DE_COPAS("Copas", "12_de_Copas.png", 12),
        // Espadas
        UNO_DE_ESPADAS("Espadas", "1_de_Espadas.png", 1),
        DOS_DE_ESPADAS("Espadas", "2_de_Espadas.png", 2),
        TRES_DE_ESPADAS("Espadas", "3_de_Espadas.png", 3),
        CUATRO_DE_ESPADAS("Espadas", "4_de_Espadas.png", 4),
        CINCO_DE_ESPADAS("Espadas", "5_de_Espadas.png", 5),
        SEIS_DE_ESPADAS("Espadas", "6_de_Espadas.png", 6),
        SIETE_DE_ESPADAS("Espadas", "7_de_Espadas.png", 7),
        DIEZ_DE_ESPADAS("Espadas", "10_de_Espadas.png", 10),
        ONCE_DE_ESPADAS("Espadas", "11_de_Espadas.png", 11),
        DOCE_DE_ESPADAS("Espadas", "12_de_Espadas.png", 12),
        // Bastos
        UNO_DE_BASTOS("Bastos", "1_de_Bastos.png", 1),
        DOS_DE_BASTOS("Bastos", "2_de_Bastos.png", 2),
        TRES_DE_BASTOS("Bastos", "3_de_Bastos.png", 3),
        CUATRO_DE_BASTOS("Bastos", "4_de_Bastos.png", 4),
        CINCO_DE_BASTOS("Bastos", "5_de_Bastos.png", 5),
        SEIS_DE_BASTOS("Bastos", "6_de_Bastos.png", 6),
        SIETE_DE_BASTOS("Bastos", "7_de_Bastos.png", 7),
        DIEZ_DE_BASTOS("Bastos", "10_de_Bastos.png", 10),
        ONCE_DE_BASTOS("Bastos", "11_de_Bastos.png", 11),
        DOCE_DE_BASTOS("Bastos", "12_de_Bastos.png", 12);

        private String value;
        private String imageName;
        private int score;

        Tipus(String value, String imageName,
                int score
        ) {
            this.value = value;
            this.imageName = imageName;
            this.score = score;
        }

        public String getValue() {
            return value;
        }

        public String getImageName() {
            return imageName;
        }

        public int getScore() {
            return score;
        }
    }

    //private Map<Enum, Image> tiupsImages;
}
