package cz.cvut.fit.tjv.hujomark.project.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class MatchDto {
    public Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy HH:mm")
    public LocalDateTime dateTime;

    public Long teamId;

    public MatchDto() {}

    public MatchDto(Long id, LocalDateTime dateTime, Long teamId) {
        this.id = id;
        this.dateTime = dateTime;
        this.teamId = teamId;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
