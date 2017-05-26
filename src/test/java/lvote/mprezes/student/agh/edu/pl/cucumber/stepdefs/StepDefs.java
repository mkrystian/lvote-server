package lvote.mprezes.student.agh.edu.pl.cucumber.stepdefs;

import lvote.mprezes.student.agh.edu.pl.LvoteApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = LvoteApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
