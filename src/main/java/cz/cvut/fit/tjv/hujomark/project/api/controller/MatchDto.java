package cz.cvut.fit.tjv.hujomark.project.api.controller;

import java.time.LocalDateTime;

public class MatchDto {
    public Long id;

    public LocalDateTime dateTime;

    public MatchDto() {}

    public MatchDto(Long id, LocalDateTime dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
