package com.assignment.todoapp.unit;

import com.assignment.todoapp.models.Task;
import com.assignment.todoapp.repository.TaskRespository;
import com.assignment.todoapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
//import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static Task task1;
    private static Task task2;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRespository taskRespository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init() {
        task1 = new Task("title1", "desc1");
        task2 = new Task("title2", "desc2");
        task1.setId(1);
        task2.setId(2);
    }

    @Test
    public void it_should_return_when_a_todo_is_created(){
        Mockito.when(taskRespository.save(task1)).thenReturn(task1);
        assertThat(taskService.createTask(task1), is(task1));
        Mockito.verify(taskRespository, Mockito.times(1)).save(task1);

        Mockito.when(taskRespository.save(task2)).thenReturn(task2);
        assertThat(taskService.createTask(task2), is(task2));
        Mockito.verify(taskRespository, Mockito.times(1)).save(task2);

    }

    @Test
    public void a_todo_should_return_when_it_is_view(){
        Mockito.when(taskRespository.findById(1)).thenReturn(Optional.of(task1));
        Mockito.when(taskRespository.save(task1)).thenReturn(task1);
        assertThat(taskService.viewTask(1), is(task1));
        Mockito.verify(taskRespository, Mockito.times(1)).findById(1);
        Mockito.verify(taskRespository, Mockito.times(1)).save(task1);

        Mockito.when(taskRespository.findById(2)).thenReturn(Optional.of(task2));
        Mockito.when(taskRespository.save(task2)).thenReturn(task2);
        assertThat(taskService.viewTask(2), is(task2));
        Mockito.verify(taskRespository, Mockito.times(1)).findById(2);
        Mockito.verify(taskRespository, Mockito.times(1)).save(task2);
    }

    @Test
    public void all_todo_should_test_when_all_is_viewed() {
        Mockito.when(taskRespository.findAll()).thenReturn(Arrays.asList(task1,task2));
        assertThat(taskService.viewAllTask().get(0), is(task1));
        Mockito.verify(taskRespository, Mockito.times(1)).findAll();
    }

    @Test
    public void view_a_todo_by_status(){
        Mockito.when(taskRespository.findByStatus("pending")).thenReturn(Arrays.asList(task1,task2));
        assertThat(taskService.viewByStatus(task1.getStatus()).size(), is(2));
        Mockito.verify(taskRespository, Mockito.times(1)).findByStatus("pending");
    }

    @Test
    public void delete_a_todo(){
        taskService.deleteTask(task1.getId());
        Mockito.verify(taskRespository, Mockito.times(1)).existsById(task1.getId());
    }
}
