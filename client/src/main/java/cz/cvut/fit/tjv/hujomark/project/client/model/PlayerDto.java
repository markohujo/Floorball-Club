package cz.cvut.fit.tjv.hujomark.project.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

public class PlayerDto {
    public Long id;

    public String name;

    public String surname;

    public String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate dateOfBirth;

    public Set<Long> teams;

    public Long tmpTeamId;

    public PlayerDto() {}

    public PlayerDto(Long id, String name, String surname, String email, LocalDate dateOfBirth, Set<Long> teams) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Long> getTeams() {
        return teams;
    }

    public void setTeams(Set<Long> teams) {
        this.teams = teams;
    }

    public Long getTmpTeamId() {
        return tmpTeamId;
    }

    public void setTmpTeamId(Long tmpTeamId) {
        this.tmpTeamId = tmpTeamId;
    }
}
