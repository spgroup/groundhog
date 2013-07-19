package br.ufpe.cin.groundhog;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ufpe.cin.groundhog.answers.ToGetProjectsWithMoreThanOneLanguage;
import br.ufpe.cin.groundhog.answers.ToGetTopMostUsedLanguages;

@RunWith(Suite.class)
@SuiteClasses({ ToGetProjectsWithMoreThanOneLanguage.class,
		ToGetTopMostUsedLanguages.class })
public class AllAnswers {

}
