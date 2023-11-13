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

import java.util.List;
import java.util.Set;

@Document("critters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Critter {
    @Id
    private long id;
    private String name;
    private String icon;
    private String howToFeed;
    private CritterType type;
    private Set<String> favoriteFood;
    private Set<String> likedFood;
    private LocationEnum location;
    private List<CritterSchedule> schedule;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CritterSchedule {
        private DayOfTheWeek day;
        private String start;
        private String end;
    }

    public enum CritterType {
        @JsonProperty("squirrel")
        SQUIRREL("Squirrel"),
        @JsonProperty("seaTurtle")
        SEA_TURTLE("Sea Turtle"),
        @JsonProperty("fox")
        FOX("Fox"),
        @JsonProperty("raccoon")
        RACCOON("Raccoon"),
        @JsonProperty("rabbit")
        RABBIT("Rabbit"),
        @JsonProperty("crocodile")
        CROCODILE("Crocodile"),
        @JsonProperty("sunbird")
        SUNBIRD("Sunbird"),
        @JsonProperty("raven")
        RAVEN("Raven");

        public final String value;

        CritterType(String value) {
            this.value = value;
        }
    }

    public enum DayOfTheWeek {
        @JsonProperty("Monday")
        MONDAY("Monday"),
        @JsonProperty("Tuesday")
        TUESDAY("Tuesday"),
        @JsonProperty("Wednesday")
        WEDNESDAY("Wednesday"),
        @JsonProperty("Thursday")
        THURSDAY("Thursday"),
        @JsonProperty("Friday")
        FRIDAY("Friday"),
        @JsonProperty("Saturday")
        SATURDAY("Saturday"),
        @JsonProperty("Sunday")
        SUNDAY("Sunday");

        public final String value;

        DayOfTheWeek(String value) {
            this.value = value;
        }
    }
}
