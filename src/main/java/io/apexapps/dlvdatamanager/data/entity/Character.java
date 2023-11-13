package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("characters")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {
    @Id
    private Long id;
    private String name;
    private String icon;
    private String characterRole;
    private String information;
    private Integer level;
    private List<Quest> quests;
    private List<Schedule> schedule;
    private String scheduleString;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Quest {
        private QuestCategory category;
        private String description;
        private String name;
        private List<QuestItem> requiredItems = new ArrayList<>();
    }

    public enum QuestCategory {
        @JsonProperty("realm")
        REALM,
        @JsonProperty("story")
        STORY,
        @JsonProperty("friendship")
        FRIENDSHIP
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestItem {
        private String additionalInformation;
        private Integer amount;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        private Integer start;
        private Integer end;
        private String location;
    }
}
