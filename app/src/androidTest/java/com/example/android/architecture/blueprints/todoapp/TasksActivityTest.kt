package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksActivityTest{

    private lateinit var repository: TasksRepository


    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository

    }

    @After
    fun cleanRepository() = runBlockingTest {
        repository.clearCompletedTasks()
    }


    @Test
    fun editTask() = runBlockingTest {

      repository.saveTask(Task("Task", "Descricao"))

      val launch =  ActivityScenario.launch(TasksActivity::class.java)

      onView(withText("Task")).perform(click())
      onView(withId(R.id.task_detail_title_text))
          .check(ViewAssertions.matches(ViewMatchers.withText("Task")))
      onView(withId(R.id.task_detail_description_text))
          .check(matches(withText("Descricao")))
      onView(withId(R.id.task_detail_complete_checkbox))
          .check(matches(IsNot.not(isChecked())))


        Thread.sleep(4000)

      // editar uma task
      onView(withId(R.id.edit_task_fab)).perform(click())

        Thread.sleep(4000)

      onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("Nova Task"))
      onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("Nova Descricao"))


        Thread.sleep(4000)

      onView(withId(R.id.save_task_fab)).perform(click())

      onView(withText("Task")).check(doesNotExist())
      onView(withText("Nova Task")).check(matches(isDisplayed()))

      launch.close()
    }


}