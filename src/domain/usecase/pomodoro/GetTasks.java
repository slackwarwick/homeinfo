package domain.usecase.pomodoro;

import domain.entity.pomodoro.Pomodoro;
import domain.usecase.BaseAction;

import java.util.List;

public abstract class GetTasks extends BaseAction<GetTasks, List<Pomodoro>, PomodoroStore> {

}
