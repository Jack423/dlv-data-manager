package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("fish")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fish {
    @Id
    private long id;
    private String name;
    private String icon;
    private String description;
    private Integer energy;
    private Set<LocationEnum> locations;
    private RippleColor rippleColor;
    private Integer sellPrice;
    private String weatherCondition;

    public enum RippleColor {
        @JsonProperty("None")
        NONE("None"),
        @JsonProperty("White")
        WHITE("White"),
        @JsonProperty("Blue")
        BLUE("Blue"),
        @JsonProperty("Gold")
        GOLD("Gold")
        ;

        public final String value;

        RippleColor(String value) {
            this.value = value;
        }
    }
}
