/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.

Oracle and Java are registered trademarks of Oracle and/or its affiliates.
Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package jsf2.demo.scrum.web.controller;

import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.CoolStoryBro;
import jsf2.demo.scrum.model.entities.Task;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = "dashboardManager")
@ViewScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{taskManager}")
    private TaskManager taskManager;
    @ManagedProperty("#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty("#{storyManager}")
    private StoryManager storyManager;


    private ListDataModel<Task> toDoTasks;
    private ListDataModel<Task> workingTasks;
    private ListDataModel<Task> doneTasks;

    @PreDestroy
    public void destroy() {
	toDoTasks = null;
	workingTasks = null;
	doneTasks = null;
    }

    public Sprint getSprint() {
        return getSprintManager().getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        this.getSprintManager().setCurrentSprint(sprint);
    }

    public DataModel<CoolStoryBro> getStories() {
        return storyManager.getStories();
    }


    public void setStories(DataModel<CoolStoryBro> stories) {
        storyManager.setStories(stories);
    }

    public ListDataModel getToDoTasks() {
        List toDoTasksList = new ArrayList();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel(toDoTasksList);
        }
        for (CoolStoryBro story : storyManager.getStoryList()) {
            toDoTasksList.addAll(story.getTodoTasks());
        }
        toDoTasks = new ListDataModel(toDoTasksList);
        return toDoTasks;
    }

    public ListDataModel getWorkingTasks() {
        List workingTasksList = new ArrayList();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel(workingTasksList);
        }
        for (CoolStoryBro story : storyManager.getStoryList()) {
            workingTasksList.addAll(story.getWorkingTasks());
        }
        workingTasks = new ListDataModel(workingTasksList);
        return workingTasks;
    }

    public ListDataModel getDoneTasks() {
        List doneTasksList = new ArrayList();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel(doneTasksList);
        }
        for (CoolStoryBro story : storyManager.getStoryList()) {
            doneTasksList.addAll(story.getDoneTasks());
        }
        doneTasks = new ListDataModel(doneTasksList);
        return doneTasks;
    }

    private String editTask(Task currentTask) {
        if (currentTask == null)
            return "";

        taskManager.setCurrentTask(currentTask);
        CoolStoryBro currentStory = storyManager.getCurrentStory();
        if (currentStory != currentTask.getStory()) {
            storyManager.setCurrentStory(currentTask.getStory());
        }
        return "/task/edit";
    }

    public String editToDoTask() {
        return editTask(toDoTasks.getRowData());
    }

    public String editDoneTask() {
        return editTask(doneTasks.getRowData());
    }

    public String editWorkingTask() {
        return editTask(workingTasks.getRowData());
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

}
