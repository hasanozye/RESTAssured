package zippo.pojoclasses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Place {

    @JsonProperty("place name") // Doğru şekilde eşleştirildi
    private String placeName;
    @JsonProperty("longitude") // Yazım hatası düzeltildi
    private String longitude; // 'longtiude' yerine 'longitude' olmalı
    private String state;
    @JsonProperty("state abbreviation") // Doğru şekilde eşleştirildi
    private String stateAbbreviation;
    private String latitude;
}
