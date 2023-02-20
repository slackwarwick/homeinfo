package persistence;

import domain.entity.pomodoro.Duration;
import domain.entity.pomodoro.Pomodoro;
import domain.entity.pomodoro.PomodoroTask;
import domain.usecase.pomodoro.PomodoroStore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class PomodoroStoreCsv extends PomodoroStore {
    public static final String FILENAME = "homeinfo_tasks.csv";
    public static final String SEP = ";";

    @Override
    public void saveTask(PomodoroTask task) {
        task.setId(UUID.randomUUID().toString());
        List<Pomodoro> tasks = readAll();
        tasks.add(task);
        writeAll(tasks);
    }

    @Override
    public List<Pomodoro> getTasks() {
        return readAll();
    }

    @Override
    public void updateTask(String taskId, Long ms) {
        List<Pomodoro> tasks = readAll();
        Iterator<Pomodoro> it = tasks.iterator();
        while (it.hasNext()) {
            Pomodoro t = it.next();
            if (t.getId().equals(taskId)) {
                Pomodoro task = new Pomodoro(t.start(), new Duration(ms), t.text(), t.project());
                task.setId(t.getId());
                it.remove();
                tasks.add(task);
                break;
            }
        }
        writeAll(tasks);
    }

    private String csv(Pomodoro task) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.start())
                + SEP
                + task.getId()
                + SEP
                + task.duration().asMs()
                + SEP
                + Optional.ofNullable(task.project()).orElse("")
                + SEP
                + Optional.ofNullable(task.text()).orElse("");
    }

    private PomodoroTask task(String csv) {
        String[] data = csv.split(SEP, 5);
        Timestamp start = Timestamp.valueOf(data[0]);
        String uuid = data[1];
        Long ms = Long.valueOf(data[2]);
        String project = data[3];
        String text = data[4];
        PomodoroTask task = new PomodoroTask(start, new Duration(ms), text, project);
        task.setId(uuid);
        return task;
    }

    private List<Pomodoro> readAll() {
        List<Pomodoro> tasks = new ArrayList<>();
        touchFile();
        try (BufferedReader br = new BufferedReader(new FileReader(file(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null)
                tasks.add(task(line));
            tasks.sort(Comparator.comparing(Pomodoro::start).reversed());
            return tasks;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void touchFile() {
        try {
            file().createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeAll(List<Pomodoro> tasks) {
        tasks.sort(Comparator.comparing(Pomodoro::start).reversed());
        try (PrintWriter pw = new PrintWriter(file(), StandardCharsets.UTF_8)) {
            for (Pomodoro task : tasks)
                pw.println(csv(task));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File file() {
        return Paths.get(System.getProperty("user.dir"), FILENAME).toFile();
    }
}
