package de.gabriel.vertretungsplan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetTeacherAttendanceRequest {

    private String attendance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetTeacherAttendanceRequest that = (SetTeacherAttendanceRequest) o;
        return attendance.equals(that.attendance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendance);
    }

}
