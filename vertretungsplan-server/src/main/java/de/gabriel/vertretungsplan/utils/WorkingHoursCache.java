package de.gabriel.vertretungsplan.utils;

import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class WorkingHoursCache {

    private final HashMap<Teacher, Integer> workHoursCache = new HashMap<>();

    public void addWorkHours(Teacher teacher, Integer workHours) {
        if (workHoursCache.containsKey(teacher)) {
            workHoursCache.put(teacher, workHoursCache.get(teacher) + workHours);
        } else {
            workHoursCache.put(teacher, workHours);
        }
    }

    public int getWorkHours(Teacher teacher) {
        return workHoursCache.getOrDefault(teacher, 0);
    }

    public void clearCache() {
        workHoursCache.clear();
    }

}
